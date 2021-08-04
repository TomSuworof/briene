package com.salat.briene.exceptions;

import java.io.IOException;

public class ArticleFoundException extends IOException {
    private static final String ARTICLE_FOUND = "[Such article exists] ";

    public ArticleFoundException() {
        super(ARTICLE_FOUND);
    }

    public ArticleFoundException(String s) {
        super(ARTICLE_FOUND + s);
    }

    public ArticleFoundException(String message, Throwable cause) {
        super(ARTICLE_FOUND + message, cause);
    }
}
