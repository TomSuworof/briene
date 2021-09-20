package com.salat.briene.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AnonymousUserException extends AuthenticationException {
    private static final String NON_AUTHORIZED_USER = "User not authorized";

    public AnonymousUserException() {
        super(NON_AUTHORIZED_USER);
    }

    public AnonymousUserException(String msg) {
        super("[%s] %s".formatted(NON_AUTHORIZED_USER, msg));
    }

    public AnonymousUserException(String msg, Throwable cause) {
        super("[%s] %s".formatted(NON_AUTHORIZED_USER, msg), cause);
    }
}