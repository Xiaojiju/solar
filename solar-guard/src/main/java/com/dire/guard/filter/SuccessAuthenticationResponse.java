package com.dire.guard.filter;

public class SuccessAuthenticationResponse implements AuthenticationResponse {

    private final String uniqueId;
    private final String token;
    private final long expiredTime;
    private final long signTime;

    public SuccessAuthenticationResponse(String uniqueId, String token) {
        this(uniqueId, token, -1, -1);
    }

    public SuccessAuthenticationResponse(String uniqueId, String token, long expiredTime, long signTime) {
        this.uniqueId = uniqueId;
        this.token = token;
        this.expiredTime = expiredTime;
        this.signTime = signTime;
    }

    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public long expiredTime() {
        return this.expiredTime;
    }

    @Override
    public long signTime() {
        return this.signTime;
    }
}
