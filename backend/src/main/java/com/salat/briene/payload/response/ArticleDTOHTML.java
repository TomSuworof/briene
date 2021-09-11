package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;

public class ArticleDTOHTML extends ArticleDTO {
    public ArticleDTOHTML(Article article) {
        super(article);
        this.content = article.makeHTML();
    }
}
