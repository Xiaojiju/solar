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

import com.dire.guard.cache.RedisUserCache;
import com.dire.guard.filter.RequestBodyAuthenticationProcessingFilter;
import com.dire.guard.filter.RequestBodyLogoutFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 密码加密配置
 * @author 一块小饼干
 * @since 1.0.0
 */
@Import({
        WebSecurityConfig.class,
        WebAuthenticationConfig.class,
        WebSecurityProperties.class,
        RedisUserCache.class
})
@Configuration
public class SecurityAutoConfiguration {

    private RequestBodyLogoutFilter requestBodyLogoutFilter;
    private RequestBodyAuthenticationProcessingFilter requestBodyAuthenticationProcessingFilter;

    public SecurityAutoConfiguration(RequestBodyLogoutFilter requestBodyLogoutFilter,
                                     RequestBodyAuthenticationProcessingFilter requestBodyAuthenticationProcessingFilter) {
        this.requestBodyLogoutFilter = requestBodyLogoutFilter;
        this.requestBodyAuthenticationProcessingFilter = requestBodyAuthenticationProcessingFilter;
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security.logout().disable()
                .csrf().disable()
                .addFilterAfter(requestBodyAuthenticationProcessingFilter, LogoutFilter.class)
                .addFilterAfter(requestBodyLogoutFilter, LogoutFilter.class);
        return security.build();
    }

    public RequestBodyLogoutFilter getRequestBodyLogoutFilter() {
        return requestBodyLogoutFilter;
    }

    public void setRequestBodyLogoutFilter(RequestBodyLogoutFilter requestBodyLogoutFilter) {
        this.requestBodyLogoutFilter = requestBodyLogoutFilter;
    }

    public RequestBodyAuthenticationProcessingFilter getRequestBodyAuthenticationProcessingFilter() {
        return requestBodyAuthenticationProcessingFilter;
    }

    public void setRequestBodyAuthenticationProcessingFilter(RequestBodyAuthenticationProcessingFilter requestBodyAuthenticationProcessingFilter) {
        this.requestBodyAuthenticationProcessingFilter = requestBodyAuthenticationProcessingFilter;
    }
}
