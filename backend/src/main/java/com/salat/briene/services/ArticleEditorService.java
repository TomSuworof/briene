package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.payload.request.ArticleUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleEditorService {
    private final ArticleService articleService;

    public void uploadArticle(User author, ArticleUploadRequest articleUploadRequest, String action) {
        Article article = new Article();
        article.setAuthor(author);
        article.setTitle(articleUploadRequest.getTitle());
        article.setContent(articleUploadRequest.getContent().getBytes());
        article.setSummary(articleUploadRequest.getSummary());
        article.setState(ArticleState.getFromAction(action.toLowerCase()));
        articleService.saveArticle(article);
    }
}
