package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.ArticleFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleEditorService {
    private final ArticleService articleService;

    public void loadArticle(User author, String title, String content, String action) throws ArticleFoundException {
        Article article = new Article();
        article.setAuthor(author);
        article.setTitle(title);
        article.setContent(content.getBytes());
        article.setState(switch (action) {
            case "Publish" -> ArticleState.ARTICLE_PUBLISHED;
            case "Save" -> ArticleState.ARTICLE_IN_EDITING;
            default -> throw new IllegalArgumentException();
        });
        articleService.saveArticle(article);
    }
}
