// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.controllers;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.payload.request.ArticleUploadRequest;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.ArticleWithContent;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.services.ArticleEditorService;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@Controller
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticlesController {
    private final ArticleService articleService;
    private final ArticleEditorService articleEditorService;
    private final UserService userService;

    @GetMapping("/get")
    public @ResponseBody ResponseEntity<PageResponseDTO<ArticleDTO>> getArticlesPaginated(
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getArticlesPaginated() called. Limit: {}, offset: {}", limit, offset);
        PageResponseDTO<ArticleDTO> response = articleService.getPageWithArticlesByState(ArticleState.PUBLISHED, limit, offset);
        log.trace("getArticlesPaginated(). Response to send: {}", () -> response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/my_articles")
    public @ResponseBody ResponseEntity<PageResponseDTO<ArticleDTO>> getMyArticlesPaginated(
            @RequestParam(required = false, defaultValue = "all") String state,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            Authentication authentication
    ) {
        log.debug("getMyArticlesPaginated() called. State: {}, limit: {}, offset: {}, auth: {}", () -> state, () -> limit, () -> offset, () -> authentication);
        User currentUser = userService.getUserFromAuthentication(authentication);
        log.trace("getMyArticlesPaginated(). Current user: {}", () -> currentUser);

        PageResponseDTO<ArticleDTO> response = articleService.getPageWithArticlesByAuthorAndStatePaginated(currentUser, ArticleState.getFromDescription(state), limit, offset);
        log.trace("getMyArticlesPaginated(). Response status: {}", () -> response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{url}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<ArticleWithContent> getArticle(@PathVariable String url, Authentication authentication) {
        log.debug("getArticle() called. Url: {}, auth: {}", () -> url, () -> authentication);
        Article article = articleService.getArticleByUrl(url);
        log.trace("getArticle(). Found article: {}", () -> article);

        if (article.getState().equals(ArticleState.IN_EDITING)) {
            log.trace("getArticle(). Article is draft. Validating user rights to see it");
            User userFromToken = userService.getUserFromAuthentication(authentication);
            log.trace("getArticle(). User from auth: {}", () -> userFromToken);

            if (!articleService.canUserEditArticle(userFromToken, article)) {
                log.trace("getArticle(). User cannot see article. User: {}, article: {}", () -> userFromToken, () -> article);
                throw new ArticleNotFoundException();
            }
        }

        return ResponseEntity.ok().body(new ArticleWithContent(article));
    }

    @GetMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<ArticleWithContent> getArticleForEdit(@PathVariable UUID id, Authentication authentication) {
        log.debug("getArticleForEdit() called. ID: {}, auth: {}", () -> id, () -> authentication);
        Article article = articleService.getArticleById(id);
        log.trace("getArticleForEdit(). Article: {}", () -> article);

        User currentUser = userService.getUserFromAuthentication(authentication);
        log.trace("getArticleForEdit(). Current user: {}", () -> currentUser);

        if (!articleService.canUserEditArticle(currentUser, article)) {
            log.trace("getArticleForEdit(). Current user cannot edit article. User: {}, article: {}", () -> currentUser, () -> article);
            throw new ArticleNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ArticleWithContent(article));
    }

    @GetMapping("/share/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<ArticleWithContent> getArticle(@PathVariable UUID id) {
        log.debug("getArticle() called. ID: {}", id);
        ArticleWithContent article = new ArticleWithContent(articleService.getArticleById(id));
        log.trace("getArticle(). Article: {}", () -> article);
        return ResponseEntity.ok().body(article);
    }

    @GetMapping("/next")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<ArticleDTO> getNextArticle(@RequestParam String url) {
        log.debug("getNextArticle() called. Url: {}", () -> url);
        ArticleDTO article = articleService.getNextArticle(url);
        log.trace("getNext(). Article: {}", () -> article);
        return ResponseEntity.ok().body(article);
    }

    @GetMapping("/prev")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<ArticleDTO> getPreviousArticle(@RequestParam String url) {
        log.debug("getPreviousArticle() called. Url: {}", () -> url);
        ArticleDTO article = articleService.getPreviousArticle(url);
        log.trace("getPreviousArticle(). Article: {}", () -> article);
        return ResponseEntity.ok().body(article);
    }

    @GetMapping("/suggested")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<ArticleDTO>> getSuggestedArticles(@RequestParam String url) {
        log.debug("getSuggestedArticles() called. Url: {}", () -> url);
        List<ArticleDTO> articles = articleService.getSuggestedArticles(url);
        log.trace("getSuggestedArticles(). Articles: {}", () -> articles);
        return ResponseEntity.ok().body(articles);
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<ArticleDTO> publishArticle(@RequestBody ArticleUploadRequest article, @RequestParam String action, Authentication authentication) {
        log.debug("publishArticle() called. Action: {}, article: {}, auth: {}", () -> action, () -> article, () -> authentication);
        User userFromToken = userService.getUserFromAuthentication(authentication);
        log.trace("publishArticle(). User from token: {}", () -> userFromToken);
        ArticleDTO articleDTO = articleEditorService.uploadArticle(userFromToken, article, action);
        log.trace("publishArticle(). Article: {}", () -> articleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(articleDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteArticle(@PathVariable UUID id, Authentication authentication) {
        log.debug("deleteArticle() called. ID: {}, auth: {}", () -> id, () -> authentication);
        Article article = articleService.getArticleById(id);
        log.trace("deleteArticle(). Found article: {}", () -> article);

        User currentUser = userService.getUserFromAuthentication(authentication);
        log.trace("deleteArticle(). Current user: {}", () -> currentUser);

        if (articleService.canUserEditArticle(currentUser, article)) {
            log.trace("deleteArticle(). This user can delete article. User: {}, article: {}", () -> currentUser, () -> article);
            articleService.deleteArticleById(id);
        } else {
            log.trace("deleteArticle(). This user can not delete article. User: {}, article: {}", () -> currentUser, () -> article);
            throw new ArticleNotFoundException();
        }
    }
}
