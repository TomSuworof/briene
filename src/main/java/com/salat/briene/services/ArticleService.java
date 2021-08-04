package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.exceptions.ArticleFoundException;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.IllegalArticleExtensionException;
import com.salat.briene.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    private boolean isMarkdown(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename()).matches("(.*).md");
        // return Objects.equals(file.getContentType(), "md");
    }

    public void saveFile(String title, MultipartFile file) throws IOException {
        Article article = new Article(title, file);
        if (!isMarkdown(file)) {
            throw new IllegalArticleExtensionException();
        }
        article.setState(ArticleState.ARTICLE_PUBLISHED);
        saveArticle(article);
    }

    public void saveArticle(Article newArticle) throws ArticleFoundException {
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

    public void deleteArticle(Long articleId) throws ArticleNotFoundException {
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

    public List<Article> getArticlesByType(String articleType) {
        if (articleType == null) {
            return getAllArticles();
        }

        ArticleState state;
        switch (articleType) {
            case "published" -> state = ArticleState.ARTICLE_PUBLISHED;
            case "drafts" -> state = ArticleState.ARTICLE_IN_EDITING;
            default -> {
                return getAllArticles();
            }
        }

        return getAllArticles().stream()
                .filter(article -> article.getState().equals(state))
                .collect(Collectors.toList());
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }
}