package com.salat.briene.payload.response;

import com.salat.briene.entities.Role;
import com.salat.briene.entities.User;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserContainer {
    private final Long id;
    private final String username;
    private final Set<Role> roles;

    public UserContainer(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRoles();
    }
}
