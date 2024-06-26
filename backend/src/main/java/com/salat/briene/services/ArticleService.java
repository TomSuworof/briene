// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.services;

import com.salat.briene.entities.*;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.DuplicatedArticleException;
import com.salat.briene.exceptions.IllegalArticleStateException;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
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

    private final TagService tagService;
    private final AuthorService authorService;
    private final SearchService searchService;

    public void saveArticle(Article newArticle) {
        if (newArticle.getState() == ArticleState.PUBLISHED) {
            publish(newArticle);
        } else if (newArticle.getState() == ArticleState.IN_EDITING) {
            saveDraft(newArticle);
        } else {
            throw new IllegalArticleStateException();
        }
    }

    protected void saveDraft(Article newArticle) {
        Optional<Article> oldArticleOpt = articleRepository.findById(newArticle.getId());

        if (oldArticleOpt.isEmpty()) {
            // completely new draft
            // and article with URL exists
            if (articleRepository.findByUrl(newArticle.getUrl()).isPresent()) {
                throw new DuplicatedArticleException();
            }
        }

        if (oldArticleOpt.isPresent()) {
            Article oldArticle = oldArticleOpt.get();

            // prevent from changing someone's article
            if (!newArticle.getAuthor().equals(oldArticle.getAuthor())) {
                throw new ArticleNotFoundException();
            }

            articleRepository.delete(oldArticle);
            searchService.delete(oldArticle.getId());
        }

        articleRepository.save(newArticle);
        searchService.save(newArticle);
    }

    protected void publish(Article newArticle) {
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
            searchService.delete(oldArticle.getId());

            articleRepository.save(newArticle);
            searchService.save(newArticle);

            authorService.notifyAboutNewArticle(newArticle);

            return;
        }

        // same url, but different titles, states or authors - error
        if (articleRepository.findByUrl(newArticle.getUrl()).isPresent()) {
            throw new DuplicatedArticleException();
        }

        articleRepository.save(newArticle);
        searchService.save(newArticle);

        authorService.notifyAboutNewArticle(newArticle);
    }

    public void deleteArticleById(UUID articleId) {
        if (articleRepository.findById(articleId).isPresent()) {
            articleRepository.deleteArticleById(articleId);
            searchService.delete(articleId);
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

    protected ArticleDTO getArticleWithOffset(String url, int offset) {
        Article article = getArticleByUrl(url);
        List<Article> articles = articleRepository.findArticlesByStateAndAuthor_Username(ArticleState.PUBLISHED,
                article.getAuthor().getUsername());
        articles.sort(Comparator.comparing(Article::getPublicationDate));

        try {
            int articleIndex = articles.indexOf(article);
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
                .limit(3) // TODO: make a parameter
                .collect(Collectors.toList());
    }

    public PageResponseDTO<ArticleDTO> getPageWithArticlesByAuthorAndStatePaginated(User author, ArticleState state,
            Integer limit, Integer offset) {
        Page<Article> articles;

        if (state.equals(ArticleState.ALL)) {
            articles = getAllArticlesByAuthorPaginated(author, limit, offset);
        } else {
            articles = getArticlesByAuthorAndStatePaginated(author, state, limit, offset);
        }

        return new PageResponseDTO<>(
                offset > 0 && articles.getTotalElements() > 0,
                (offset + limit) < articles.getTotalElements(),
                articles.getContent().stream().map(ArticleDTO::new).collect(Collectors.toList()),
                articles.getTotalElements());
    }

    protected Page<Article> getAllArticlesByAuthorPaginated(User author, Integer limit, Integer offset) {
        return articleRepository.findArticlesByAuthor(author, getDefaultPageable(limit, offset));
    }

    protected Page<Article> getArticlesByAuthorAndStatePaginated(User author, ArticleState state, Integer limit,
            Integer offset) {
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
                articles.getContent().stream().map(ArticleDTO::new).collect(Collectors.toList()),
                articles.getTotalElements());
    }

    protected Page<Article> getArticlesByStatePaginated(ArticleState state, Integer limit, Integer offset) {
        return articleRepository.findArticlesByState(state, getDefaultPageable(limit, offset));
    }

    protected Page<Article> getAllArticlesPaginated(Integer limit, Integer offset) {
        return articleRepository.findAll(getDefaultPageable(limit, offset));
    }

    public boolean canUserEditArticle(User user, Article article) {
        if (user == null) {
            return false;
        }
        return user.is(RoleEnum.ADMIN) || user.equals(article.getAuthor());
    }

    protected Pageable getDefaultPageable(Integer limit, Integer offset) {
        return PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate"));
    }
}