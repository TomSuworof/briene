package com.salat.briene.exceptions;

public class DuplicatedUserByUsernameException extends DuplicatedUserException {
    private static final String USERNAME_IN_USE = "Such username is in use";

    public DuplicatedUserByUsernameException() {
        super(USERNAME_IN_USE);
    }
}
