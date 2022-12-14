package com.dire.guard.config;

import com.dire.guard.mapper.UserServiceMapper;
import com.dire.guard.service.GrantAuthorityService;
import com.dire.guard.service.NullGrantAuthorityServiceImpl;
import com.dire.guard.service.UserFromJdbcImpl;
import com.dire.guard.service.UserTemplateDetailsManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class SecurityServiceAutoConfigure {

    private WebSecurityProperties webSecurityProperties;

    public SecurityServiceAutoConfigure(WebSecurityProperties webSecurityProperties) {
        this.webSecurityProperties = webSecurityProperties;
    }

    @Bean
    @ConditionalOnMissingBean(GrantAuthorityService.class)
    public GrantAuthorityService grantAuthorityService() {
        return new NullGrantAuthorityServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService(UserServiceMapper userServiceMapper, MessageSource messageSource) {
        UserFromJdbcImpl userFromJdbc = new UserFromJdbcImpl(userServiceMapper, webSecurityProperties.isEnablePermissions());
        userFromJdbc.setMessageSource(messageSource);
        return userFromJdbc;
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsManager.class)
    public UserDetailsManager userDetailsManager(UserServiceMapper userServiceMapper, MessageSource messageSource) {
        UserTemplateDetailsManager manager = new UserTemplateDetailsManager(userServiceMapper);
        manager.setMessageSource(messageSource);
        return manager;
    }

    public WebSecurityProperties getWebSecurityProperties() {
        return webSecurityProperties;
    }

    public void setWebSecurityProperties(WebSecurityProperties webSecurityProperties) {
        this.webSecurityProperties = webSecurityProperties;
    }
}
