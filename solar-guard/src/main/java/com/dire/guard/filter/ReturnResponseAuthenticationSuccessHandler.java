package com.dire.guard.filter;

import com.dire.core.context.response.RestResult;
import com.dire.guard.ResponseUtils;
import com.dire.guard.UserTemplate;
import com.dire.guard.authentication.AccessTokenProvider;
import com.dire.guard.authentication.SignToken;
import com.dire.tools.JSONUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.security.auth.login.AccountExpiredException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReturnResponseAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private AccessTokenProvider accessTokenProvider;

    public ReturnResponseAuthenticationSuccessHandler(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        try {
            Authentication tokenAuthentication = makeToken(authentication);
            ResponseUtils.writeObject(response, JSONUtils.toJsonString(RestResult.success(tokenAuthentication.getCredentials())));
        } catch (AccountExpiredException accountExpiredException) {
            new ReturnResponseAuthenticationFailHandler().onAuthenticationFailure(request, response,
                    new org.springframework.security.authentication.AccountExpiredException(accountExpiredException.getMessage()));
        }
    }

    protected Authentication makeToken(Authentication authentication) throws AccountExpiredException {
        SignToken securityToken = accessTokenProvider.create(authentication.getPrincipal());
        UserTemplate userTemplate = (UserTemplate) authentication.getPrincipal();
        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), securityToken, userTemplate.getAuthorities());
    }

    public AccessTokenProvider getAccessTokenProvider() {
        return accessTokenProvider;
    }

    public void setAccessTokenProvider(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }
}
