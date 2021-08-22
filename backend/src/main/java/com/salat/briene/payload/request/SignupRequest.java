package com.salat.briene.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    private String username;
    private String email;
    private String secretQuestion;
    private String secretAnswer;
    private String password;
    private Set<String> roles;
}