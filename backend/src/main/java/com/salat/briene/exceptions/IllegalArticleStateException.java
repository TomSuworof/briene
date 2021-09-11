package com.salat.briene.exceptions;

import java.io.IOException;

public class IllegalArticleStateException extends IOException {
    private static final String ILLEGAL_STATE = "Unknown article state";

    public IllegalArticleStateException() {
        super(ILLEGAL_STATE);
    }

    public IllegalArticleStateException(String msg) {
        super("[%s] %s".formatted(ILLEGAL_STATE, msg));
    }

    public IllegalArticleStateException(String msg, Throwable cause) {
        super("[%s] %s".formatted(ILLEGAL_STATE, msg), cause);
    }
}
