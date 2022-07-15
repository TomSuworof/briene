package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.payload.request.ArticleUploadRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ArticleEditorService {
    private final ArticleService articleService;
    private final TagService tagService;

    public void uploadArticle(User author, ArticleUploadRequest articleUploadRequest, String action) throws EmailException {
        Article article = new Article();
        article.setAuthor(author);
        article.setTitle(articleUploadRequest.getTitle());
        article.setContent(articleUploadRequest.getContent());
        article.setSummary(articleUploadRequest.getSummary());
        article.setState(ArticleState.getFromAction(action.toLowerCase()));
        article.setTags(tagService.mapToTags(articleUploadRequest.getTags()));
        article.setUrl(articleUploadRequest.getUrl());

        if (article.getUrl() == null) {
            article.setUrl(article.getTitle());
        }

        articleService.saveArticle(article);
    }
}
