package com.dire.core.context.exceptions;

public class ServiceException extends RuntimeException {

    private int code;

    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
