package com.salat.briene.entities;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Data
@Entity
@Table(name = "t_articles")
@NoArgsConstructor
public class Article {

    @Id
    private Long id;

    @Column
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column
    private byte[] content;

    @Column
    private ArticleState state;

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
        int result = Objects.hash(id, title, author, state);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
