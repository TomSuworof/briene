package com.salat.briene.controlleradvices;

import com.salat.briene.exceptions.AnonymousUserException;
import com.salat.briene.exceptions.DuplicatedUserException;
import com.salat.briene.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserAdvice {
    @ExceptionHandler(AnonymousUserException.class)
    public ResponseEntity<?> handleAnonymousUser(AnonymousUserException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<?> handleDuplicatedUser(DuplicatedUserException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.SEE_OTHER).body(e.getMessage());
        // According to RFC 7231, a 303 See Other MAY be used If the result of processing a POST would be equivalent to
        // a representation of an existing resource.
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
