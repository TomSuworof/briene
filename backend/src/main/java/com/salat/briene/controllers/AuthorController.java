package com.salat.briene.controllers;

import com.salat.briene.entities.ArticleState;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.AuthorDTO;
import com.salat.briene.entities.User;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{authorName}")
    public ResponseEntity<AuthorDTO> getAuthorPage(@PathVariable String authorName, @RequestParam Integer limit, @RequestParam Integer offset) {
        User userAuthor = userService.loadUserByUsername(authorName);
        PageResponseDTO<ArticleDTO> articles = articleService.getPageWithArticlesByAuthorAndStatePaginated(userAuthor, ArticleState.PUBLISHED, limit, offset);

        AuthorDTO author = new AuthorDTO(userAuthor, articles);
        return ResponseEntity.ok().body(author);
    }
}
