package com.salat.briene.services;

import com.salat.briene.entities.*;
import com.salat.briene.exceptions.DuplicatedArticleException;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.IllegalArticleStateException;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.repositories.ArticleRepository;
import com.salat.briene.repositories.ArticleSearchRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleSearchRepository articleSearchRepository;

    private final TagService tagService;
    private final AuthorService authorService;

    public void saveArticle(Article newArticle) throws EmailException {
        switch (newArticle.getState()) {
            case PUBLISHED -> publish(newArticle);
            case IN_EDITING -> saveDraft(newArticle);
            default -> throw new IllegalArticleStateException();
        }
    }

    private void saveDraft(Article newArticle) {
        if (articleRepository.findByUrl(newArticle.getUrl()).isPresent()) {
            throw new DuplicatedArticleException();
        }

        Optional<Article> oldArticleOpt = articleRepository.findArticleByTitleAndStateAndAuthor_Username(
                newArticle.getTitle(),
                newArticle.getState(),
                newArticle.getAuthor().getUsername());

        if (oldArticleOpt.isPresent()) {
            Article oldArticle = oldArticleOpt.get();
            newArticle.setId(oldArticle.getId());

            articleRepository.delete(oldArticle);
            articleSearchRepository.delete(oldArticle);
        }

        articleRepository.save(newArticle);
        articleSearchRepository.save(newArticle);
    }

    private void publish(Article newArticle) throws EmailException {
        Optional<Article> oldArticleOfSameAuthor = articleRepository.findArticleByTitleAndStateAndAuthor_Username(
                newArticle.getTitle(),
                newArticle.getState(),
                newArticle.getAuthor().getUsername());

        if (oldArticleOfSameAuthor.isPresent()) {
            Optional<Article> oldArticleWithSameUrl = articleRepository.findByUrl(newArticle.getUrl());

            if (oldArticleWithSameUrl.isPresent()) {
                Article oldArticle = oldArticleWithSameUrl.get();

                String oldArticleUsername = oldArticle.getAuthor().getUsername();
                String newArticleUsername = newArticle.getAuthor().getUsername();

                if (!oldArticleUsername.equals(newArticleUsername)) {
                    // title, state, url are the same, but different authors - error
                    throw new DuplicatedArticleException();
                }
            }

            // title, state, url, author are the same - just update
            Article oldArticle = oldArticleOfSameAuthor.get();
            newArticle.setPublicationDate(oldArticle.getPublicationDate());

            newArticle.setId(oldArticle.getId());

            articleRepository.delete(oldArticle);
            articleSearchRepository.delete(oldArticle);

            articleRepository.save(newArticle);
            articleSearchRepository.save(newArticle);

            authorService.notifyAboutNewArticle(newArticle);

            return;
        }

        // same url, but different titles, states or authors - error
        if (articleRepository.findByUrl(newArticle.getUrl()).isPresent()) {
            throw new DuplicatedArticleException();
        }

        articleRepository.save(newArticle);
        articleSearchRepository.save(newArticle);

        authorService.notifyAboutNewArticle(newArticle);
    }

    public void deleteArticleById(UUID articleId) {
        if (articleRepository.findById(articleId).isPresent()) {
            articleRepository.deleteArticleById(articleId);
            articleSearchRepository.deleteById(articleId);
        } else {
            throw new ArticleNotFoundException();
        }
    }

    public Article getArticleById(UUID id) {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            return articleOptional.get();
        } else {
            throw new ArticleNotFoundException();
        }
    }

    public Article getArticleByUrl(String url) {
        Optional<Article> articleOptional = articleRepository.findByUrl(url);
        if (articleOptional.isPresent()) {
            return articleOptional.get();
        } else {
            throw new ArticleNotFoundException();
        }
    }


    public ArticleDTO getNextArticle(String url) {
        return getArticleWithOffset(url, 1);
    }

    public ArticleDTO getPreviousArticle(String url) {
        return getArticleWithOffset(url, -1);
    }

    private ArticleDTO getArticleWithOffset(String url, int offset) {
        Article article = getArticleByUrl(url);
        List<Article> articles = articleRepository.findArticlesByStateAndAuthor_Username(ArticleState.PUBLISHED, article.getAuthor().getUsername());
        articles.sort(Comparator.comparing(Article::getPublicationDate));

        int articleIndex = articles.indexOf(article);

        try {
            return new ArticleDTO(articles.get(articleIndex + offset));
        } catch (IndexOutOfBoundsException e) {
            throw new ArticleNotFoundException();
        }
    }


    public List<ArticleDTO> getSuggestedArticles(String url) {
        Article mainArticle = getArticleByUrl(url);
        Set<String> mainTags = mainArticle.getTags().stream().map(Tag::getName).collect(Collectors.toSet());

        Set<ArticleDTO> suggestedArticles = new LinkedHashSet<>();

        for (String tag : mainTags) {
            suggestedArticles.addAll(tagService.getPageWithArticles(tag, 10, 0).getEntities());
        }
        suggestedArticles.remove(new ArticleDTO(mainArticle));

        Map<ArticleDTO, Integer> rating = new HashMap<>();

        for (ArticleDTO articleDTO : suggestedArticles) {
            Set<String> tags = new HashSet<>(articleDTO.getTags());
            tags.retainAll(mainTags);
            rating.put(articleDTO, tags.size());
        }

        return rating
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .limit(3) // make a parameter
                .toList();
    }


    public PageResponseDTO<ArticleDTO> getPageWithArticlesByAuthorAndStatePaginated(User author, ArticleState state, Integer limit, Integer offset) {
        Page<Article> articles;

        if (state.equals(ArticleState.ALL)) {
            articles = getAllArticlesByAuthorPaginated(author, limit, offset);
        } else {
            articles = getArticlesByAuthorAndStatePaginated(author, state, limit, offset);
        }

        return new PageResponseDTO<>(
                offset > 0 && articles.getTotalElements() > 0,
                (offset + limit) < articles.getTotalElements(),
                articles.getContent().stream().map(ArticleDTO::new).toList(),
                articles.getTotalElements());
    }

    private Page<Article> getAllArticlesByAuthorPaginated(User author, Integer limit, Integer offset) {
        return articleRepository.findArticlesByAuthor(author, getDefaultPageable(limit, offset));
    }

    private Page<Article> getArticlesByAuthorAndStatePaginated(User author, ArticleState state, Integer limit, Integer offset) {
        return articleRepository.findArticlesByAuthorAndState(author, state, getDefaultPageable(limit, offset));
    }


    public PageResponseDTO<ArticleDTO> getPageWithArticlesByState(ArticleState state, Integer limit, Integer offset) {
        // here we will store response
        Page<Article> articles;

        if (state.equals(ArticleState.ALL)) {
            articles = getAllArticlesPaginated(limit, offset);
        } else {
            articles = getArticlesByStatePaginated(state, limit, offset);
        }

        // return response after filtering
        return new PageResponseDTO<>(
                offset > 0 && articles.getTotalElements() > 0,
                (offset + limit) < articles.getTotalElements(),
                articles.getContent().stream().map(ArticleDTO::new).toList(),
                articles.getTotalElements());
    }

    private Page<Article> getArticlesByStatePaginated(ArticleState state, Integer limit, Integer offset) {
        return articleRepository.findArticlesByState(state, getDefaultPageable(limit, offset));
    }

    private Page<Article> getAllArticlesPaginated(Integer limit, Integer offset) {
        return articleRepository.findAll(getDefaultPageable(limit, offset));
    }


    public boolean canUserEditArticle(User user, Article article) {
        if (user == null) {
            return false;
        }
        return user.is(RoleEnum.ADMIN) || article.getAuthor().equals(user);
    }


    private Pageable getDefaultPageable(Integer limit, Integer offset) {
        return PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate"));
    }
}