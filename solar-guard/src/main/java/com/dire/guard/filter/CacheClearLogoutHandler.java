package com.dire.guard.filter;

import com.dire.guard.authentication.HeaderTokenHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheClearLogoutHandler implements LogoutHandler {

    private final HeaderTokenHandler headerTokenHandler;

    public CacheClearLogoutHandler(HeaderTokenHandler headerTokenHandler) {
        this.headerTokenHandler = headerTokenHandler;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        headerTokenHandler.clear(authentication);
    }
}
