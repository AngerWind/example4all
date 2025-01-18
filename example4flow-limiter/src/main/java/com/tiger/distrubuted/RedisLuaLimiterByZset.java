package com.tiger.distrubuted;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
class RedisLuaLimiterByZset {

    private String KEY_PREFIX = "limiter_";
    private String QPS = "4";
    private int WINDOW_SIZE = 1000; // 窗口大小, 毫秒
    public static final String LUA = "slide_window_rate_limiter.lua";

    DefaultRedisScript<Long> redisScript;

    RedisLuaLimiterByZset(){
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        //lua文件存放在resources目录下
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LUA)));
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    /**
     * 使用zset实现滑动窗口限流, zset中时间戳作为value, 同时也作为score
     * 先清除zset中无效的value, 然后判断个数是否达到阈值
     */
    public boolean acquire(String key) {
        long now = System.currentTimeMillis();
        key = KEY_PREFIX + key;
        String oldest = String.valueOf(now - WINDOW_SIZE); // 计算窗口的开始时间
        String score = String.valueOf(now);
        return stringRedisTemplate.execute(redisScript, Arrays.asList(key), oldest, score, QPS) == 1;
    }
}
