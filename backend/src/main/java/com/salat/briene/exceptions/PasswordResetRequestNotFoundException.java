package com.salat.briene.exceptions;

public class PasswordResetRequestNotFoundException extends IllegalStateException {
    private static final String PASSWORD_RESET_REQUEST_NOT_EXISTS = "Password reset request does not exist";

    public PasswordResetRequestNotFoundException() {
        super(PASSWORD_RESET_REQUEST_NOT_EXISTS);
    }

    public PasswordResetRequestNotFoundException(String msg) {
        super("[%s] %s".formatted(PASSWORD_RESET_REQUEST_NOT_EXISTS, msg));
    }

    public PasswordResetRequestNotFoundException(String msg, Throwable cause) {
        super("[%s] %s".formatted(PASSWORD_RESET_REQUEST_NOT_EXISTS, msg), cause);
    }
}
