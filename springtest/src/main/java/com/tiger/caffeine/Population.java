package com.tiger.caffeine;

import com.github.benmanes.caffeine.cache.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;

public class Population {


    /**
     * caffeine提供四种加载缓存的策略
     * 1:手动加载,2自动加载:,3:异步手动,4:异步自动
     */

    // 手动加载
    @Test
    public void manual(){

        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();
        cache.put("key1", "value");
        cache.putAll(new HashMap<>(5));

        // if cached, return; otherwise create, cache and return
        // if can not create(throw exception), return null
        // 推荐使用这种方式， 因为判断是否存在和生成缓存是原子性的。
        String value1 = cache.get("key1", new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s + "hello world";
            }
        });

        // get a value, or null if absent
        String value2 = cache.getIfPresent("key2");

        // remove
        cache.invalidate("key1");
    }

    // 自动加载
    @Test
    public void loading(){
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Nullable
                    @Override
                    public String load(@NonNull String key) throws Exception {
                        return key + "hello world";
                    }

                    @Override
                    public @NonNull Map<String, String> loadAll(@NonNull Iterable<? extends String> keys) throws Exception {
                        // loadAll返回的map里是不可以包含value为null的数据，否则将会报NullPointerException
                        // 无法加载的数据无需放在返回的map中，即只返回可以加载的数据
                        // 如果返回的map中含有keys中不包含的key， 那个该entry将被缓存， 但是不会出现在getAll返回的map中
                        // 例如keys为[1,2], 返回的map中keys为[1,4], 那么key为4的entry将会被缓存， 但是getAll返回的map中只有[1]
                        return null;
                    }
                });
        // get if present, or create, or null if exception
        String key = cache.get("key");

        // 针对每一个不存在的key进行单独的调用CacheLoader.load
        // 可以重写CacheLoader.loadAll以改变其行为
        // 针对从数据库中加载缓存， 一个一个加载太慢了， 可以重写loadAll一次性全部加载
        Map<String, String> all = cache.getAll(new ArrayList<String>());

    }

    // 异步手动
    @Test
    public void manualAsynchronous (){

        AsyncCache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10000)
                // 重写异步使用的线程池， 默认为ForkJoinPool.commonPool();
                .executor(Executors.newFixedThreadPool(5))
                .buildAsync();

        CompletableFuture<String> value = cache.getIfPresent("key");
        if (value.isDone()) {
            try {
                String s = value.get();
                value.thenApply(key -> key + "hello ").thenApply(key -> key + "world");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // get if present, or create asynchronously
        CompletableFuture<String> completableFuture = cache.get("key", new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s + "hello world";
            }
        });

        // 异步转同步
        cache.synchronous().getIfPresent("key");

    }

    // 异步自动加载
    @Test
    public void loadingAsynchronous (){

        AsyncLoadingCache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10000)
                // 异步封装了一段同步代码
                .buildAsync(new CacheLoader<String, String>() {
                    @Nullable
                    @Override
                    public String load(@NonNull String key) throws Exception {
                        return key + "hello world";
                    }
                });

        AsyncLoadingCache<String, String> cache1 = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10000)
                // 异步封装了一段异步代码， 异步的异步
                .buildAsync(new AsyncCacheLoader<String, String>() {
                    @Override
                    public @NonNull CompletableFuture<String> asyncLoad(@NonNull String key, @NonNull Executor executor) {
                        return null;
                    }
                });




    }



}
