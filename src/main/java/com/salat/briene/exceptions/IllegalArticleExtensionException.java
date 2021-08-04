package com.salat.briene.exceptions;

import java.io.IOException;

public class IllegalArticleExtensionException extends IOException {
    private static final String ILLEGAL_EXTENSION = "[Article should be in Markdown format] ";

    public IllegalArticleExtensionException() {
        super(ILLEGAL_EXTENSION);
    }

    public IllegalArticleExtensionException(String s) {
        super(ILLEGAL_EXTENSION + s);
    }

    public IllegalArticleExtensionException(String message, Throwable cause) {
        super(ILLEGAL_EXTENSION + message, cause);
    }
}
