package com.salat.briene.exceptions;

public class DuplicatedUserException extends IllegalStateException {
    private static final String USER_EXISTS = "Such user exists";

    public DuplicatedUserException() {
        super(USER_EXISTS);
    }

    public DuplicatedUserException(String msg) {
        super("[%s] %s".formatted(USER_EXISTS, msg));
    }

    public DuplicatedUserException(String msg, Throwable cause) {
        super("[%s] %s".formatted(USER_EXISTS, msg), cause);
    }
}
