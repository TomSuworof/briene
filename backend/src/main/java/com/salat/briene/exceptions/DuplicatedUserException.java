package com.salat.briene.exceptions;

import org.springframework.security.core.AuthenticationException;

public class DuplicatedUserException extends AuthenticationException {
    private static final String USER_EXISTS = "[Such user exists] ";

    public DuplicatedUserException(String msg, Throwable cause) {
        super(USER_EXISTS + msg, cause);
    }

    public DuplicatedUserException(String msg) {
        super(USER_EXISTS + msg);
    }

    public DuplicatedUserException() {
        super(USER_EXISTS);
    }
}
