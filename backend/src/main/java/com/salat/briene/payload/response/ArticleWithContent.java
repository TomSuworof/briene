package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;
import lombok.Getter;

@Getter
public class ArticleWithContent extends ArticleDTO {
    private final String content;

    public ArticleWithContent(Article article) {
        super(article);
        this.content = article.getContent();
    }
}
