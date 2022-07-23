package com.salat.briene.controllers;

import com.salat.briene.entities.User;
import com.salat.briene.payload.request.CommentUploadRequest;
import com.salat.briene.payload.response.CommentDTO;
import com.salat.briene.services.CommentService;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {
    private final UserService userService;
    private final CommentService commentService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentDTO> uploadComment(@RequestBody CommentUploadRequest commentUploadRequest, Authentication authentication) throws EmailException {
        User user = userService.getUserFromAuthentication(authentication);
        CommentDTO comment = commentService.uploadComment(user, commentUploadRequest);

        return ResponseEntity.ok().body(comment);
    }
}
