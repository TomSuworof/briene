package com.salat.briene.api.containers;

import com.salat.briene.entities.Article;
import lombok.Getter;

import java.util.Date;

@Getter
public abstract class ArticleContainer {
    protected final Long id;
    protected final String title;
    protected final String author;
    protected String content;
    protected final Date publicationDate;

    public ArticleContainer(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.author = article.getAuthor().getUsername();
        this.publicationDate = article.getPublicationDate();
    }
}
