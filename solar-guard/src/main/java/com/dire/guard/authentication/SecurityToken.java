package com.dire.guard.authentication;

import org.springframework.util.Assert;

import java.io.Serializable;

public class SecurityToken implements SignToken, Serializable {

    private final String accessToken;
    private final String refreshToken;
    private final long signAccessTimestamp;
    private final long expiredTimestamp;

    public SecurityToken(String accessToken, String refreshToken, long expiredTimestamp) {
        this(accessToken, refreshToken, System.currentTimeMillis(), expiredTimestamp);
    }

    public SecurityToken(String accessToken, String refreshToken, long signAccessTimestamp, long expiredTimestamp) {
        long current = System.currentTimeMillis();
        Assert.notNull(accessToken, "accessToken could not be null");
        Assert.notNull(refreshToken, "refreshToken could not be null");
        Assert.isTrue(signAccessTimestamp <= current, "signAccessTimestamp must greater than current time mills");
        Assert.isTrue(expiredTimestamp <= signAccessTimestamp, "expiredTimestamp must greater than signAccessTimestamp");
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.signAccessTimestamp = signAccessTimestamp;
        this.expiredTimestamp = expiredTimestamp;
    }

    public static SecurityTokenBuilder builder() {
        return new SecurityTokenBuilder();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getSignAccessTimestamp() {
        return signAccessTimestamp;
    }

    public long getExpiredTimestamp() {
        return expiredTimestamp;
    }

    public static class SecurityTokenBuilder {

        private String accessToken;
        private String refreshToken;
        private long signAccessTimestamp;
        private long expiredTimestamp;

        private SecurityTokenBuilder() {
        }

        public SecurityToken build() {
            return new SecurityToken(this.accessToken, this.refreshToken, this.signAccessTimestamp, this.expiredTimestamp);
        }

        public SecurityTokenBuilder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public SecurityTokenBuilder setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public SecurityTokenBuilder setSignAccessTimestamp(long signAccessTimestamp) {
            this.signAccessTimestamp = signAccessTimestamp;
            return this;
        }

        public SecurityTokenBuilder setExpiredTimestamp(long expiredTimestamp) {
            this.expiredTimestamp = expiredTimestamp;
            return this;
        }
    }
}
