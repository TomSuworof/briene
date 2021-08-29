package com.salat.briene.api;

import com.salat.briene.payload.response.ArticleContainer;
import com.salat.briene.payload.response.ArticleContainerHTML;
import com.salat.briene.payload.response.ArticleContainerRaw;
import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.IllegalArticleStateException;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.services.ArticleEditorService;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ApiArticlesController {
    private final ArticleService articleService;
    private final ArticleEditorService articleEditorService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getArticles() {
        try {
            List<ArticleContainer> publishedArticles = articleService.getArticlesByState("published")
                    .stream().map(ArticleContainerHTML::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(publishedArticles);
        } catch (IllegalArticleStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getArticlesPaginated(@RequestParam Integer limit, @RequestParam Integer offset) {

        @Getter
        @AllArgsConstructor
        class PageContent {
            private boolean hasBefore;
            private List<ArticleContainer> articles;
            private boolean hasAfter;
        }

        try {
            List<ArticleContainer> publishedArticles = articleService.getArticlesByState("published")
                    .stream().map(ArticleContainerHTML::new)
                    .collect(Collectors.toList());

            boolean hasBefore = offset > 0 && publishedArticles.size() > 0;
            boolean hasAfter = (offset + limit) < publishedArticles.size();

            List<ArticleContainer> articles = publishedArticles.subList(
                    offset,
                    Math.min((offset + limit), publishedArticles.size()));
            // toIndex should be calculated as min of requested and actual ends

            return ResponseEntity.ok().body(new PageContent(hasBefore, articles, hasAfter));
        } catch (IllegalArticleStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyArticles(@RequestParam String state, Authentication authentication) {
        try {
            User currentUser = userService.getUserFromAuthentication(authentication);

            List<ArticleContainer> articles = articleService.getArticlesByAuthorAndState(currentUser, state)
                    .stream().map(ArticleContainerHTML::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(articles);
        } catch (UserNotFoundException | IllegalArticleStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticle(
            @RequestParam(required = false) Boolean raw,
            @PathVariable Long id,
            Authentication authentication) {
        try {
            ArticleContainer article = null;

            if (raw == null) {
                article = this.getArticleHTML(id, authentication);
            } else if (raw) {
                article = this.getArticleRaw(id, authentication);
            }

            return ResponseEntity.ok().body(article);
        } catch (ArticleNotFoundException | UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private ArticleContainer getArticleHTML(Long id, Authentication authentication) throws ArticleNotFoundException {
        Article article = articleService.getArticleById(id);

        if (article.getState().equals(ArticleState.ARTICLE_IN_EDITING)) {
            User userFromToken = userService.getUserFromAuthentication(authentication);

            if (!articleService.canUserEditArticle(userFromToken, article)) {
                throw new ArticleNotFoundException();
            }
        }

        return new ArticleContainerHTML(article);
    }

    private ArticleContainer getArticleRaw(Long id, Authentication authentication) throws ArticleNotFoundException {
        Article article = articleService.getArticleById(id);

        User currentUser = userService.getUserFromAuthentication(authentication);

        if (!articleService.canUserEditArticle(currentUser, article)) {
            throw new ArticleNotFoundException();
        }

        return new ArticleContainerRaw(article);
    }

    @PostMapping("/load")
    public ResponseEntity<?> publishArticle(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String action,
            Authentication authentication) {
        try {
            User userFromToken = userService.getUserFromAuthentication(authentication);
            articleEditorService.loadArticle(userFromToken, title, content, action);
            return ResponseEntity.ok().body("Article was published or saved");
        } catch (IOException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body("Error occurred while saving article. Try to remove article with same title");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id, Authentication authentication) {
        try {
            Article article = articleService.getArticleById(id);

            User currentUser = userService.getUserFromAuthentication(authentication);
            if (articleService.canUserEditArticle(currentUser, article)) {
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
