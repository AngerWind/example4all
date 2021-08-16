package com.tiger.guava_cache;

import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title GuavaCacheTest
 * @date 2021/6/7 15:17
 * @description
 */
public class GuavaCacheTest {

    public static void main(String[] args) {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        builder.maximumSize(2);
        builder.weakKeys();
        builder.weakValues();
        // 两者或
        builder.expireAfterWrite(24, TimeUnit.HOURS);
        builder.expireAfterAccess(24, TimeUnit.HOURS);

        builder.initialCapacity(2);
    }
}
