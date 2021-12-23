package com.salat.briene.entities;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "t_articles")
@NoArgsConstructor
public class Article {

    @Id
    @NotNull
    @GeneratedValue
    private UUID id;

    @NotBlank(message = ConstraintViolationMessage.ARTICLE_TITLE_EMPTY)
    private String title;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Size(min = 1, message = ConstraintViolationMessage.ARTICLE_CONTENT_EMPTY)
    private byte[] content;

    @Size(max = 255)
    private String summary;

    @NotNull
    private ArticleState state;

    @PastOrPresent(message = ConstraintViolationMessage.ARTICLE_PUBLICATION_DATE_INVALID)
    private OffsetDateTime publicationDate;

    public String makeHTML() {
        MutableDataSet options = new MutableDataSet();

        options.set(Parser.EXTENSIONS, List.of(
                TablesExtension.create(),
                StrikethroughExtension.create()
        ));

        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

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
