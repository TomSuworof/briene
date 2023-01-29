package com.salat.briene.controllers;

import com.salat.briene.entities.User;
import com.salat.briene.payload.request.CommentUploadRequest;
import com.salat.briene.payload.response.CommentDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.services.CommentService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@Controller
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/get")
    public @ResponseBody ResponseEntity<PageResponseDTO<CommentDTO>> getCommentsPaginated(
            @RequestParam String articleUrl,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getCommentsPaginated() called. Article url: {}, limit: {}, offset: {}", () -> articleUrl, () -> limit, () -> offset);
        PageResponseDTO<CommentDTO> response = commentService.getPageWithCommentsByArticleUrl(articleUrl, limit, offset);
        log.trace("getCommentPaginated(). Response to send: {}", () -> response);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            log.trace("getArticlesPaginated(). Response contains all articles. Response status is OK");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            log.trace("getArticlesPaginated(). Response does not contain all articles. Response status is PARTIAL_CONTENT");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }


    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody ResponseEntity<CommentDTO> uploadComment(@RequestBody CommentUploadRequest commentUploadRequest, Authentication authentication) {
        log.debug("uploadComment() called. Comment upload request: {}, auth: {}", () -> commentUploadRequest, () -> authentication);
        User user = userService.getUserFromAuthentication(authentication);
        log.trace("uploadComment(). User: {}", () -> user);

        CommentDTO comment = commentService.uploadComment(user, commentUploadRequest);
        log.trace("uploadComment(). Comment: {}", () -> comment);

        return ResponseEntity.ok().body(comment);
    }
}
