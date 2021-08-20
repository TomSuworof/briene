package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.ArticleFoundException;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.IllegalArticleStateException;
import com.salat.briene.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public void saveArticle(Article newArticle) throws ArticleFoundException {
        newArticle.setPublicationDate(new Date());
        newArticle.setId((long) newArticle.hashCode());

        Optional<Article> oldArticleOpt = articleRepository.findArticleByTitleAndState(newArticle.getTitle(), newArticle.getState());

        if (oldArticleOpt.isPresent()) {
            Article oldArticle = oldArticleOpt.get();
            if (newArticle.getState().equals(ArticleState.ARTICLE_IN_EDITING)) {
                articleRepository.delete(oldArticle);
                articleRepository.save(newArticle);
            } else {
                throw new ArticleFoundException();
            }
        } else {
            articleRepository.save(newArticle);
        }
    }

    public void deleteArticleById(Long articleId) throws ArticleNotFoundException {
        if (articleRepository.findById(articleId).isPresent()) {
            articleRepository.deleteById(articleId);
        } else {
            throw new ArticleNotFoundException();
        }
    }

    public Article getArticleById(Long id) throws ArticleNotFoundException {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            return articleOptional.get();
        } else {
            throw new ArticleNotFoundException();
        }
    }

    public List<Article> getArticlesByState(String articleState) throws IllegalArticleStateException {
        if (articleState == null) {
            throw new IllegalArticleStateException();
        }

        ArticleState state;
        switch (articleState) {
            case "published" -> state = ArticleState.ARTICLE_PUBLISHED;
            case "drafts" -> state = ArticleState.ARTICLE_IN_EDITING;
            case "all" -> {
                return getAllArticles();
            }
            default -> throw new IllegalArticleStateException();
        }

        return articleRepository.findArticlesByState(state);
    }

    private List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public List<Article> getArticlesByAuthorAndState(User author, String articleState) throws IllegalArticleStateException {
        if (articleState == null) {
            throw new IllegalArticleStateException();
        }

        ArticleState state;
        switch (articleState) {
            case "published" -> state = ArticleState.ARTICLE_PUBLISHED;
            case "drafts" -> state = ArticleState.ARTICLE_IN_EDITING;
            case "all" -> {
                return getArticlesByAuthor(author);
            }
            default -> throw new IllegalArticleStateException();
        }

        return articleRepository.findArticlesByAuthorAndState(author, state);
    }

    private List<Article> getArticlesByAuthor(User author) {
        return articleRepository.findArticlesByAuthor(author);
    }

    public boolean canUserEditArticle(User user, Article article) {
        if (user == null) {
            return false;
        }

        return user.is("admin") || article.getAuthor().equals(user);
    }
}