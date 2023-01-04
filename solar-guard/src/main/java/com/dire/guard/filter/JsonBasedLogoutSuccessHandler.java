package com.dire.guard.filter;

import com.dire.core.context.response.RestResult;
import com.dire.guard.ResponseUtils;
import com.dire.tools.JSONUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonBasedLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseUtils.writeObject(response, JSONUtils.toJsonString(RestResult.success()));
    }
}
