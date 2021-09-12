package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public abstract class ArticleDTO {
    protected final UUID id;
    protected final String title;
    protected final String author;
    protected String content;
    protected String summary;
    protected final OffsetDateTime publicationDate;

    public ArticleDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.author = article.getAuthor().getUsername();
        this.summary = article.getSummary();
        this.publicationDate = article.getPublicationDate();
    }
}
