package com.salat.briene.controllers;

import com.salat.briene.payload.request.UserDataRequest;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.entities.Article;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarksController {
    private static final String BOOKMARKS_ACTION_ADD = "add";
    private static final String BOOKMARKS_ACTION_REMOVE = "remove";

    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ArticleDTO>> getBookmarksOfUser(Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);

        List<ArticleDTO> bookmarks = currentUser.getBookmarkedArticles()
                .stream().map(ArticleDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(bookmarks);
    }

    @GetMapping("/isIn")
    public ResponseEntity<Boolean> isArticleInBookmarksOfUser(@RequestParam String url, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        Article article = articleService.getArticleByUrl(url);
        return ResponseEntity.ok().body(currentUser.getBookmarkedArticles().contains(article));
    }

    @PostMapping("/edit")
    @ResponseBody
    public void editBookmark(@RequestParam String url, @RequestParam String action, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        Set<Article> bookmarks = currentUser.getBookmarkedArticles();

        Article article = articleService.getArticleByUrl(url);

        switch (action.toLowerCase()) {
            case BOOKMARKS_ACTION_ADD -> bookmarks.add(article);
            case BOOKMARKS_ACTION_REMOVE -> bookmarks.remove(article);
            default -> throw new ArticleNotFoundException();
        }

        UserDataRequest newUserData = new UserDataRequest();
        newUserData.setBookmarks(Optional.of(bookmarks));

        userService.updateUser(currentUser.getId(), newUserData);
    }
}
