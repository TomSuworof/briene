package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;

public class ArticleWithContentHTML extends ArticleWithContent {
    public ArticleWithContentHTML(Article article) {
        super(article);
        this.content = article.makeHTML();
    }
}
