package com.dire.guard.authentication;

public interface SignToken {

    String getAccessToken();

     String getRefreshToken();

     long getSignAccessTimestamp();

     long getExpiredTimestamp();
}
