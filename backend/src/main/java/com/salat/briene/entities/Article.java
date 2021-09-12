package com.salat.briene.entities;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "t_articles")
@NoArgsConstructor
public class Article {

    @Id
    @NotNull
    private Long id;

    @NotBlank(message = "Title cannot be empty: it is shown in HTML title")
    private String title;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Size(min = 1, message = "Content cannot be empty")
    private byte[] content;

    @Size(max = 255)
    private String summary;

    @NotNull
    private ArticleState state;

    @PastOrPresent(message = "Article cannot be published in future")
    private OffsetDateTime publicationDate;

    public String makeHTML() {
        MutableDataSet options = new MutableDataSet();

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(new String(content));
        return renderer.render(document);
    }

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
