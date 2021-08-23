package com.salat.briene.api;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.IllegalArticleStateException;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.services.ArticleEditorService;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:8081")
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ApiArticlesController {
    private final ArticleService articleService;
    private final ArticleEditorService articleEditorService;
    private final UserService userService;

    @Getter
    private static class ArticleContainer {
        private final Long id;
        private final String title;
        private final String author;
        private final String htmlContent;
        private final Date publicationDate;

        public ArticleContainer(Article article) {
            this.id = article.getId();
            this.title = article.getTitle();
            this.author = article.getAuthor().getUsername();
            this.htmlContent = article.makeHTML();
            this.publicationDate = article.getPublicationDate();
        }
    }

    @GetMapping
    public ResponseEntity<?> getArticles() {
        try {
            List<ArticleContainer> publishedArticles = articleService.getArticlesByState("published")
                    .stream().map(ArticleContainer::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(publishedArticles);
        } catch (IllegalArticleStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticle(@PathVariable Long id, @RequestHeader(name = "Authorization") String accessToken) {
        try {
            Article article = articleService.getArticleById(id);

            if (article.getState().equals(ArticleState.ARTICLE_IN_EDITING)) {
                User userFromToken = userService.getUserFromToken(accessToken);

                if (!articleService.canUserEditArticle(userFromToken, article)) {
                    throw new ArticleNotFoundException();
                }
            }

            return ResponseEntity.ok().body(new ArticleContainer(article));
        } catch (ArticleNotFoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/load")
    public ResponseEntity<?> publishArticle(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String action,
            @RequestHeader(name = "Authorization") String accessToken) {
        try {
            User userFromToken = userService.getUserFromToken(accessToken);
            articleEditorService.loadArticle(userFromToken, title, content, action);
            return ResponseEntity.ok().body("Article was published or saved");
        } catch (IOException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body("Error occurred while saving article. Try to remove article with same title");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id, @RequestHeader(name = "Authorization") String accessToken) {
        try {
            Article article = articleService.getArticleById(id);

            User userFromToken = userService.getUserFromToken(accessToken);
            if (article.getAuthor().equals(userFromToken)) {
                articleService.deleteArticleById(id);
                return ResponseEntity.ok().body("Article " + id + " was deleted");
            } else {
                throw new ArticleNotFoundException();
            }
        } catch (ArticleNotFoundException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body("Article " + id + " can not be deleted");
        }
    }
}
