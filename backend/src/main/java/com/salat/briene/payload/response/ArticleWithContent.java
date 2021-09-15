package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;
import lombok.Getter;

@Getter
public abstract class ArticleWithContent extends ArticleDTO {
    protected String content;

    public ArticleWithContent(Article article) {
        super(article);
    }
}
