package com.salat.briene.controllers;

import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.User;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.AuthorDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.AuthorService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@Controller
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final ArticleService articleService;
    private final UserService userService;
    private final AuthorService authorService;

    @GetMapping("/{authorName}")
    public @ResponseBody ResponseEntity<AuthorDTO> getAuthorPage(
            @PathVariable String authorName,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getAuthorPage() called. Author name: {}, limit: {}, offset: {}", authorName, limit, offset);
        User userAuthor = userService.loadUserByUsername(authorName);
        log.trace("getAuthorPage(). User: {}", () -> userAuthor);

        PageResponseDTO<ArticleDTO> articles = articleService.getPageWithArticlesByAuthorAndStatePaginated(userAuthor, ArticleState.PUBLISHED, limit, offset);
        AuthorDTO author = new AuthorDTO(userAuthor, articles);
        log.trace("getAuthorPage(). Response to send: {}", () -> author);

        if (!author.getArticles().isHasBefore() && !author.getArticles().isHasAfter()) {
            log.trace("getArticlesPaginated(). Response contains all articles. Response status is OK");
            return ResponseEntity.status(HttpStatus.OK).body(author);
        } else {
            log.trace("getArticlesPaginated(). Response does not contain all articles. Response status is PARTIAL_CONTENT");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(author);
        }
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void subscribeToAuthor(@RequestParam String author, Authentication authentication) {
        log.debug("subscribeToAuthor() called. Author name: {}, auth: {}", () -> author, () -> authentication);
        User currentUser = userService.getUserFromAuthentication(authentication);
        User authorUser = userService.loadUserByUsername(author);
        log.trace("subscribeToAuthor(). Current user: {}, author: {}", () -> currentUser, () -> authorUser);

        authorService.subscribe(currentUser, authorUser);
    }

    @PostMapping("/unsubscribe")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void unsubscribeFromAuthor(@RequestParam String author, Authentication authentication) {
        log.debug("unsubscribeFromAuthor() called. Author name: {}, auth: {}", () -> author, () -> authentication);
        User currentUser = userService.getUserFromAuthentication(authentication);
        User authorUser = userService.loadUserByUsername(author);
        log.trace("unsubscribeFromAuthor(). Current user: {}, author: {}", () -> currentUser, () -> authorUser);

        authorService.unsubscribe(currentUser, authorUser);
    }

    @GetMapping("/isFollowing")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Boolean> isFollowing(@RequestParam String authorName, Authentication authentication) {
        log.debug("isFollowing() called. Author: {}, auth: {}", () -> authorName, () -> authentication);
        User currentUser = userService.getUserFromAuthentication(authentication);
        User authorUser = userService.loadUserByUsername(authorName);
        log.trace("isFollowing(). Current user: {}, author: {}", () -> currentUser, () -> authorUser);

        return ResponseEntity.ok().body(currentUser.getFollowings().contains(authorUser));
    }
}
