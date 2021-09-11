package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.DuplicatedArticleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleEditorService {
    private final ArticleService articleService;

    public void loadArticle(User author, String title, String content, String summary, String action) throws DuplicatedArticleException {
        Article article = new Article();
        article.setAuthor(author);
        article.setTitle(title);
        article.setContent(content.getBytes());
        article.setSummary(summary);
        article.setState(switch (action.toLowerCase()) {
            case "publish" -> ArticleState.ARTICLE_PUBLISHED;
            case "save" -> ArticleState.ARTICLE_IN_EDITING;
            default -> throw new IllegalArgumentException();
        });
        articleService.saveArticle(article);
    }
}
