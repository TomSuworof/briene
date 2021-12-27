package com.salat.briene.controllers;

import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.RoleEnum;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.ArticleWithContentHTML;
import com.salat.briene.payload.response.UserDTO;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "*")
@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private static final String ROLE_UPDATED = "Role of {%s} changed";

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
    public ResponseEntity<String> changeRole(@RequestParam String action, @RequestParam UUID id) throws EmailException {
        userService.changeRole(id, RoleEnum.getFromAction(action).getAsObject());
        return ResponseEntity.ok().body(ROLE_UPDATED.formatted(id));
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getAllArticles(@RequestParam String state) {
        List<ArticleDTO> articles = articleService.getArticlesByState(ArticleState.getFromDescription(state))
                .stream().map(ArticleWithContentHTML::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(articles);
    }
}
