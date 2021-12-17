package com.tiger.redisson.lock;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title RedisLockRegistry
 * @date 2021/12/17 10:38
 * @description
 */
public class RedisLockRegistry implements LockRegistry {
    protected String lockPrefix;
    protected StringRedisTemplate redisTemplate;

    public RedisLockRegistry(StringRedisTemplate redisTemplate,String lockPrefix) {
        this.redisTemplate = redisTemplate;
        this.lockPrefix = lockPrefix;
    }

    @Override
    public Lock obtain(String key) {
        return new RedisLockImpl(redisTemplate, lockPrefix + ":" + key);
    }
}