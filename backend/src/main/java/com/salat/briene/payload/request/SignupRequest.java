package com.salat.briene.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class SignupRequest {
    private String username;
    private String email;
    @ToString.Exclude private String password;
    private Set<String> roles;
}