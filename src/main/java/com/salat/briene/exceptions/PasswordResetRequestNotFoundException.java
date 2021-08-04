package com.salat.briene.exceptions;

public class PasswordResetRequestNotFoundException extends IllegalStateException {
    private static final String PASSWORD_RESET_REQUEST_NOT_EXISTS = "[Password reset not exists] ";

    public PasswordResetRequestNotFoundException() {
        super(PASSWORD_RESET_REQUEST_NOT_EXISTS);
    }

    public PasswordResetRequestNotFoundException(String s) {
        super(PASSWORD_RESET_REQUEST_NOT_EXISTS + s);
    }

    public PasswordResetRequestNotFoundException(String message, Throwable cause) {
        super(PASSWORD_RESET_REQUEST_NOT_EXISTS + message, cause);
    }
}
