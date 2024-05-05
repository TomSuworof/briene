// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.payload.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PasswordResetResponse {
    private final static String message = "Secret code was sent to your email";
    private final String hiddenEmail;

    public PasswordResetResponse(String hiddenEmail) {
        this.hiddenEmail = hiddenEmail;
    }
}
