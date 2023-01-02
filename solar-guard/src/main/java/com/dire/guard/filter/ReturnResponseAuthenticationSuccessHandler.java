package com.dire.guard.filter;

import com.dire.core.context.response.RestResult;
import com.dire.guard.ResponseUtils;
import com.dire.guard.UserTemplate;
import com.dire.tools.JSONUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReturnResponseAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        Assert.isInstanceOf(UserTemplate.class, principal, "ReturnResponseAuthenticationSuccessHandler just " +
                "only UserTemplate is supported");
        UserTemplate userTemplate = (UserTemplate) principal;
        SuccessAuthenticationResponse responseBody = new SuccessAuthenticationResponse(userTemplate.getUsername(), "");
        ResponseUtils.writeObject(response, JSONUtils.toJsonString(RestResult.success(responseBody)));
    }

}
