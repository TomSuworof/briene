package com.salat.briene.api.containers;

import com.salat.briene.entities.Article;

public class ArticleContainerRaw extends ArticleContainer {
    public ArticleContainerRaw(Article article) {
        super(article);
        this.content = new String(article.getContent());
    }
}
