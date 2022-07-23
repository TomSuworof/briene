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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final ArticleService articleService;
    private final UserService userService;
    private final AuthorService authorService;

    @GetMapping("/{authorName}")
    public ResponseEntity<AuthorDTO> getAuthorPage(@PathVariable String authorName, @RequestParam Integer limit, @RequestParam Integer offset) {
        User userAuthor = userService.loadUserByUsername(authorName);
        PageResponseDTO<ArticleDTO> articles = articleService.getPageWithArticlesByAuthorAndStatePaginated(userAuthor, ArticleState.PUBLISHED, limit, offset);

        AuthorDTO author = new AuthorDTO(userAuthor, articles);

        if (!author.getArticles().isHasBefore() && !author.getArticles().isHasAfter()) {
            return ResponseEntity.status(HttpStatus.OK).body(author);
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(author);
        }
    }

    @PostMapping("/subscribe")
    @ResponseBody
    public void subscribeToAuthor(@RequestParam String author, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        User authorUser = userService.loadUserByUsername(author);

        authorService.subscribe(currentUser, authorUser);
    }

    @PostMapping("/unsubscribe")
    @ResponseBody
    public void unsubscribeFromAuthor(@RequestParam String author, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        User authorUser = userService.loadUserByUsername(author);

        authorService.unsubscribe(currentUser, authorUser);
    }

    @PostMapping("/isFollowing")
    public ResponseEntity<Boolean> isFollowing(@RequestParam String author, Authentication authentication) {
        User currentUser = userService.getUserFromAuthentication(authentication);
        User authorUser = userService.loadUserByUsername(author);

        Boolean isFollowing = authorService.isFollowing(currentUser, authorUser);
        return ResponseEntity.ok().body(isFollowing);
    }
}
