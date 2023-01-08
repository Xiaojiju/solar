package com.dire.guard.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dire.guard.UserTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class JsonBasedAccessTokenProvider implements AccessTokenProvider {

    private static final long DEFAULT_EXPIRED_TIME = 30 * 60 * 1000;
    private static final long DEFAULT_REFRESH_EXPIRED_TIME = 60 * 1000 * 24 * 7;
    private static final String SECRET = "X5V7QZ2Z4CB40A7T";
    private static final Algorithm DEFAULT_ALGORITHM = Algorithm.HMAC256(SECRET);
    protected static final String ISSUER = "一块小饼干";
    protected static final String U_ID = "u_id";

    private final long expiredTimestamp;
    private final long refreshExpiredTimestamp;
    private final Algorithm algorithm;

    public JsonBasedAccessTokenProvider() {
        this(DEFAULT_EXPIRED_TIME, DEFAULT_REFRESH_EXPIRED_TIME, DEFAULT_ALGORITHM);
    }

    public JsonBasedAccessTokenProvider(long expiredTimestamp, long refreshExpiredTimestamp, Algorithm algorithm) {
        this.expiredTimestamp = expiredTimestamp;
        this.refreshExpiredTimestamp = refreshExpiredTimestamp;
        this.algorithm = algorithm;
    }

    @Override
    public SignToken create(Object principal) {
        SecurityToken.SecurityTokenBuilder builder = SecurityToken.builder();
        long signAccessTimestamp = System.currentTimeMillis();
        String accessToken = createAccessToken(principal, signAccessTimestamp);
        String refreshToken = createRefreshToken(accessToken);
        builder.setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setSignAccessTimestamp(signAccessTimestamp)
                .setExpiredTimestamp(this.expiredTimestamp);
        return builder.build();
    }

    protected String createRefreshToken(String token) {
        long signAccessTimestamp = System.currentTimeMillis();
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(Instant.ofEpochMilli(signAccessTimestamp))
                .withExpiresAt(Instant.ofEpochMilli(this.refreshExpiredTimestamp))
                .withClaim("access_token", token);
        return builder.sign(this.algorithm);
    }

    protected String createAccessToken(Object principal, long signAccessTokenTimestamp) {
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(Instant.ofEpochMilli(signAccessTokenTimestamp))
                .withExpiresAt(Instant.ofEpochMilli(this.expiredTimestamp));
        Map<String, String> claims = claims(principal);
        Assert.notEmpty(claims, "claims must not null");
        for (Map.Entry<String, String> entry : claims.entrySet()) {
            builder.withClaim(entry.getKey(), entry.getValue());
        }
        return builder.sign(this.algorithm);
    }

    protected Map<String, String> claims(Object principal) {
        Assert.isInstanceOf(UserTemplate.class, principal, "only UserTemplate is supported");
        UserTemplate userTemplate = (UserTemplate) principal;
        Map<String, String> claims = new HashMap<>();
        claims.put(U_ID, userTemplate.getUsername());
        claims.put("current_method", userTemplate.getCurrentMethod());
        return claims;
    }

    @Override
    public SignToken refresh(Object principal, String refreshToken) {
        long currentMills = System.currentTimeMillis();
        DecodedJWT decodedJWT = parseToken(refreshToken);
        long expiredMills = decodedJWT.getExpiresAtAsInstant().toEpochMilli();
        if (currentMills <= expiredMills) {
            throw new TokenExpiredException("token has expired", Instant.now());
        }
        String accessToken = createAccessToken(principal, currentMills);
        refreshToken = createRefreshToken(accessToken);
        return SecurityToken.builder().setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setExpiredTimestamp(this.expiredTimestamp)
                .setSignAccessTimestamp(currentMills)
                .build();
    }

    @Override
    public void check(String token) {
        DecodedJWT decodedJWT = parseToken(token);
        if (decodedJWT == null) {
            throw new TokenExpiredException("token has expired", Instant.now());
        }
    }

    private DecodedJWT parseToken(String token) throws JWTVerificationException {
        try {
            JWTVerifier verifier = JWT.require(this.algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new TokenExpiredException("token has expired", Instant.now());
        }
    }

    public long getExpiredTimestamp() {
        return expiredTimestamp;
    }

    public long getRefreshExpiredTimestamp() {
        return refreshExpiredTimestamp;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
