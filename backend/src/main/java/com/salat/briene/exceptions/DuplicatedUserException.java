package com.salat.briene.exceptions;

import org.springframework.security.core.AuthenticationException;

public class DuplicatedUserException extends AuthenticationException {
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
