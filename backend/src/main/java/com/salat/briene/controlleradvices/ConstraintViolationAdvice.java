package com.salat.briene.controlleradvices;

import com.salat.briene.payload.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ConstraintViolationAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ConstraintViolationAdvice.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e, HttpServletRequest request) {
        logger.error("Illegal entity format: {}", e.getMessage());

        Set<String> violations = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), violations.toString(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }
}
