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
import com.dire.guard.authentication.HeaderTokenHandler;
import com.dire.guard.authentication.UserTemplatePreAuthenticationChecks;
import com.dire.guard.cache.RedisUserCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
public class WebAuthenticationConfig {

    private final RedisTemplate<Object, Object> redisTemplate;
    private final WebSecurityProperties webSecurityProperties;

    public WebAuthenticationConfig(RedisTemplate<Object, Object> redisTemplate, WebSecurityProperties webSecurityProperties) {
        this.redisTemplate = redisTemplate;
        this.webSecurityProperties = webSecurityProperties;
    }

    @Bean
    @ConditionalOnMissingBean(RedisUserCache.class)
    public RedisUserCache redisUserCache(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisUserCache(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(HeaderTokenHandler.class)
    public HeaderTokenHandler headerTokenHandler(RedisTemplate<Object, Object> redisTemplate) {
        return new Auth0HeaderTokenHandler(redisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(DaoAuthenticationProvider.class)
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                               MessageSource messageSource,
                                                               RedisUserCache redisUserCache) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setMessageSource(messageSource);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        daoAuthenticationProvider.setForcePrincipalAsString(false);
        daoAuthenticationProvider.setUserCache(redisUserCache);
        daoAuthenticationProvider.setPreAuthenticationChecks(new UserTemplatePreAuthenticationChecks());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider daoAuthenticationProvider) {
        return new ProviderManager(daoAuthenticationProvider);
    }

    public RedisTemplate<Object, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public WebSecurityProperties getWebSecurityProperties() {
        return webSecurityProperties;
    }
}
