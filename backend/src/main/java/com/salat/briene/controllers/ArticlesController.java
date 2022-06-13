package com.salat.briene.controllers;

import com.salat.briene.exceptions.*;
import com.salat.briene.payload.request.ArticleUploadRequest;
import com.salat.briene.payload.response.*;
import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.services.ArticleEditorService;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticlesController {
    private static final String ARTICLE_UPLOADED = "Article was published or saved";
    private static final String ARTICLE_DELETED = "Article {%s} was deleted";

    private final ArticleService articleService;
    private final ArticleEditorService articleEditorService;
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<PageResponseDTO<ArticleDTO>> getArticlesPaginated(@RequestParam Integer limit, @RequestParam Integer offset) {
        PageResponseDTO<ArticleDTO> response = articleService.getPageWithArticlesByState(ArticleState.PUBLISHED, limit, offset);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    @GetMapping("/my_articles")
    public ResponseEntity<PageResponseDTO<ArticleDTO>> getMyArticlesPaginated(@RequestParam String state, @RequestParam Integer limit, @RequestParam Integer offset, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        PageResponseDTO<ArticleDTO> response = articleService.getPageWithArticlesByAuthorAndStatePaginated(currentUser, ArticleState.getFromDescription(state), limit, offset);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleWithContent> getArticle(@RequestParam(required = false) Boolean raw, @PathVariable UUID id, Authentication authentication) {
        ArticleWithContent article = null;

        if (raw == null) {
            article = this.getArticleForRender(id, authentication);
        } else if (raw) {
            article = this.getArticleRaw(id, authentication);
        }

        return ResponseEntity.ok().body(article);
    }

    private ArticleWithContent getArticleForRender(UUID id, Authentication authentication) {
        Article article = articleService.getArticleById(id);

        if (article.getState().equals(ArticleState.IN_EDITING)) {
            User userFromToken = userService.getUserFromAuthentication(authentication);

            if (!articleService.canUserEditArticle(userFromToken, article)) {
                throw new ArticleNotFoundException();
            }
        }

        return new ArticleWithContent(article);
    }

    private ArticleWithContent getArticleRaw(UUID id, Authentication authentication) {
        Article article = articleService.getArticleById(id);

        User currentUser = userService.getUserFromAuthentication(authentication);

        if (!articleService.canUserEditArticle(currentUser, article)) {
            throw new ArticleNotFoundException();
        }

        return new ArticleWithContent(article);
    }

    @GetMapping("/next")
    public ResponseEntity<ArticleDTO> getNextArticle(@RequestParam UUID id) {
        ArticleDTO article = articleService.getNextArticle(id);
        return ResponseEntity.ok().body(article);
    }

    @GetMapping("/prev")
    public ResponseEntity<ArticleDTO> getPreviousArticle(@RequestParam UUID id) {
        ArticleDTO article = articleService.getPreviousArticle(id);
        return ResponseEntity.ok().body(article);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> publishArticle(@RequestBody ArticleUploadRequest article, @RequestParam String action, Authentication authentication) {
        User userFromToken = userService.getUserFromAuthentication(authentication);
        articleEditorService.uploadArticle(userFromToken, article, action);
        return ResponseEntity.ok().body(ARTICLE_UPLOADED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable UUID id, Authentication authentication) {
        Article article = articleService.getArticleById(id);

        User currentUser = userService.getUserFromAuthentication(authentication);
        if (articleService.canUserEditArticle(currentUser, article)) {
            articleService.deleteArticleById(id);
            return ResponseEntity.ok().body(ARTICLE_DELETED.formatted(id));
        } else {
            throw new ArticleNotFoundException();
        }
    }
}
