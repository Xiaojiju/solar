package com.dire.guard.config;

import com.dire.guard.filter.RequestBodyAuthenticationProcessingFilter;
import com.dire.guard.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
public class WebAuthenticationConfig {

    private AuthenticationManager authenticationManager;

    public WebAuthenticationConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security.addFilterAfter(requestBodyAuthenticationFilter(authenticationManager), LogoutFilter.class);
        security.csrf().disable();
        return security.build();
    }

    @Bean
    public RequestBodyAuthenticationProcessingFilter requestBodyAuthenticationFilter(AuthenticationManager authenticationManager) {
        RequestBodyAuthenticationProcessingFilter requestBodyAuthenticationProcessingFilter = new RequestBodyAuthenticationProcessingFilter();
        requestBodyAuthenticationProcessingFilter.setAuthenticationManager(authenticationManager);
        return requestBodyAuthenticationProcessingFilter;
    }
}
