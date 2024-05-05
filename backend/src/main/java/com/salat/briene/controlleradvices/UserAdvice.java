// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.controlleradvices;

import com.salat.briene.exceptions.AnonymousUserException;
import com.salat.briene.exceptions.DuplicatedUserException;
import com.salat.briene.exceptions.RoleNotFoundException;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.payload.response.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Log4j2
@ControllerAdvice
public class UserAdvice {
    @ExceptionHandler(AnonymousUserException.class)
    public ResponseEntity<ErrorResponse> handleAnonymousUser(AnonymousUserException e, HttpServletRequest request) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedUser(DuplicatedUserException e, HttpServletRequest request) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
        // 422 Unprocessable Entity response status code indicates that the server understands the content type of the
        // request entity, and the syntax of the request entity is correct, but it was unable to process the contained
        // instructions, because content is already exists.
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e, HttpServletRequest request) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFound(RoleNotFoundException e, HttpServletRequest request) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }
}
