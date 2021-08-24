package com.salat.briene.exceptions;

import java.io.IOException;

public class IllegalArticleStateException extends IOException {
    private static final String ILLEGAL_STATE = "[Unknown article state] ";

    public IllegalArticleStateException() {
        super(ILLEGAL_STATE);
    }

    public IllegalArticleStateException(String s) {
        super(ILLEGAL_STATE + s);
    }

    public IllegalArticleStateException(String message, Throwable cause) {
        super(ILLEGAL_STATE + message, cause);
    }
}

