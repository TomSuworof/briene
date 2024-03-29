package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ArticleWithContent extends ArticleDTO {
    @ToString.Exclude private final String content;

    public ArticleWithContent(Article article) {
        super(article);
        this.content = article.getContent();
    }
}
