package com.salat.briene.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PasswordChangeRequest {
    private UUID requestId;
    private String newPassword;
}
