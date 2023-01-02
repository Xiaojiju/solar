package com.dire.guard.config;

import com.dire.guard.mapper.UserServiceMapper;
import com.dire.guard.service.GrantAuthorityService;
import com.dire.guard.service.NullGrantAuthorityServiceImpl;
import com.dire.guard.service.UserFromJdbcImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
public class WebAuthenticationConfig {

    private final WebSecurityProperties webSecurityProperties;

    public WebAuthenticationConfig(WebSecurityProperties webSecurityProperties) {
        this.webSecurityProperties = webSecurityProperties;
    }

    @Bean
    @ConditionalOnMissingBean(GrantAuthorityService.class)
    public GrantAuthorityService grantAuthorityService() {
        return new NullGrantAuthorityServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userTemplateService(UserServiceMapper userServiceMapper, MessageSource messageSource) {
        UserFromJdbcImpl userFromJdbc = new UserFromJdbcImpl(userServiceMapper, webSecurityProperties.isEnablePermissions());
        userFromJdbc.setMessageSource(messageSource);
        return userFromJdbc;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                               MessageSource messageSource) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setMessageSource(messageSource);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        daoAuthenticationProvider.setForcePrincipalAsString(false);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider daoAuthenticationProvider) {
        return new ProviderManager(daoAuthenticationProvider);
    }
}
