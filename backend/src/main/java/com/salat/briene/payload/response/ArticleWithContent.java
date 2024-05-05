// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

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
