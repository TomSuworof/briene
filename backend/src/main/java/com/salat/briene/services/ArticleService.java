package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.RoleEnum;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.DuplicatedArticleException;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.repositories.ArticleRepository;
import com.salat.briene.repositories.ArticleSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleSearchRepository articleSearchRepository;

    public void saveArticle(Article newArticle) {
        newArticle.setPublicationDate(OffsetDateTime.now());

        Optional<Article> oldArticleOpt = articleRepository.findArticleByTitleAndState(newArticle.getTitle(), newArticle.getState());

        if (oldArticleOpt.isPresent()) {
            Article oldArticle = oldArticleOpt.get();
            if (newArticle.getState().equals(ArticleState.IN_EDITING)) {
                articleRepository.delete(oldArticle);
                articleSearchRepository.delete(oldArticle);

                articleRepository.save(newArticle);
                articleSearchRepository.save(newArticle);
            } else {
                throw new DuplicatedArticleException();
            }
        } else {
            articleRepository.save(newArticle);
            articleSearchRepository.save(newArticle);
        }
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
                articles.getContent().stream().map(ArticleDTO::new).collect(Collectors.toList())
        );
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
                articles.getContent().stream().map(ArticleDTO::new).collect(Collectors.toList())
        );
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