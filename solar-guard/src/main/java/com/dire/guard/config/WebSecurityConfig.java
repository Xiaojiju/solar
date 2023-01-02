package com.dire.guard.config;

import com.dire.guard.filter.RequestBodyAuthenticationProcessingFilter;
import com.dire.guard.filter.ReturnResponseAuthenticationFailHandler;
import com.dire.guard.filter.ReturnResponseAuthenticationSuccessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
public class WebSecurityConfig {

    private AuthenticationManager authenticationManager;

    public WebSecurityConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        System.out.println("开始加载filterChain");
        security.addFilterAfter(requestBodyAuthenticationFilter(authenticationManager), LogoutFilter.class);
        security.csrf().disable();
        return security.build();
    }

    @Bean
    @ConditionalOnMissingBean(RequestBodyAuthenticationProcessingFilter.class)
    public RequestBodyAuthenticationProcessingFilter requestBodyAuthenticationFilter(AuthenticationManager authenticationManager) {
        RequestBodyAuthenticationProcessingFilter requestBodyAuthenticationProcessingFilter = new RequestBodyAuthenticationProcessingFilter();
        requestBodyAuthenticationProcessingFilter.setAuthenticationManager(authenticationManager);
        requestBodyAuthenticationProcessingFilter.setAuthenticationSuccessHandler(new ReturnResponseAuthenticationSuccessHandler());
        requestBodyAuthenticationProcessingFilter.setAuthenticationFailureHandler(new ReturnResponseAuthenticationFailHandler());
        return requestBodyAuthenticationProcessingFilter;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
