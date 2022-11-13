package com.salat.briene.controllers;

import com.salat.briene.entities.User;
import com.salat.briene.payload.request.CommentUploadRequest;
import com.salat.briene.payload.response.CommentDTO;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.services.CommentService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/get")
    public ResponseEntity<PageResponseDTO<CommentDTO>> getCommentsPaginated(@RequestParam String articleUrl, @RequestParam Integer limit, @RequestParam Integer offset) {
        PageResponseDTO<CommentDTO> response = commentService.getPageWithCommentsByArticleUrl(articleUrl, limit, offset);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }


    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentDTO> uploadComment(@RequestBody CommentUploadRequest commentUploadRequest, Authentication authentication) throws EmailException {
        User user = userService.getUserFromAuthentication(authentication);
        CommentDTO comment = commentService.uploadComment(user, commentUploadRequest);

        return ResponseEntity.ok().body(comment);
    }
}
