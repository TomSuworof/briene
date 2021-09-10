package com.salat.briene.controlleradvices;

import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.DuplicatedArticleException;
import com.salat.briene.exceptions.IllegalArticleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ArticleAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ArticleAdvice.class);

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<?> handleArticleNotFound(ArticleNotFoundException e) {
        logger.error("Article not found error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedArticleException.class)
    public ResponseEntity<?> handleDuplicatedArticle(DuplicatedArticleException e) {
        logger.error("Duplicated article error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        // 422 Unprocessable Entity response status code indicates that the server understands the content type of the
        // request entity, and the syntax of the request entity is correct, but it was unable to process the contained
        // instructions, because content is already exists.
    }

    @ExceptionHandler(IllegalArticleStateException.class)
    public ResponseEntity<?> handleIllegalArticleState(IllegalArticleStateException e) {
        logger.error("Illegal article state error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
