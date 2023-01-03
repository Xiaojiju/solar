package com.dire.guard.filter;

import com.dire.core.context.response.RestResult;
import com.dire.guard.ResponseUtils;
import com.dire.guard.UserTemplate;
import com.dire.guard.authentication.Auth0HeaderTokenHandler;
import com.dire.guard.authentication.HeaderTokenHandler;
import com.dire.tools.JSONUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Assert;

import javax.security.auth.login.AccountExpiredException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReturnResponseAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private HeaderTokenHandler headerTokenHandler;

    public ReturnResponseAuthenticationSuccessHandler() {
        this.headerTokenHandler = new Auth0HeaderTokenHandler(null);
    }

    public ReturnResponseAuthenticationSuccessHandler(HeaderTokenHandler headerTokenHandler) {
        this.headerTokenHandler = headerTokenHandler;
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
        return  headerTokenHandler.createToken(authentication);
    }

    public HeaderTokenHandler getHeaderTokenHandler() {
        return headerTokenHandler;
    }

    public void setHeaderTokenHandler(HeaderTokenHandler headerTokenHandler) {
        this.headerTokenHandler = headerTokenHandler;
    }
}
