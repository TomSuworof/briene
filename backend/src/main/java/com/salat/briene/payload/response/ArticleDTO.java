package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.Tag;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

// ArticleDTO provides short form of article without content
// It is helpful and recommended using when you need to get a list of articles

@Getter
@ToString
@EqualsAndHashCode
public class ArticleDTO implements ObjectDTO {
    protected final UUID id;
    protected final String title;
    protected final String author;
    protected String summary;
    protected final OffsetDateTime publicationDate;
    protected final String url;
    protected final String state;
    protected final List<String> tags;

    public ArticleDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.author = article.getAuthor().getUsername();
        this.summary = article.getSummary();
        this.publicationDate = article.getPublicationDate();
        this.url = article.getUrl();
        this.state = article.getState().getDescription();

        Set<Tag> tags = article.getTags();

        if (tags == null) {
            this.tags = Collections.emptyList();
        } else {
            this.tags = tags.stream().map(Tag::getName).collect(Collectors.toList());
        }
    }
}
