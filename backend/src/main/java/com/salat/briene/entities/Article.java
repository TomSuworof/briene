package com.salat.briene.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "t_articles")
@Document(indexName = "articles")
public class Article {
    @Id
    @NotNull
    private UUID id;

    @NotBlank(message = ConstraintViolationMessage.ARTICLE_TITLE_EMPTY)
    @Field(type = FieldType.Text)
    private String title;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    @Field(type = FieldType.Nested, ignoreFields = {"bookmarkedArticles"})
    private User author;

    @Size(min = 1, message = ConstraintViolationMessage.ARTICLE_CONTENT_EMPTY)
    @Column(columnDefinition = "text")
    @Field(type = FieldType.Text)
    @ToString.Exclude
    private String content;

    @Size(max = 500)
    @Field(type = FieldType.Text)
    private String summary;

    @NotNull
    @Field(type = FieldType.Keyword)
    private ArticleState state;

    @PastOrPresent(message = ConstraintViolationMessage.ARTICLE_PUBLICATION_DATE_INVALID)
    @Field(type = FieldType.Date)
    private OffsetDateTime publicationDate;

    @NotNull
    @Size(max = 140)
    @Field(type = FieldType.Text)
    private String url;

    @OneToMany
    @JoinTable(name = "t_articles_comments",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "comment_id")})
    @org.springframework.data.annotation.Transient
    private Set<Comment> comments;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "t_articles_tags",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tags_id")})
    @org.springframework.data.annotation.Transient
    private Set<Tag> tags;

    public Article() {
        this.id = UUID.randomUUID();
        this.publicationDate = OffsetDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.getId()) &&
                Objects.equals(title, article.getTitle()) &&
                Objects.equals(author, article.getAuthor()) &&
                Objects.equals(content, article.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, content, state, publicationDate);
    }
}
