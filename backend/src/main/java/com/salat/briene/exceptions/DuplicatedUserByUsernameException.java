// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.exceptions;

public class DuplicatedUserByUsernameException extends DuplicatedUserException {
    private static final String USERNAME_IN_USE = "Such username is in use";

    public DuplicatedUserByUsernameException() {
        super(USERNAME_IN_USE);
    }
}
