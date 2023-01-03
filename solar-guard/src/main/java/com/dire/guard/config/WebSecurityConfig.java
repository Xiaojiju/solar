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

import com.dire.guard.authentication.Auth0HeaderTokenHandler;
import com.dire.guard.filter.RequestBodyAuthenticationProcessingFilter;
import com.dire.guard.filter.ReturnResponseAuthenticationFailHandler;
import com.dire.guard.filter.ReturnResponseAuthenticationSuccessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
public class WebSecurityConfig {

    private AuthenticationManager authenticationManager;
    private RedisTemplate<Object, Object> redisTemplate;

    public WebSecurityConfig(AuthenticationManager authenticationManager, RedisTemplate<Object, Object> redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security.addFilterAfter(requestBodyAuthenticationFilter(authenticationManager), LogoutFilter.class);
        security.csrf().disable();
        return security.build();
    }

    @Bean
    @ConditionalOnMissingBean(RequestBodyAuthenticationProcessingFilter.class)
    public RequestBodyAuthenticationProcessingFilter requestBodyAuthenticationFilter(AuthenticationManager authenticationManager) {
        RequestBodyAuthenticationProcessingFilter requestBodyAuthenticationProcessingFilter = new RequestBodyAuthenticationProcessingFilter();
        requestBodyAuthenticationProcessingFilter.setAuthenticationManager(authenticationManager);
        requestBodyAuthenticationProcessingFilter.setAuthenticationSuccessHandler(
                new ReturnResponseAuthenticationSuccessHandler(new Auth0HeaderTokenHandler(redisTemplate)));
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
