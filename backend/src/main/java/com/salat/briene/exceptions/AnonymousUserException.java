// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AnonymousUserException extends AuthenticationException {
    private static final String NON_AUTHORIZED_USER = "User not authorized";

    public AnonymousUserException() {
        super(NON_AUTHORIZED_USER);
    }

    public AnonymousUserException(String msg) {
        super(String.format("[%s] %s", NON_AUTHORIZED_USER, msg));
    }

    public AnonymousUserException(String msg, Throwable cause) {
        super(String.format("[%s] %s", NON_AUTHORIZED_USER, msg), cause);
    }
}