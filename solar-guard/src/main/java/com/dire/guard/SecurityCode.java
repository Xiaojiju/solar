package com.dire.guard;

import com.dire.core.CodeExpression;

public enum SecurityCode implements CodeExpression {

    BAD_CREDENTIAL(50005, "Bad credentials"),
    NO_AUTHORITIES(50006, "No authorities"),
    HAS_EXPIRED(50007, "has expired"),
    USER_NOT_FOUND(50008, "not found"),
    LOCKED(50009, "locked");

    private final int code;
    private final String message;

    SecurityCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
