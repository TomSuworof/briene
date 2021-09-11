package com.salat.briene.exceptions;

public class DuplicatedUserByEmailException extends DuplicatedUserException {
    public DuplicatedUserByEmailException() {
        super("such email is in use");
    }
}
