package com.salat.briene.exceptions;

public class BlockedUserException extends IllegalStateException {
    private static final String BLOCKED_USER = "This user is blocked";

    public BlockedUserException() {
        super(BLOCKED_USER);
    }

    public BlockedUserException(String msg) {
        super("[%s] %s".formatted(BLOCKED_USER, msg));
    }

    public BlockedUserException(String msg, Throwable cause) {
        super("[%s] %s".formatted(BLOCKED_USER, msg), cause);
    }
}
