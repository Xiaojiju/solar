package com.dire.guard.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dire.guard.UserTemplate;
import com.dire.tools.JSONUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.login.AccountExpiredException;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * auth0解析token
 * @author 一块小饼干
 * @since 1.0.0
 */
public class Auth0HeaderTokenHandler implements HeaderTokenHandler {
    /**
     * 过期时间，默认为30分钟
     */
    private long expiredIn = 30 * 60 * 1000;
    protected static final String SECRET = "X5V7QZ2Z4CB40A7T";
    protected static final String ISSUER = "一块小饼干";
    protected static final long BALANCE_MILLS = 5 * 60 * 100;
    protected static final String SIGN_TIME = "sign_time";
    protected static final String EXPIRED_IN = "expired_in";
    protected static final String U_ID = "u_id";
    protected static final String TARGET_KEY = "target_key";

    private final RedisTemplate<Object, Object> redisTemplate;

    public Auth0HeaderTokenHandler(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication createToken(Authentication authentication) throws AccountExpiredException {
        UserTemplate userTemplate = (UserTemplate) authentication.getPrincipal();
        long signTime = System.currentTimeMillis();
        String token = token(authentication, signTime);
        TokenDetails tokenDetails = new TokenDetails();
        tokenDetails.setToken(token);
        tokenDetails.setSignTime(signTime);
        tokenDetails.setExpiredIn(expiredIn);
        refresh(new UserDetailsWithToken(tokenDetails, userTemplate), false);
        return createSuccessAuthentication(userTemplate, tokenDetails);
    }

    @Override
    public Authentication parseToken(String token) throws AccountExpiredException {
        Map<String, Claim> map = claims(token);
        if (map == null) {
            throw new AccountExpiredException("登录过期，需要重新登录");
        }
        String key = SecurityRedisKey.makeKey(SecurityRedisKey.HEADER_TOKEN, map.get(U_ID).asString());
        UserDetailsWithToken userTemplate = JSONUtils.parse((String) redisTemplate.opsForValue().get(key), UserDetailsWithToken.class);
        if (userTemplate == null) {
            throw new AccountExpiredException("登录过期，需要重新登录");
        }
        // 防止同一账号异地登录
        TokenDetails tokenDetails = userTemplate.getTokenDetails();
        if (tokenDetails == null || !token.equals(tokenDetails.getToken())) {
            throw new AccountExpiredException("登录过期，需要重新登录");
        }
        refresh(userTemplate, false);
        return createSuccessAuthentication(userTemplate.getUserDetails(), tokenDetails);
    }

    @Override
    public void clear(Authentication authentication) {
        if (authentication == null) {
            return ;
        }
        UserTemplate userTemplate = (UserTemplate) authentication.getPrincipal();
        String key = SecurityRedisKey.makeKey(SecurityRedisKey.HEADER_TOKEN, userTemplate.getUsername());
        redisTemplate.delete(key);
    }

    protected Authentication createSuccessAuthentication(UserDetails userDetails, TokenDetails tokenDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, tokenDetails, userDetails.getAuthorities());
    }

    protected void refresh(UserDetailsWithToken userDetailsWithToken, boolean cached) throws AccountExpiredException {
        long current = System.currentTimeMillis();
        TokenDetails tokenDetails = userDetailsWithToken.getTokenDetails();
        long targetExpiredTime = tokenDetails.getSignTime() + tokenDetails.getExpiredIn();
        if (targetExpiredTime > 0L && targetExpiredTime < current) {
            throw new AccountExpiredException("登录过期，需要重新登录");
        }
        UserDetails userDetails = userDetailsWithToken.getUserDetails();
        String key = SecurityRedisKey.makeKey(SecurityRedisKey.HEADER_TOKEN, userDetails.getUsername());
        if (targetExpiredTime <= 0L || targetExpiredTime - current < BALANCE_MILLS) {
            tokenDetails.setSignTime(current);
            userDetailsWithToken = new UserDetailsWithToken(tokenDetails, userDetails);
            redisTemplate.opsForValue().set(key, JSONUtils.toJsonString(userDetailsWithToken), expiredIn, TimeUnit.MILLISECONDS);
        }
        if (!cached) {
            redisTemplate.opsForValue().set(key, JSONUtils.toJsonString(userDetailsWithToken), expiredIn, TimeUnit.MILLISECONDS);
        }

    }

    private Map<String, Claim> claims(String token) {
        DecodedJWT verify = verify(token);
        if (verify != null) {
            return verify.getClaims();
        }
        return null;
    }

    private String token(Authentication authentication, long signTime) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        UserTemplate userTemplate = (UserTemplate) authentication.getPrincipal();
        return JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(new Date())
                .withClaim(SIGN_TIME, signTime)
                .withClaim(EXPIRED_IN, expiredIn)
                .withClaim(U_ID, userTemplate.getUsername())
                .sign(algorithm);
    }

    private static DecodedJWT verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public long getExpiredIn() {
        return expiredIn;
    }

    public void setExpiredIn(long expiredIn) {
        this.expiredIn = expiredIn;
    }

    public static class TokenDetails implements Serializable {

        public static final String TOKEN_HEADER = "Authentication";
        private String token;
        private long expiredIn;
        private long signTime;
        private String Header = TOKEN_HEADER;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getExpiredIn() {
            return expiredIn;
        }

        public void setExpiredIn(long expiredIn) {
            this.expiredIn = expiredIn;
        }

        public long getSignTime() {
            return signTime;
        }

        public void setSignTime(long signTime) {
            this.signTime = signTime;
        }

        public String getHeader() {
            return Header;
        }

        public void setHeader(String header) {
            Header = header;
        }
    }

    public static class UserDetailsWithToken {

        private final TokenDetails tokenDetails;
        private final UserDetails userDetails;

        public UserDetailsWithToken(TokenDetails tokenDetails, UserDetails userDetails) {
            this.tokenDetails = tokenDetails;
            this.userDetails = userDetails;
        }

        public TokenDetails getTokenDetails() {
            return tokenDetails;
        }

        public UserDetails getUserDetails() {
            return userDetails;
        }
    }

}
