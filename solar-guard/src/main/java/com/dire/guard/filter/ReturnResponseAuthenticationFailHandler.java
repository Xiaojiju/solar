package com.dire.guard.filter;

import com.dire.core.ResultCode;
import com.dire.core.context.response.RestResult;
import com.dire.guard.ResponseUtils;
import com.dire.guard.SecurityCode;
import com.dire.tools.JSONUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReturnResponseAuthenticationFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
        RestResult<Void> failResult;
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            failResult = RestResult.complete(SecurityCode.BAD_CREDENTIAL);
        } else if (exception instanceof InternalAuthenticationServiceException) {
            failResult = RestResult.complete(SecurityCode.NO_AUTHORITIES);
        } else {
            failResult = RestResult.complete(ResultCode.FORBIDDEN);
        }
        ResponseUtils.writeObject(response, JSONUtils.toJsonString(failResult));
    }
}
