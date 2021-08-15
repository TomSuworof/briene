package com.salat.briene.api;

import com.google.gson.Gson;
import com.salat.briene.entities.Article;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiAuthorController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/authors/{authorName}")
    public ResponseEntity<String> getAuthorPage(@PathVariable String authorName) throws UserNotFoundException {
        User userAuthor = userService.loadUserByUsername(authorName);
        List<Article> articles = articleService.getArticlesByAuthorAndState(userAuthor, "published");

        AuthorContainer author = new AuthorContainer(userAuthor, articles);
        return ResponseEntity.ok().body(new Gson().toJson(author));
    }

    @Getter
    private static class ArticleContainer {
        private final Long id;
        private final String title;

        public ArticleContainer(Article article) {
            this.id = article.getId();
            this.title = article.getTitle();
        }
    }

    @Getter
    private static class AuthorContainer {
        private final String username;
        private final List<ArticleContainer> articles;

        public AuthorContainer(User user, List<Article> articles) {
            this.username = user.getUsername();
            this.articles = articles.stream().map(ArticleContainer::new).collect(Collectors.toList());
        }
    }
}
