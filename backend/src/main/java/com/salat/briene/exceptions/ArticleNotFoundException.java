package com.salat.briene.exceptions;

import java.io.IOException;

public class ArticleNotFoundException extends IOException {
    private static final String ARTICLE_NOT_FOUND = "[Such article does not exist] ";

    public ArticleNotFoundException() {
        super(ARTICLE_NOT_FOUND);
    }

    public ArticleNotFoundException(String s) {
        super(ARTICLE_NOT_FOUND + s);
    }

    public ArticleNotFoundException(String message, Throwable cause) {
        super(ARTICLE_NOT_FOUND + message, cause);
    }
}
