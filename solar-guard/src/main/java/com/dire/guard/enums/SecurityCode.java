package com.dire.guard.enums;

public enum SecurityCode {

    TOKEN_EXPIRED(4001, "登录过期，请重新登录");

    private int code;
    private String message;

    SecurityCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
