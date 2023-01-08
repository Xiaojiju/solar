package com.dire.guard.authentication;

public interface AccessTokenProvider {

    SignToken create(Object principal);

    SignToken refresh(Object principal, String refreshToken);

    void check(String token);

}
