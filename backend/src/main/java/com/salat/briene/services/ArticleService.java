package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.RoleEnum;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.DuplicatedArticleException;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public void saveArticle(Article newArticle) throws DuplicatedArticleException {
        newArticle.setPublicationDate(OffsetDateTime.now());
//        newArticle.setId((long) newArticle.hashCode());

        Optional<Article> oldArticleOpt = articleRepository.findArticleByTitleAndState(newArticle.getTitle(), newArticle.getState());

        if (oldArticleOpt.isPresent()) {
            Article oldArticle = oldArticleOpt.get();
            if (newArticle.getState().equals(ArticleState.IN_EDITING)) {
                articleRepository.delete(oldArticle);
                articleRepository.save(newArticle);
            } else {
                throw new DuplicatedArticleException();
            }
        } else {
            articleRepository.save(newArticle);
        }
    }

    public void deleteArticleById(UUID articleId) throws ArticleNotFoundException {
        if (articleRepository.findById(articleId).isPresent()) {
            articleRepository.deleteById(articleId);
        } else {
            throw new ArticleNotFoundException();
        }
    }

    public Article getArticleById(UUID id) throws ArticleNotFoundException {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            return articleOptional.get();
        } else {
            throw new ArticleNotFoundException();
        }
    }


    public List<Article> getArticlesByState(ArticleState state) {
        if (state.equals(ArticleState.ALL)) {
            return getAllArticles();
        } else {
            return articleRepository.findArticlesByState(state);
        }
    }

    private List<Article> getAllArticles() {
        return articleRepository.findAll();
    }


    public List<Article> getArticlesByAuthorAndState(User author, ArticleState state) {
        if (state.equals(ArticleState.ALL)) {
            return getArticlesByAuthor(author);
        } else {
            return articleRepository.findArticlesByAuthorAndState(author, state);
        }
    }

    private List<Article> getArticlesByAuthor(User author) {
        return articleRepository.findArticlesByAuthor(author);
    }


    public PageResponseDTO getPageWithArticlesByState(ArticleState state, Integer limit, Integer offset) {
        // here we will store response
        List<Article> articles;
        long allArticlesSize;

        if (state.equals(ArticleState.ALL)) {
            // if all articles were requested, then execute SELECT and COUNT for all articles
            articles = getAllArticlesPaginated(limit, offset);
            allArticlesSize = articleRepository.count();
        } else {
            // if articles were requested with state, then execute SELECT and COUNT for articles with the state
            articles = getArticlesByStatePaginated(state, limit, offset);
            allArticlesSize = articleRepository.countArticlesByState(state);
        }

        // return response after filtering
        return new PageResponseDTO(
                offset > 0 && allArticlesSize > 0,
                (offset + limit) < allArticlesSize,
                articles
        );
    }

    private List<Article> getArticlesByStatePaginated(ArticleState state, Integer limit, Integer offset) {
        return articleRepository.findArticlesByState(state, PageRequest.of(offset / limit, limit));
    }

    private List<Article> getAllArticlesPaginated(Integer limit, Integer offset) {
        return articleRepository.findAll(PageRequest.of(offset / limit, limit)).stream().toList();
    }


    public boolean canUserEditArticle(User user, Article article) {
        if (user == null) {
            return false;
        }

        return user.is(RoleEnum.ADMIN) || article.getAuthor().equals(user);
    }
}