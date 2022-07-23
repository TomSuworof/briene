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
import org.apache.commons.mail.EmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticlesController {
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

    @GetMapping("/{url}")
    public ResponseEntity<ArticleWithContent> getArticle(@PathVariable String url, Authentication authentication) {
        Article article = articleService.getArticleByUrl(url);

        if (article.getState().equals(ArticleState.IN_EDITING)) {
            User userFromToken = userService.getUserFromAuthentication(authentication);

            if (!articleService.canUserEditArticle(userFromToken, article)) {
                throw new ArticleNotFoundException();
            }
        }

        return ResponseEntity.ok().body(new ArticleWithContent(article));
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<ArticleWithContent> getArticleForEdit(@PathVariable UUID id, Authentication authentication) {
        Article article = articleService.getArticleById(id);

        User currentUser = userService.getUserFromAuthentication(authentication);

        if (!articleService.canUserEditArticle(currentUser, article)) {
            throw new ArticleNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ArticleWithContent(article));
    }

    @GetMapping("/share/{id}")
    public ResponseEntity<ArticleWithContent> getArticle(@PathVariable UUID id) {
        ArticleWithContent article = new ArticleWithContent(articleService.getArticleById(id));
        return ResponseEntity.ok().body(article);
    }

    @GetMapping("/next")
    public ResponseEntity<ArticleDTO> getNextArticle(@RequestParam String url) {
        ArticleDTO article = articleService.getNextArticle(url);
        return ResponseEntity.ok().body(article);
    }

    @GetMapping("/prev")
    public ResponseEntity<ArticleDTO> getPreviousArticle(@RequestParam String url) {
        ArticleDTO article = articleService.getPreviousArticle(url);
        return ResponseEntity.ok().body(article);
    }

    @GetMapping("/suggested")
    public ResponseEntity<List<ArticleDTO>> getSuggestedArticles(@RequestParam String url) {
        List<ArticleDTO> articles = articleService.getSuggestedArticles(url);
        return ResponseEntity.ok().body(articles);
    }

    @PostMapping("/upload")
    public ResponseEntity<ArticleDTO> publishArticle(@RequestBody ArticleUploadRequest article, @RequestParam String action, Authentication authentication) throws EmailException {
        User userFromToken = userService.getUserFromAuthentication(authentication);
        ArticleDTO articleDTO = articleEditorService.uploadArticle(userFromToken, article, action);
        return ResponseEntity.ok().body(articleDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteArticle(@PathVariable UUID id, Authentication authentication) {
        Article article = articleService.getArticleById(id);

        User currentUser = userService.getUserFromAuthentication(authentication);
        if (articleService.canUserEditArticle(currentUser, article)) {
            articleService.deleteArticleById(id);
        } else {
            throw new ArticleNotFoundException();
        }
    }
}
