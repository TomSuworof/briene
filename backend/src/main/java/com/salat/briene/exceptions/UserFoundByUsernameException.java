package com.salat.briene.exceptions;

public class UserFoundByUsernameException extends UserFoundException {
    public UserFoundByUsernameException() {
        super("such username is in use");
    }
}
