package com.dire.guard.exceptions;

import org.springframework.security.core.AuthenticationException;

public class NoAuthoritiesException extends AuthenticationException {

    public NoAuthoritiesException(String msg) {
        super(msg);
    }

    public NoAuthoritiesException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
