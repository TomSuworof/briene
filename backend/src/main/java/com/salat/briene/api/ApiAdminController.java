package com.salat.briene.api;

import com.salat.briene.payload.response.ArticleContainer;
import com.salat.briene.payload.response.ArticleContainerHTML;
import com.salat.briene.payload.response.UserContainer;
import com.salat.briene.exceptions.IllegalArticleStateException;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ApiAdminController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<UserContainer> users = userService.getAllUsers()
                .stream().map(UserContainer::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/edit_user")
    public ResponseEntity<?> changeRole(@RequestParam String action, @RequestParam Long id) {
        try {
            switch (action) {
                case "delete" -> userService.changeRole(id, "blocked");
                case "make_admin" -> userService.changeRole(id, "admin");
                case "make_user" -> userService.changeRole(id, "user");
                default -> throw new UserNotFoundException();
            }
            return ResponseEntity.ok().body("Role of " + id + " changed");
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/articles")
    public ResponseEntity<?> getAllArticles(@RequestParam String state) {
        try {
            List<ArticleContainer> articles = articleService.getArticlesByState(state)
                    .stream().map(ArticleContainerHTML::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(articles);
        } catch (IllegalArticleStateException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
