// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.exceptions;

public class DuplicatedUserException extends IllegalStateException {
    private static final String USER_EXISTS = "Such user exists";

    public DuplicatedUserException() {
        super(USER_EXISTS);
    }

    public DuplicatedUserException(String msg) {
        super(String.format("[%s] %s", USER_EXISTS, msg));
    }

    public DuplicatedUserException(String msg, Throwable cause) {
        super(String.format("[%s] %s", USER_EXISTS, msg), cause);
    }
}
