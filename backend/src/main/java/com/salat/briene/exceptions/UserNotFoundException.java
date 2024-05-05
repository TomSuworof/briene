// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {
    private static final String USER_NOT_FOUND = "User not found";

    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }

    public UserNotFoundException(String msg) {
        super(String.format("[%s] %s", USER_NOT_FOUND, msg));
    }

    public UserNotFoundException(String msg, Throwable cause) {
        super(String.format("[%s] %s", USER_NOT_FOUND, msg), cause);
    }
}
