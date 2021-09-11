package com.salat.briene.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {
    private static final String USER_NOT_FOUND = "User not found";

    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }

    public UserNotFoundException(String msg) {
        super("[%s] %s".formatted(USER_NOT_FOUND, msg));
    }

    public UserNotFoundException(String msg, Throwable cause) {
        super("[%s] %s".formatted(USER_NOT_FOUND, msg), cause);
    }
}
