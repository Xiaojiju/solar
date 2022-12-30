package com.dire.guard.autentication;

import com.dire.guard.AccountDetails;
import com.dire.guard.service.UserTemplateService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationTokenProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    private UserTemplateService userTemplateService;

    public JwtAuthenticationTokenProvider(UserTemplateService userTemplateService) {
        this.userTemplateService = userTemplateService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setMessageSource(MessageSource messageSource) {

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        String principal = (String) authenticationToken.getPrincipal();
        AccountDetails accountDetails = userTemplateService.loadUserByReferenceKey(principal);
        System.out.println(accountDetails);
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
