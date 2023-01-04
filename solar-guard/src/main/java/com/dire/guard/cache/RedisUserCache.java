package com.dire.guard.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;

public class RedisUserCache implements UserCache {

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisUserCache(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UserDetails getUserFromCache(String username) {
        return null;
    }

    @Override
    public void putUserInCache(UserDetails user) {

    }

    @Override
    public void removeUserFromCache(String username) {

    }
}
