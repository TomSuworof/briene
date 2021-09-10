package com.salat.briene.controllers;

import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.ArticleDTOHTML;
import com.salat.briene.payload.response.UserDTO;
import com.salat.briene.exceptions.IllegalArticleStateException;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream().map(UserDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/edit_user")
    public ResponseEntity<String> changeRole(@RequestParam String action, @RequestParam Long id) throws UserNotFoundException {
        switch (action) {
            case "delete" -> userService.changeRole(id, "blocked");
            case "make_admin" -> userService.changeRole(id, "admin");
            case "make_user" -> userService.changeRole(id, "user");
            default -> throw new UserNotFoundException();
        }
        return ResponseEntity.ok().body("Role of " + id + " changed");
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getAllArticles(@RequestParam String state) throws IllegalArticleStateException {
        List<ArticleDTO> articles = articleService.getArticlesByState(state)
                .stream().map(ArticleDTOHTML::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(articles);
    }
}
