package com.salat.briene.controlleradvices;

import com.salat.briene.exceptions.AnonymousUserException;
import com.salat.briene.exceptions.DuplicatedUserException;
import com.salat.briene.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserAdvice {
    private static final Logger logger = LoggerFactory.getLogger(UserAdvice.class);

    @ExceptionHandler(AnonymousUserException.class)
    public ResponseEntity<?> handleAnonymousUser(AnonymousUserException e) {
        logger.error("Anonymous user error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<?> handleDuplicatedUser(DuplicatedUserException e) {
        logger.error("Duplicated user error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        // 422 Unprocessable Entity response status code indicates that the server understands the content type of the
        // request entity, and the syntax of the request entity is correct, but it was unable to process the contained
        // instructions, because content is already exists.
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
        logger.error("User not found error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
