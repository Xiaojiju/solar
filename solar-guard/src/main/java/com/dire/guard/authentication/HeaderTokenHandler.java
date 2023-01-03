package com.dire.guard.authentication;

import org.springframework.security.core.Authentication;

import javax.security.auth.login.AccountExpiredException;

/**
 * @author 一块小饼干
 * token操作
 */
public interface HeaderTokenHandler {

    /**
     * 创建token
     * @param authentication 授权信息
     * @return token字符
     */
    Authentication createToken(Authentication authentication) throws AccountExpiredException;

    /**
     * 解析token
     * @param token 令牌
     * @return 授权信息
     */
    Authentication parseToken(String token) throws AccountExpiredException;

    /**
     * 清除token
     */
    void clear(Authentication authentication);

}
