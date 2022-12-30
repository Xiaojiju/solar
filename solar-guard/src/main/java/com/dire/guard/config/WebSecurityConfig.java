package com.dire.guard.config;

import com.dire.guard.autentication.JwtAuthenticationTokenProvider;
import com.dire.guard.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

@Configuration
public class WebSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(JwtAuthenticationTokenProvider jwtAuthenticationTokenProvider) {
        return new ProviderManager(jwtAuthenticationTokenProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public GrantAuthorityService grantAuthorityService() {
        return new NullGrantAuthorityServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserTemplateService userTemplateService(UserServiceMapper userServiceMapper, MessageSource messageSource) {
        UserFromJdbcImpl userFromJdbc = new UserFromJdbcImpl(userServiceMapper);
        userFromJdbc.setMessageSource(messageSource);
        return userFromJdbc;
    }
}
