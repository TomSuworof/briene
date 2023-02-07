package com.salat.briene.controllers;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.payload.request.UserDataRequest;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarksController {
    private static final String BOOKMARKS_ACTION_ADD = "add";
    private static final String BOOKMARKS_ACTION_REMOVE = "remove";

    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<List<ArticleDTO>> getBookmarksOfUser(Authentication authentication) {
        log.debug("getBookmarksOfUser() called. Auth: {}", () -> authentication);
        User currentUser = userService.getUserFromAuthentication(authentication);
        log.trace("getBookmarksOfUser(). Current user: {}", () -> currentUser);

        List<ArticleDTO> bookmarks = currentUser.getBookmarkedArticles()
                .stream().map(ArticleDTO::new)
                .collect(Collectors.toList());
        log.trace("getBookmarksOfUser(). Bookmarks count: {}", bookmarks::size);

        return ResponseEntity.ok().body(bookmarks);
    }

    @GetMapping("/isIn")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Boolean> isArticleInBookmarksOfUser(@RequestParam String url, Authentication authentication) {
        log.debug("isArticleInBookmarksOfUser() called. Url: {}", () -> url);
        User currentUser = userService.getUserFromAuthentication(authentication);
        log.trace("isArticleInBookmarksOfUser(). Current user: {}", () -> currentUser);

        Article article = articleService.getArticleByUrl(url);
        log.trace("isArticleInBookmarksOfUser(). Article: {}", () -> article);

        return ResponseEntity.ok().body(currentUser.getBookmarkedArticles().contains(article));
    }

    @PostMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void editBookmark(@RequestParam String url, @RequestParam String action, Authentication authentication) {
        log.debug("editBookmark() called. Url: {}, action: {}, auth: {}", () -> url, () -> action, () -> authentication);
        User currentUser = userService.getUserFromAuthentication(authentication);
        log.trace("editBookmarks(). Current user: {}", () -> currentUser);

        Set<Article> bookmarks = currentUser.getBookmarkedArticles();
        log.trace("editBookmarks(). Bookmarks count: {}", bookmarks::size);

        Article article = articleService.getArticleByUrl(url);
        log.trace("editBookmarks(). Article with the url: {}", () -> article);

        if (action.equalsIgnoreCase(BOOKMARKS_ACTION_ADD)) {
            bookmarks.add(article);
        } else if (action.equalsIgnoreCase(BOOKMARKS_ACTION_REMOVE)) {
            bookmarks.remove(article);
        } else {
            throw new ArticleNotFoundException();
        }

        UserDataRequest newUserData = new UserDataRequest();
        newUserData.setBookmarks(Optional.of(bookmarks));
        log.trace("editBookmarks(). New user data: {}", () -> newUserData);
        userService.updateUser(currentUser.getId(), newUserData);
    }
}
