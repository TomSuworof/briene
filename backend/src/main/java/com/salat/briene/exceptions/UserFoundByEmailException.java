package com.salat.briene.exceptions;

public class UserFoundByEmailException extends UserFoundException {
    public UserFoundByEmailException() {
        super("such email is in use");
    }
}
