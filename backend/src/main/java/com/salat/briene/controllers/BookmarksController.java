package com.salat.briene.controllers;

import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.ArticleDTOHTML;
import com.salat.briene.entities.Article;
import com.salat.briene.entities.User;
import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarksController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getBookmarksOfUser(Authentication authentication) {
        try {
            User currentUser = userService.getUserFromAuthentication(authentication);

            List<ArticleDTO> bookmarks = currentUser.getBookmarkedArticles()
                    .stream().map(ArticleDTOHTML::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(bookmarks);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/isIn")
    public ResponseEntity<?> isArticleInBookmarksOfUser(@RequestParam Long id, Authentication authentication) {
        try {
            User currentUser = userService.getUserFromAuthentication(authentication);

            Article article = articleService.getArticleById(id);

            return ResponseEntity.ok().body(currentUser.getBookmarkedArticles().contains(article));
        } catch (ArticleNotFoundException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editBookmark(@RequestParam Long id, @RequestParam String action, Authentication authentication) {
        try {
            User currentUser = userService.getUserFromAuthentication(authentication);
            Set<Article> bookmarks = currentUser.getBookmarkedArticles();

            Article article = articleService.getArticleById(id);

            switch (action.toLowerCase()) {
                case "add" -> bookmarks.add(article);
                case "remove" -> bookmarks.remove(article);
                default -> throw new ArticleNotFoundException();
            }

            userService.updateUser(currentUser.getId(), new HashMap<>(){{
                put("bookmarks", bookmarks);
            }});

            return ResponseEntity.ok().body("Bookmarks updated");
        } catch (ArticleNotFoundException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
