package com.dire.guard;

import com.dire.core.CodeExpression;

public enum SecurityCode implements CodeExpression {

    BAD_CREDENTIAL(50005, "Bad credentials"),
    NO_AUTHORITIES(50006, "No authorities"),
    USER_NOT_FOUND(50007, "");

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
