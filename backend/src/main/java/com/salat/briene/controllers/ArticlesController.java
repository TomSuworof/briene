package com.salat.briene.controllers;

import com.salat.briene.exceptions.*;
import com.salat.briene.payload.request.ArticleUploadRequest;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.ArticleDTOHTML;
import com.salat.briene.payload.response.ArticleDTORaw;
import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.services.ArticleEditorService;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticlesController {
    private static final String ARTICLE_UPLOADED = "Article was published or saved";
    private static final String ARTICLE_DELETED = "Article {%s} was deleted";

    private final ArticleService articleService;
    private final ArticleEditorService articleEditorService;
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<PageResponseDTO> getArticlesPaginated(@RequestParam Integer limit, @RequestParam Integer offset) {
        PageResponseDTO response = articleService.getPageWithArticlesByState(ArticleState.PUBLISHED, limit, offset);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    @GetMapping("/my_articles")
    public ResponseEntity<List<ArticleDTO>> getMyArticles(@RequestParam String state, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);

        List<ArticleDTO> articles = articleService.getArticlesByAuthorAndState(currentUser, ArticleState.getFromDescription(state))
                .stream().map(ArticleDTOHTML::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticle(
            @RequestParam(required = false) Boolean raw,
            @PathVariable UUID id,
            Authentication authentication) {
        ArticleDTO article = null;

        if (raw == null) {
            article = this.getArticleHTML(id, authentication);
        } else if (raw) {
            article = this.getArticleRaw(id, authentication);
        }

        return ResponseEntity.ok().body(article);
    }

    private ArticleDTO getArticleHTML(UUID id, Authentication authentication) {
        Article article = articleService.getArticleById(id);

        if (article.getState().equals(ArticleState.IN_EDITING)) {
            User userFromToken = userService.getUserFromAuthentication(authentication);

            if (!articleService.canUserEditArticle(userFromToken, article)) {
                throw new ArticleNotFoundException();
            }
        }

        return new ArticleDTOHTML(article);
    }

    private ArticleDTO getArticleRaw(UUID id, Authentication authentication) {
        Article article = articleService.getArticleById(id);

        User currentUser = userService.getUserFromAuthentication(authentication);

        if (!articleService.canUserEditArticle(currentUser, article)) {
            throw new ArticleNotFoundException();
        }

        return new ArticleDTORaw(article);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> publishArticle(
            @RequestBody ArticleUploadRequest article,
            @RequestParam String action,
            Authentication authentication) {
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
