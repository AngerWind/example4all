package com.tiger.caffeine;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.Test;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Refresh
 * @date 2021/9/1 17:28
 * @description
 */
public class Refresh {

    @Test
    public void refresh(){
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                // refresh是异步执行的，在refresh期间读将会返回旧值
                .refreshAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(@NonNull String key) throws Exception {
                        return null;
                    }

                    @Override
                    public String reload(@NonNull String key, @NonNull String oldValue) throws Exception {
                        return null;
                    }
                });

    }

    public static void main(String[] args) {
        LinkedList<String> strings = new LinkedList<>();
    }

}
