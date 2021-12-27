package com.salat.briene.controlleradvices;

import com.salat.briene.exceptions.PasswordResetRequestInvalidException;
import com.salat.briene.exceptions.PasswordResetRequestNotFoundException;
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
public class PasswordResetAdvice {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetAdvice.class);

    @ExceptionHandler(PasswordResetRequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePasswordResetRequestNotFound(PasswordResetRequestNotFoundException e, HttpServletRequest request) {
        logger.error(e.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(PasswordResetRequestInvalidException.class)
    public ResponseEntity<ErrorResponse> handlePasswordResetRequestInvalid(PasswordResetRequestInvalidException e, HttpServletRequest request) {
        logger.error(e.getMessage());

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }
}
