package com.salat.briene.exceptions;

import java.io.IOException;

public class DuplicatedArticleException extends IOException {
    private static final String ARTICLE_FOUND = "[Such article exists] ";

    public DuplicatedArticleException() {
        super(ARTICLE_FOUND);
    }

    public DuplicatedArticleException(String s) {
        super(ARTICLE_FOUND + s);
    }

    public DuplicatedArticleException(String message, Throwable cause) {
        super(ARTICLE_FOUND + message, cause);
    }
}
