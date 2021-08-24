package com.salat.briene.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {
    private static final String USER_NOT_EXISTS = "[User not found] ";

    public UserNotFoundException(String msg, Throwable cause) {
        super(USER_NOT_EXISTS + msg, cause);
    }

    public UserNotFoundException(String msg) {
        super(USER_NOT_EXISTS + msg);
    }

    public UserNotFoundException() {
        super(USER_NOT_EXISTS);
    }
}
