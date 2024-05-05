// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.exceptions;

public class BlockedUserException extends IllegalStateException {
    private static final String BLOCKED_USER = "This user is blocked";

    public BlockedUserException() {
        super(BLOCKED_USER);
    }

    public BlockedUserException(String msg) {
        super(String.format("[%s] %s", BLOCKED_USER, msg));
    }

    public BlockedUserException(String msg, Throwable cause) {
        super(String.format("[%s] %s", BLOCKED_USER, msg), cause);
    }
}
