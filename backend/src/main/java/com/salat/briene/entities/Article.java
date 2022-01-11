package com.salat.briene.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "t_articles")
@NoArgsConstructor
@Document(indexName = "articles")
public class Article {

    @Id
    @NotNull
    @GeneratedValue
    private UUID id;

    @NotBlank(message = ConstraintViolationMessage.ARTICLE_TITLE_EMPTY)
    @Field(type = FieldType.Text)
    private String title;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    @Field(type = FieldType.Nested, includeInParent = true)
    private User author;

    @Size(min = 1, message = ConstraintViolationMessage.ARTICLE_CONTENT_EMPTY)
    @Field(type = FieldType.Binary)
    private byte[] content;

    @Size(max = 255)
    @Field(type = FieldType.Text)
    private String summary;

    @NotNull
    private ArticleState state;

    @PastOrPresent(message = ConstraintViolationMessage.ARTICLE_PUBLICATION_DATE_INVALID)
    private OffsetDateTime publicationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.getId()) &&
                Objects.equals(title, article.getTitle()) &&
                Objects.equals(author, article.getAuthor()) &&
                Arrays.equals(content, article.getContent());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, author, state, publicationDate);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
