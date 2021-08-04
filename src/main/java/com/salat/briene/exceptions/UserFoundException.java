package com.salat.briene.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserFoundException extends AuthenticationException {
    private static final String USER_EXISTS = "[Such user exists] ";

    public UserFoundException(String msg, Throwable cause) {
        super(USER_EXISTS + msg, cause);
    }

    public UserFoundException(String msg) {
        super(USER_EXISTS + msg);
    }

    public UserFoundException() {
        super(USER_EXISTS);
    }
}
