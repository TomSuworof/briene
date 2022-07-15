package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;
import lombok.Getter;

import java.util.List;

@Getter
public class ArticleWithContent extends ArticleDTO {
    private final String content;
    private final List<CommentDTO> comments;

    public ArticleWithContent(Article article) {
        super(article);
        this.content = article.getContent();
        this.comments = article.getComments().stream().map(CommentDTO::new).toList();
    }
}
