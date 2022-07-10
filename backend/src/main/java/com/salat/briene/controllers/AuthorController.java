package com.salat.briene.controllers;

import com.salat.briene.entities.ArticleState;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.AuthorDTO;
import com.salat.briene.entities.User;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.AuthorService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:8081")
//@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final ArticleService articleService;
    private final UserService userService;
    private final AuthorService authorService;

    private static final String SUBSCRIBED_MESSAGE = "Subscribed";
    private static final String UNSUBSCRIBED_MESSAGE = "Unsubscribed";

    @GetMapping("/{authorName}")
    public ResponseEntity<AuthorDTO> getAuthorPage(@PathVariable String authorName, @RequestParam Integer limit, @RequestParam Integer offset) {
        User userAuthor = userService.loadUserByUsername(authorName);
        PageResponseDTO<ArticleDTO> articles = articleService.getPageWithArticlesByAuthorAndStatePaginated(userAuthor, ArticleState.PUBLISHED, limit, offset);

        AuthorDTO author = new AuthorDTO(userAuthor, articles);
        return ResponseEntity.ok().body(author);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToAuthor(@RequestParam String author, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        User authorUser = userService.loadUserByUsername(author);

        authorService.subscribe(currentUser, authorUser);

        return ResponseEntity.ok().body(SUBSCRIBED_MESSAGE);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribeFromAuthor(@RequestParam String author, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        User authorUser = userService.loadUserByUsername(author);

        authorService.unsubscribe(currentUser, authorUser);

        return ResponseEntity.ok().body(UNSUBSCRIBED_MESSAGE);
    }

    @PostMapping("/isFollowing")
    public ResponseEntity<Boolean> isFollowing(@RequestParam String author, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        User authorUser = userService.loadUserByUsername(author);

        Boolean isFollowing = authorService.isFollowing(currentUser, authorUser);

        return ResponseEntity.ok().body(isFollowing);
    }
}
