package com.salat.briene.controlleradvices;

import com.salat.briene.exceptions.AnonymousUserException;
import com.salat.briene.exceptions.DuplicatedUserException;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.payload.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class UserAdvice {
    private static final Logger logger = LoggerFactory.getLogger(UserAdvice.class);

    @ExceptionHandler(AnonymousUserException.class)
    public ResponseEntity<?> handleAnonymousUser(AnonymousUserException e, HttpServletRequest request) {
        logger.error("Anonymous user error: {}", e.getMessage());

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<?> handleDuplicatedUser(DuplicatedUserException e, HttpServletRequest request) {
        logger.error("Duplicated user error: {}", e.getMessage());

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
        // 422 Unprocessable Entity response status code indicates that the server understands the content type of the
        // request entity, and the syntax of the request entity is correct, but it was unable to process the contained
        // instructions, because content is already exists.
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException e, HttpServletRequest request) {
        logger.error("User not found error: {}", e.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }
}
