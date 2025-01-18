package com.tiger.distrubuted;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;


import org.springframework.stereotype.Component;

@Component
class RedisLuaLimiterByIncr {
    private String KEY_PREFIX = "limiter_";
    private String QPS = "4";
    private int WINDOW_SIZE = 1000; // 窗口大小, 同时也是key的过期时间, 毫秒;

    private DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();

    public static final String LUA = "slide_window_rate_limiter.lua";

    RedisLuaLimiterByIncr() {
        redisScript.setResultType(Long.class);
        // lua文件存放在resources目录下
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LUA)));
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 固定窗口算法限流, 原理是计算请求所属的窗口的开始时间, 作为redis中string的key 然后统计每个key的请求次数, 并判断是否超过限流阈值
     */
    public boolean acquire(String key) {
        // 根据窗口大小, 计算窗口开始的时间
        long current = System.currentTimeMillis();
        long start = current - (current % WINDOW_SIZE);
        key = KEY_PREFIX + key + start;
        return stringRedisTemplate.execute(redisScript, Arrays.asList(key), QPS, WINDOW_SIZE) == 1;
    }
}
