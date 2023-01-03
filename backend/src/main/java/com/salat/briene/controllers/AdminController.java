package com.salat.briene.controllers;

import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.RoleEnum;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.payload.response.UserDTO;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

//@CrossOrigin(origins = "*")
@Controller
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<PageResponseDTO<UserDTO>> getUsersPaginated(@RequestParam Integer limit, @RequestParam Integer offset) {
        PageResponseDTO<UserDTO> response = userService.getUsersPaginated(limit, offset);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    @PostMapping("/edit_user")
    public ResponseEntity<UserDTO> changeRole(@RequestParam String action, @RequestParam UUID id) {
        UserDTO user = userService.changeRole(id, RoleEnum.getFromAction(action).getAsObject());
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/articles")
    public ResponseEntity<PageResponseDTO<ArticleDTO>> getArticlesPaginated(@RequestParam String state, @RequestParam Integer limit, @RequestParam Integer offset) {
        PageResponseDTO<ArticleDTO> response = articleService.getPageWithArticlesByState(ArticleState.getFromDescription(state), limit, offset);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }
}
