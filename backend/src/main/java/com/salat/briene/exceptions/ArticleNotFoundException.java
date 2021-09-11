package com.salat.briene.exceptions;

import java.io.IOException;

public class ArticleNotFoundException extends IOException {
    private static final String ARTICLE_NOT_FOUND = "Such article does not exist";

    public ArticleNotFoundException() {
        super(ARTICLE_NOT_FOUND);
    }

    public ArticleNotFoundException(String msg) {
        super("[%s] %s".formatted(ARTICLE_NOT_FOUND, msg));
    }

    public ArticleNotFoundException(String msg, Throwable cause) {
        super("[%s] %s".formatted(ARTICLE_NOT_FOUND, msg), cause);
    }
}
