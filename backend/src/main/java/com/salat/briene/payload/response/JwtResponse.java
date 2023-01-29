package com.salat.briene.payload.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
public class JwtResponse {
    private final String type = "Bearer";

    @ToString.Exclude private String token;
    private UUID id;
    private String username;
    private String email;
    private Set<String> roles;

    public JwtResponse(String accessToken, UUID id, String username, String email, Set<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}