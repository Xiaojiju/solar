package com.dire.guard.filter;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class RequestBodyLogoutFilter extends LogoutFilter {

    private static final String LOGOUT_URL = "/solar/logout";
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(LOGOUT_URL, "POST");

    public RequestBodyLogoutFilter(LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers) {
        super(logoutSuccessHandler, handlers);
        super.setLogoutRequestMatcher(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

}
