// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.exceptions;

public class RoleNotFoundException extends IllegalStateException {
    private static final String ROLE_NOT_FOUND = "Role not found";

    public RoleNotFoundException() {
        super(ROLE_NOT_FOUND);
    }

    public RoleNotFoundException(String msg) {
        super(String.format("[%s] %s", ROLE_NOT_FOUND, msg));
    }

    public RoleNotFoundException(String msg, Throwable cause) {
        super(String.format("[%s] %s", ROLE_NOT_FOUND, msg), cause);
    }
}
