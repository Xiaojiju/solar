package com.dire.guard;

import com.dire.guard.common.SecurityRedisKey;
import com.dire.guard.core.UserAuthority;
import com.dire.tools.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
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
                return JSONUtils.from(details, UserAuthority.class);
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
            UserAuthority authority = (UserAuthority) user;
            String authorityValue = JSONUtils.to(authority);
            BoundValueOperations<Object, Object> ops = getOperations(authority.getUsername());
            ops.set(authorityValue);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("UserDetails cast to UserAuthority error");
            }
        }
    }

    // todo 修改用户信息后，需要进行清除缓存
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
