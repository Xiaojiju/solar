package com.dire.guard.filter;

import com.dire.guard.authentication.AccessTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheClearLogoutHandler implements LogoutHandler {

    private final AccessTokenProvider accessTokenProvider;

    public CacheClearLogoutHandler(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    }

    public AccessTokenProvider getAccessTokenProvider() {
        return accessTokenProvider;
    }
}
