package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.payload.request.ArticleUploadRequest;
import com.salat.briene.payload.response.ArticleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleEditorService {
    private final ArticleService articleService;
    private final TagService tagService;

    public ArticleDTO uploadArticle(User author, ArticleUploadRequest articleUploadRequest, String action) {
        Article article = new Article();
        article.setId(articleUploadRequest.getId() == null ? article.getId() : articleUploadRequest.getId());
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
        return new ArticleDTO(article);
    }
}
