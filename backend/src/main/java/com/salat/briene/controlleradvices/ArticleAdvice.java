package com.salat.briene.controlleradvices;

import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.DuplicatedArticleException;
import com.salat.briene.exceptions.IllegalArticleStateException;
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
public class ArticleAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ArticleAdvice.class);

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<?> handleArticleNotFound(ArticleNotFoundException e, HttpServletRequest request) {
        logger.error("Article not found error: {}", e.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DuplicatedArticleException.class)
    public ResponseEntity<?> handleDuplicatedArticle(DuplicatedArticleException e, HttpServletRequest request) {
        logger.error("Duplicated article error: {}", e.getMessage());

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
        // 422 Unprocessable Entity response status code indicates that the server understands the content type of the
        // request entity, and the syntax of the request entity is correct, but it was unable to process the contained
        // instructions, because content is already exists.
    }

    @ExceptionHandler(IllegalArticleStateException.class)
    public ResponseEntity<?> handleIllegalArticleState(IllegalArticleStateException e, HttpServletRequest request) {
        logger.error("Illegal article state error: {}", e.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(new Date(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(response);
    }
}
