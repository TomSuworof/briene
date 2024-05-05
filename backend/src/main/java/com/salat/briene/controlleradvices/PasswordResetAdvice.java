// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.controlleradvices;

import com.salat.briene.exceptions.PasswordResetRequestInvalidException;
import com.salat.briene.exceptions.PasswordResetRequestNotFoundException;
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
public class PasswordResetAdvice {
    @ExceptionHandler(PasswordResetRequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePasswordResetRequestNotFound(PasswordResetRequestNotFoundException e, HttpServletRequest request) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(PasswordResetRequestInvalidException.class)
    public ResponseEntity<ErrorResponse> handlePasswordResetRequestInvalid(PasswordResetRequestInvalidException e, HttpServletRequest request) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }
}
