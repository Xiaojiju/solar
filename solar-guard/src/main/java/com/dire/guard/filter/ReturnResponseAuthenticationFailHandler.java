package com.dire.guard.filter;

import com.dire.core.ResultCode;
import com.dire.core.context.response.RestResult;
import com.dire.guard.ResponseUtils;
import com.dire.guard.SecurityCode;
import com.dire.guard.context.SolarMessageSource;
import com.dire.tools.JSONUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReturnResponseAuthenticationFailHandler implements AuthenticationFailureHandler, MessageSourceAware {

    private MessageSourceAccessor messages;

    public ReturnResponseAuthenticationFailHandler() {
        this.messages = SolarMessageSource.getAccessor();
    }

    public ReturnResponseAuthenticationFailHandler(MessageSource messages) {
        setMessageSource(messages);
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
        RestResult<Void> failResult;
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            failResult = RestResult.error(SecurityCode.BAD_CREDENTIAL.getCode(),
                    this.messages.getMessage("ReturnResponseAuthenticationFailHandler.bad_credential",
                            "bad credential"));
        } else if (exception instanceof InternalAuthenticationServiceException) {
            failResult = RestResult.error(SecurityCode.NO_AUTHORITIES.getCode(),
                    this.messages.getMessage("ReturnResponseAuthenticationFailHandler.no_authorities",
                            "no authorities"));
        } else if (exception instanceof AccountExpiredException) {
            failResult = RestResult.error(SecurityCode.HAS_EXPIRED.getCode(),
                    this.messages.getMessage("ReturnResponseAuthenticationFailHandler.expired",
                            "User has expired or current method has expired"));
        } else if (exception instanceof LockedException) {
            failResult = RestResult.error(SecurityCode.LOCKED.getCode(),
                    this.messages.getMessage("ReturnResponseAuthenticationFailHandler.locked",
                            "User has been locked"));
        } else {
            failResult = RestResult.complete(ResultCode.FORBIDDEN);
        }
        ResponseUtils.writeObject(response, JSONUtils.toJsonString(failResult));
    }
}
