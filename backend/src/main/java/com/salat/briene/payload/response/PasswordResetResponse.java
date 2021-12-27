package com.salat.briene.payload.response;

import lombok.Getter;

@Getter
public class PasswordResetResponse {
    private final static String message = "Secret code was sent to your email";
    private final String hiddenEmail;

    public PasswordResetResponse(String hiddenEmail) {
        this.hiddenEmail = hiddenEmail;
    }
}
