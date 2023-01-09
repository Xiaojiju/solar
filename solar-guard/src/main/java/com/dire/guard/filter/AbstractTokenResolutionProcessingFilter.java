package com.dire.guard.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTokenResolutionProcessingFilter extends GenericFilterBean {

    private final Log log = LogFactory.getLog(AbstractTokenResolutionProcessingFilter.class);
    private String[] skipUrls;
    private List<RequestMatcher> requestMatchers;

    public AbstractTokenResolutionProcessingFilter() {
        this.skipUrls = new String[0];
        this.requestMatchers = new ArrayList<>();
    }

    public AbstractTokenResolutionProcessingFilter(String[] skipUrls) {
        setSkipUrls(skipUrls);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        preResolve((HttpServletRequest) servletRequest,(HttpServletResponse) servletResponse, filterChain);
        Object token = getToken((HttpServletRequest) servletRequest);
        try {
            verifyToken(token);
        } catch (TokenExpiredException e) {
            log.debug("User token has expired");
            fail((HttpServletRequest) servletRequest,(HttpServletResponse) servletResponse,
                    new AccountExpiredException("User token has expired"));
        }
        success((HttpServletRequest) servletRequest,(HttpServletResponse) servletResponse, filterChain);
    }

    protected void preResolve(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (CollectionUtils.isEmpty(this.requestMatchers)) {
            return ;
        }
        for (RequestMatcher matcher : this.requestMatchers) {
            if (matcher.matcher(request).isMatch()) {
                filterChain.doFilter(request, response);
            }
        }
    }

    protected abstract Object getToken(HttpServletRequest request);

    protected abstract void verifyToken(Object token) throws TokenExpiredException;

    protected abstract void success(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException;

    protected abstract void fail(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException failed) throws IOException, ServletException;

    public String[] getSkipUrls() {
        return skipUrls;
    }

    public void setSkipUrls(String[] skipUrls) {
        List<RequestMatcher> requireSkipMatchers = new ArrayList<>();
        for (String pathUrl : skipUrls) {
            requireSkipMatchers.add(new AntPathRequestMatcher(pathUrl));
        }
        this.skipUrls = skipUrls;
        this.requestMatchers = requireSkipMatchers;
    }

    public List<RequestMatcher> getRequestMatchers() {
        return requestMatchers;
    }

    public void setRequestMatchers(List<RequestMatcher> requestMatchers) {
        this.requestMatchers = requestMatchers;
    }
}
