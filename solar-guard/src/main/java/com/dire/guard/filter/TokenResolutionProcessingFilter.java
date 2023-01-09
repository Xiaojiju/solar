package com.dire.guard.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dire.core.context.response.RestResult;
import com.dire.guard.ResponseUtils;
import com.dire.guard.SecurityCode;
import com.dire.guard.authentication.AccessTokenProvider;
import com.dire.guard.authentication.JsonBasedAccessTokenProvider;
import com.dire.tools.JSONUtils;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenResolutionProcessingFilter extends AbstractTokenResolutionProcessingFilter {

    private static final String DEFAULT_HEADER_AUTH = "Authentication";
    private String authHeader;
    private AccessTokenProvider accessTokenProvider;

    public TokenResolutionProcessingFilter() {
        this.accessTokenProvider = new JsonBasedAccessTokenProvider();
        this.authHeader = DEFAULT_HEADER_AUTH;
    }

    public TokenResolutionProcessingFilter(String authHeader, AccessTokenProvider accessTokenProvider) {
        this.authHeader = authHeader;
        this.accessTokenProvider = accessTokenProvider;
    }

    public TokenResolutionProcessingFilter(String[] skipUrls, String authHeader, AccessTokenProvider accessTokenProvider) {
        super(skipUrls);
        this.authHeader = authHeader;
        this.accessTokenProvider = accessTokenProvider;
    }

    @Override
    protected Object getToken(HttpServletRequest request) {
        return request.getHeader(this.authHeader);
    }

    @Override
    protected void verifyToken(Object token) throws TokenExpiredException {
        String accessToken = (String) token;
        accessTokenProvider.check(accessToken);
    }

    @Override
    protected void success(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // redis 取数据
        chain.doFilter(request, response);
    }

    @Override
    protected void fail(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        RestResult<Void> failResult = RestResult.error(SecurityCode.HAS_EXPIRED.getCode(), "Has Expired");
        ResponseUtils.writeObject(response, JSONUtils.toJsonString(failResult));
    }

    public String getAuthHeader() {
        return authHeader;
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }

    public AccessTokenProvider getAccessTokenProvider() {
        return accessTokenProvider;
    }

    public void setAccessTokenProvider(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }
}
