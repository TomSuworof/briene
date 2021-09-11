package com.salat.briene.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JWTError {
    INVALID_SIGNATURE("Invalid JWT signature: {}"),
    INVALID_TOKEN("Invalid JWT token: {}"),
    TOKEN_IS_EXPIRED("JWT token is expired: {}"),
    TOKEN_IS_UNSUPPORTED("JWT token is unsupported: {}"),
    TOKEN_IS_EMPTY("JWT claims string is empty: {}");

    private String message;
}
