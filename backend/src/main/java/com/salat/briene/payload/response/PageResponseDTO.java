package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PageResponseDTO {
    private final boolean hasBefore;
    private final boolean hasAfter;
    private final List<ArticleDTO> articles;

    public PageResponseDTO(boolean hasBefore, boolean hasAfter, List<Article> articles) {
        this.hasBefore = hasBefore;
        this.hasAfter = hasAfter;
        this.articles = articles.stream().map(ArticleDTO::new).collect(Collectors.toList());
    }
}