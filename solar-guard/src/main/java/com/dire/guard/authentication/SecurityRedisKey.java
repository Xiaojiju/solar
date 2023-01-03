package com.dire.guard.authentication;

import com.dire.redis.RedisKey;

import java.util.Arrays;

/**
 * @author 一块小饼干
 * reddis key 常量
 */
public final class SecurityRedisKey extends RedisKey {

    private static final String KEY_SEPARATOR = ":";
    public static final String SCAN_ALL = "*";
    public static final String HEADER_TOKEN = "authentication:token";
    public static final String TARGET_KEY_BLACK_ITEM = "security:target_key:black_item";
    public static final String TARGET_KEY_FAIL_COUNTER = "security:target_key:fail_counter";
    public static final String TARGET_USER_CACHE = "target_user:target_key:";

    public static String makeKey(String key, String...patterns) {
        StringBuffer buffer = new StringBuffer(key);
        Arrays.stream(patterns).sorted().forEach(item -> buffer.append(KEY_SEPARATOR).append(item));
        return buffer.toString();
    }
}
