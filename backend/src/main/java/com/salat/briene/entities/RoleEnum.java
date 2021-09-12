package com.salat.briene.entities;

import com.salat.briene.exceptions.RoleNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    BLOCKED("delete", "blocked", new Role(0L, "ROLE_BLOCKED")),
    ADMIN("make_admin", "admin", new Role(1L, "ROLE_ADMIN")),
    USER("make_user", "user", new Role(2L, "ROLE_USER"));

    private final String actionForThisRole;
    private final String description;
    private final Role asObject; // should be used for persistence

    public static RoleEnum getFromAction(String action) {
        return switch (action) {
            case "delete" -> BLOCKED;
            case "make_admin" -> ADMIN;
            case "make_user" -> USER;
            default -> throw new RoleNotFoundException();
        };
    }

    public static RoleEnum getFromDescription(String description) {
        return switch (description) {
            case "blocked" -> BLOCKED;
            case "admin" -> ADMIN;
            case "user" -> USER;
            default -> throw new RoleNotFoundException();
        };
    }
}
