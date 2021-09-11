package com.salat.briene.exceptions;

public class DuplicatedUserByEmailException extends DuplicatedUserException {
    private static final String EMAIL_IN_USE = "such email is in use";

    public DuplicatedUserByEmailException() {
        super(EMAIL_IN_USE);
    }
}
