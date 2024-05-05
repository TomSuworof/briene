// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.salat.briene.exceptions;

public class TagNotFoundException extends IllegalStateException {
    private static final String TAG_NOT_FOUND = "Tag not found";

    public TagNotFoundException() {
        super(TAG_NOT_FOUND);
    }

    public TagNotFoundException(String msg) {
        super(String.format("[%s] %s", TAG_NOT_FOUND, msg));
    }

    public TagNotFoundException(String msg, Throwable cause) {
        super(String.format("[%s] %s", TAG_NOT_FOUND, msg), cause);
    }
}
