// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.exceptions;

public class DuplicatedUserByEmailException extends DuplicatedUserException {
    private static final String EMAIL_IN_USE = "such email is in use";

    public DuplicatedUserByEmailException() {
        super(EMAIL_IN_USE);
    }
}
