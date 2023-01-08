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

import com.dire.guard.authentication.JsonBasedAccessTokenProvider;
import com.dire.guard.filter.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class WebSecurityConfig {

    private AuthenticationManager authenticationManager;

    public WebSecurityConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean
    @ConditionalOnMissingBean(RequestBodyAuthenticationProcessingFilter.class)
    public RequestBodyAuthenticationProcessingFilter requestBodyAuthenticationFilter(
            AuthenticationManager authenticationManager, JsonBasedAccessTokenProvider jsonBasedAccessTokenProvider) {
        RequestBodyAuthenticationProcessingFilter requestBodyAuthenticationProcessingFilter = new RequestBodyAuthenticationProcessingFilter();
        requestBodyAuthenticationProcessingFilter.setAuthenticationManager(authenticationManager);
        requestBodyAuthenticationProcessingFilter.setAuthenticationSuccessHandler(
                new ReturnResponseAuthenticationSuccessHandler(jsonBasedAccessTokenProvider));
        requestBodyAuthenticationProcessingFilter.setAuthenticationFailureHandler(new ReturnResponseAuthenticationFailHandler());
        return requestBodyAuthenticationProcessingFilter;
    }

    @Bean
    public RequestBodyLogoutFilter requestBodyLogoutFilter(JsonBasedAccessTokenProvider jsonBasedAccessTokenProvider) {
        return new RequestBodyLogoutFilter(
                new JsonBasedLogoutSuccessHandler(), new CacheClearLogoutHandler(jsonBasedAccessTokenProvider));
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
