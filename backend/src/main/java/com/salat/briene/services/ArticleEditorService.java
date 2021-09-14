package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.payload.request.ArticleLoadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleEditorService {
    private final ArticleService articleService;

    public void loadArticle(User author, ArticleLoadRequest articleLoadRequest, String action) {
        Article article = new Article();
        article.setAuthor(author);
        article.setTitle(articleLoadRequest.getTitle());
        article.setContent(articleLoadRequest.getContent().getBytes());
        article.setSummary(articleLoadRequest.getSummary());
        article.setState(ArticleState.getFromAction(action.toLowerCase()));
        articleService.saveArticle(article);
    }
}
