package com.salat.briene.controllers;

import com.salat.briene.entities.ArticleState;
import com.salat.briene.entities.RoleEnum;
import com.salat.briene.payload.response.ArticleDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.payload.response.UserDTO;
import com.salat.briene.services.ArticleService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Log4j2
@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/users")
    public @ResponseBody ResponseEntity<PageResponseDTO<UserDTO>> getUsersPaginated(
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getUsersPaginated() called. Limit: {}, offset: {}", limit, offset);
        PageResponseDTO<UserDTO> response = userService.getUsersPaginated(limit, offset);
        log.trace("getUsersPaginated(). Response to send: {}", () -> response);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            log.trace("getUsersPaginated(). Response contains all users. Response status is OK");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            log.trace("getUsersPaginated(). Response does not contain all users. Response status is PARTIAL_CONTENT");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    @PostMapping("/edit_user")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<UserDTO> changeRole(@RequestParam String action, @RequestParam UUID id) {
        log.debug("changeRole() called. Action: {}, ID: {}", action, id);
        UserDTO user = userService.changeRole(id, RoleEnum.getFromAction(action).getAsObject());
        log.trace("changeRole(). Result user: {}", () -> user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/articles")
    public @ResponseBody ResponseEntity<PageResponseDTO<ArticleDTO>> getArticlesPaginated(
            @RequestParam(required = false, defaultValue = "all") String state,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getExercisePaginated() called. State: {}, limit: {}, offset: {}", state, limit, offset);
        PageResponseDTO<ArticleDTO> response = articleService.getPageWithArticlesByState(ArticleState.getFromDescription(state), limit, offset);
        log.trace("getExercisePaginated(). Response to send: {}", () -> response);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            log.trace("getExercisesPaginated(). Response contains all exercises. Response status is OK");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            log.trace("getExercisesPaginated(). Response does not contain all exercises. Response status is PARTIAL_CONTENT");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }
}
