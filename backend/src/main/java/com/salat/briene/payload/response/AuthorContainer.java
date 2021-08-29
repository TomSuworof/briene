package com.salat.briene.payload.response;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AuthorContainer {
    private final String username;
    private final List<ArticleContainer> articles;

    public AuthorContainer(User user, List<Article> articles) {
        this.username = user.getUsername();
        this.articles = articles.stream().map(ArticleContainerHTML::new).collect(Collectors.toList());
    }
}