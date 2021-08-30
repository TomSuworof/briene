package com.salat.briene.controlleradvices;

import com.salat.briene.exceptions.ArticleNotFoundException;
import com.salat.briene.exceptions.DuplicatedArticleException;
import com.salat.briene.exceptions.IllegalArticleStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ArticleAdvice {
    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<?> handleArticleNotFound(ArticleNotFoundException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedArticleException.class)
    public ResponseEntity<?> handleDuplicatedArticle(DuplicatedArticleException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.SEE_OTHER).body(e.getMessage());
        // According to RFC 7231, a 303 See Other MAY be used If the result of processing a POST would be equivalent to
        // a representation of an existing resource.
    }

    @ExceptionHandler(IllegalArticleStateException.class)
    public ResponseEntity<?> handleIllegalArticleState(IllegalArticleStateException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
