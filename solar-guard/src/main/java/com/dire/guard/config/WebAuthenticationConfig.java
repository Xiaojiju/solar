/*
 * Copyright 2022 一块小饼干(莫杨)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
