package com.salat.briene.exceptions;

public class DuplicatedUserByUsernameException extends DuplicatedUserException {
    public DuplicatedUserByUsernameException() {
        super("such username is in use");
    }
}
