package com.salat.briene.exceptions;

public class DuplicatedArticleException extends IllegalStateException {
    private static final String ARTICLE_FOUND = "Such article exists";

    public DuplicatedArticleException() {
        super(ARTICLE_FOUND);
    }

    public DuplicatedArticleException(String msg) {
        super(String.format("[%s] %s", ARTICLE_FOUND, msg));
    }

    public DuplicatedArticleException(String msg, Throwable cause) {
        super(String.format("[%s] %s", ARTICLE_FOUND, msg), cause);
    }
}
