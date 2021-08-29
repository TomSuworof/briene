package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;

public class ArticleContainerHTML extends ArticleContainer {
    public ArticleContainerHTML(Article article) {
        super(article);
        this.content = article.makeHTML();
    }
}
