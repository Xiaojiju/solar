package com.dire.guard.cache;

import com.dire.guard.UserTemplate;
import com.dire.guard.authentication.SecurityRedisKey;
import com.dire.tools.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class RedisUserCache implements UserCache {

    protected Logger logger = LoggerFactory.getLogger(RedisUserCache.class);
    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisUserCache(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UserDetails getUserFromCache(String username) {
        BoundValueOperations<Object, Object> ops = getOperations(username);
        String details = (String) ops.get();
        if (StringUtils.hasText(details)) {
            try {
                return JSONUtils.parse(details, UserTemplate.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public void putUserInCache(UserDetails user) {
        if (user == null) {
            return ;
        }
        try {
            UserTemplate authority = (UserTemplate) user;
            String authorityValue = JSONUtils.toJsonString(authority);
            BoundValueOperations<Object, Object> ops = getOperations(authority.getUsername());
            ops.set(authorityValue);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("UserDetails cast to UserTemplate error");
            }
        }
    }

    @Override
    public void removeUserFromCache(String username) {
        if (!StringUtils.hasText(username)) {
            return ;
        }
        redisTemplate.delete(getKey(username));
    }

    private BoundValueOperations<Object, Object> getOperations(String username) {
        String key = getKey(username);
        return redisTemplate.boundValueOps(key);
    }

    private String getKey(String username) {
        return SecurityRedisKey.makeKey(SecurityRedisKey.TARGET_USER_CACHE, username);
    }
}
