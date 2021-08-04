package com.salat.briene.entities;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.*;
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

    @Column
    private String filename;

    @Column
    private byte[] content;

    @Column
    private ArticleState state;

    public Article(String title, MultipartFile file) throws IOException {
        this.title = title;
        this.filename = file.getOriginalFilename();
        this.content = file.getBytes();
    }

    @Deprecated
    private File makeFile() {
        File file = new File(this.filename);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(this.content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public String makeHTML() throws IOException {
        MutableDataSet options = new MutableDataSet();

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
        Node document = parser.parse(new String(content));
        System.out.println(renderer.render(document));
        return renderer.render(document);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id) && Objects.equals(title, article.title) && Objects.equals(filename, article.filename) && Arrays.equals(content, article.content);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, filename, state);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
