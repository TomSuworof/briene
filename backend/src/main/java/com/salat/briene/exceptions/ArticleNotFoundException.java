package com.salat.briene.exceptions;

public class ArticleNotFoundException extends IllegalStateException {
    private static final String ARTICLE_NOT_FOUND = "Such article does not exist";

    public ArticleNotFoundException() {
        super(ARTICLE_NOT_FOUND);
    }

    public ArticleNotFoundException(String msg) {
        super(String.format("[%s] %s", ARTICLE_NOT_FOUND, msg));
    }

    public ArticleNotFoundException(String msg, Throwable cause) {
        super(String.format("[%s] %s", ARTICLE_NOT_FOUND, msg), cause);
    }
}
