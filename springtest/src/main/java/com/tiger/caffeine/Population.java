package com.tiger.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Population {

    @Test
    public void get(){

        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();
        // if cached, return; otherwise create, cache and return
        // if can not create, return null
        String value1 = cache.get("key1", k -> k);

        // get a value, or null if absent
        String value2 = cache.getIfPresent("key2");
    }

    /**
     * caffeine提供四种加载缓存的策略
     * 1:手动,2自动:,3:异步手动,4:异步自动
     */

    @Test
    public void manual(){

        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();
        cache.put("key1", "value");
        cache.putAll(new HashMap<>());
    }



}
