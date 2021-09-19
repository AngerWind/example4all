package com.tiger.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Removal
 * @date 2021/9/1 16:10
 * @description
 */
public class Removal {
    /*
    eviction表示因为过期策略而被移除
    invalidation表示手动移除缓存
    removal表示两者的结合
     */

    @Test
    public void explicitRemovals(){

        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .removalListener(new RemovalListener<String, String>() {
                    // 缓存移除（过期或者手动移除）时被异步调用
                    @Override
                    public void onRemoval(@Nullable String key, @Nullable String value, @NonNull RemovalCause cause) {
                        System.out.printf("%s %s on removal, cause %s%n", key, value, cause);
                    }
                })
                .evictionListener(new RemovalListener<String, String>() {
                    // 缓存过期时被异步调用
                    @Override
                    public void onRemoval(@Nullable String key, @Nullable String value, RemovalCause cause) {
                        System.out.printf("%s %s on eviction, cause %s%n", key, value, cause);
                    }
                })
                .build();

        // 移除单个key
        cache.invalidate("key");
        // 批量移除
        cache.invalidateAll(new ArrayList<>());
        // 移除所有
        cache.invalidateAll();

    }
}
