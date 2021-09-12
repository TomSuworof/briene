package com.salat.briene.payload.response;

import com.salat.briene.entities.Role;
import com.salat.briene.entities.User;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
public class UserDTO {
    private final UUID id;
    private final String username;
    private final String email;
    private final Set<Role> roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
