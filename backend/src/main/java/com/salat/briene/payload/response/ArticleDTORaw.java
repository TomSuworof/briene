package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;

public class ArticleDTORaw extends ArticleDTO {
    public ArticleDTORaw(Article article) {
        super(article);
        this.content = new String(article.getContent());
    }
}
