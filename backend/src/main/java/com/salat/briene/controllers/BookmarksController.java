package com.salat.briene.controllers;

import com.salat.briene.payload.request.UserDataRequest;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.ArticleDTOHTML;
import com.salat.briene.entities.Article;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarksController {
    private static final String BOOKMARKS_ACTION_ADD = "add";
    private static final String BOOKMARKS_ACTION_REMOVE = "remove";

    private static final String BOOKMARKS_UPDATED = "Bookmarks updated";

    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<List<ArticleDTO>> getBookmarksOfUser(Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);

        List<ArticleDTO> bookmarks = currentUser.getBookmarkedArticles()
                .stream().map(ArticleDTOHTML::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(bookmarks);
    }

    @GetMapping("/isIn")
    public ResponseEntity<Boolean> isArticleInBookmarksOfUser(@RequestParam UUID id, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        Article article = articleService.getArticleById(id);
        return ResponseEntity.ok().body(currentUser.getBookmarkedArticles().contains(article));
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editBookmark(@RequestParam UUID id, @RequestParam String action, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        Set<Article> bookmarks = currentUser.getBookmarkedArticles();

        Article article = articleService.getArticleById(id);

        switch (action.toLowerCase()) {
            case BOOKMARKS_ACTION_ADD -> bookmarks.add(article);
            case BOOKMARKS_ACTION_REMOVE -> bookmarks.remove(article);
            default -> throw new ArticleNotFoundException();
        }

        UserDataRequest newUserData = new UserDataRequest();
        newUserData.setBookmarks(Optional.ofNullable(bookmarks));

        userService.updateUser(currentUser.getId(), newUserData);

        return ResponseEntity.ok().body(BOOKMARKS_UPDATED);
    }
}
