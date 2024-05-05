// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.exceptions;

public class PasswordResetRequestInvalidException extends IllegalStateException {
    private static final String PASSWORD_RESET_REQUEST_INVALID = "Password reset is invalid";

    public PasswordResetRequestInvalidException() {
        super(PASSWORD_RESET_REQUEST_INVALID);
    }

    public PasswordResetRequestInvalidException(String msg) {
        super(String.format("[%s] %s", PASSWORD_RESET_REQUEST_INVALID, msg));
    }

    public PasswordResetRequestInvalidException(String msg, Throwable cause) {
        super(String.format("[%s] %s", PASSWORD_RESET_REQUEST_INVALID, msg), cause);
    }
}
