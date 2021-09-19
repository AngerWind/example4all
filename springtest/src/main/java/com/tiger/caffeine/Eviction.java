package com.tiger.caffeine;

import com.github.benmanes.caffeine.cache.*;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Shen
 * @version v1.0
 * @date 2021/8/31 22:09
 */
public class Eviction {

    /*
    Caffeine 提供三种过期策略， 基于容量， 时间， 引用类型
     */

    @Test
    public void baseOnSize() {

        // 固定大小下剔除最不常用的缓存
        LoadingCache<String, String> graphs = Caffeine.newBuilder()
                // 如果缓存的条目数量不应该超过某个值，那么可以使用Caffeine.maximumSize(long)。
                // 如果超过这个值，则会剔除很久没有被访问过或者不经常使用的那个条目。
                // 详细的过期算法参考：https://github.com/ben-manes/caffeine/wiki/Efficiency
                .maximumSize(10_000)
                .build(new CacheLoader<String, String>() {
                    @Nullable
                    @Override
                    public String load(@NonNull String key) throws Exception {
                        return null;
                    }
                });

        // 固定大小下剔除权重最小的缓存
        LoadingCache<String, String> graph = Caffeine.newBuilder()
                .maximumWeight(10_000)
                // 如果，不同的条目有不同的权重值的话，那么你可以用Caffeine.weigher(Weigher)来指定一个权重函数，
                // 并且使用Caffeine.maximumWeight(long)来设定最大的权重值。
                // 权重仅在缓存被创建和更新的时候计算， 在那之后就是静态的了
                .maximumSize(10000)
                .weigher(new Weigher<String, String>() {
                    @Override
                    public @NonNegative int weigh(@NonNull String key, @NonNull String value) {
                        return 0;
                    }
                })
                .build(new CacheLoader<String, String>() {
                    @Nullable
                    @Override
                    public String load(@NonNull String key) throws Exception {
                        return null;
                    }
                });
    }

    @Test
    public void baseOnTime() {

        // 更新，修改，创建缓存后固定时间过期
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build(this::generateValue);
        // 更新，修改，创建缓存后固定时间过期
        LoadingCache<String, String> cache1 = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(this::generateValue);

        /*
        基于不同的过期驱逐策略
        如果表示永久不过期， 应该返回Long#MAX_VALUE
        时间单位都是纳秒，包括返回的时间
         */
        LoadingCache<String, String> graphs = Caffeine.newBuilder()
                .expireAfter(new Expiry<String, String>() {
                    // 创建之后多久过期
                    @Override
                    public long expireAfterCreate(String key, String graph, long currentTime) {
                        return 0;
                    }

                    // 更新之后多久过期
                    @Override
                    public long expireAfterUpdate(String key, String graph, long currentTime, long currentDuration) {
                        return 0;
                    }

                    // 读取之后多久过期
                    @Override
                    public long expireAfterRead(String key, String graph, long currentTime, long currentDuration) {
                        return 0;
                    }
                })
                .build(this::generateValue);

        /**
         * 基于时间的过期策略的执行，都是在缓存的写和少量的读期间完成的（意思就是依赖缓存的活动，只要不读写，就不会执行过期策略）
         * （如果在）
         * 可以使用Caffeine.scheduler(Scheduler)来指定一个调度的线程
         * java9+ 可以使用Scheduler.systemScheduler()来利用专门的，系统范围的调度线程
         *
         * 测试时间过期策略时不必等待时间过期，可以使用Caffeine.ticker(Ticker)方法在缓存中指定时间源，而不必等待系统时钟
         *
         * todo scheduler和ticker的使用
         */
        LoadingCache<String, String> cache2 = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .scheduler(new Scheduler() {
                    @Override
                    public @NonNull Future<?> schedule(@NonNull Executor executor, @NonNull Runnable command,
                                                       @Positive long delay, @NonNull TimeUnit unit) {
                        return null;
                    }
                })
                .ticker(new Ticker() {
                    @Override
                    public long read() {
                        return 0;
                    }
                })
                .build(this::generateValue);

    }

    public void baseOnReferenceType() {
        /*
         基于引用类型, 使用weakKeys, weakValues, softValues会导致整个缓存使用==来判断是否相等而不是equals
         软引用在内存不足时回收
         弱引用在gc时回收
         */
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(10000)
                // 使用弱引用包装key
                .weakKeys()
                // 使用弱引用包装value
                .weakValues()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();

        Caffeine.newBuilder()
                .maximumSize(10000)
                // 使用软引用包装value
                .softValues()
                .expireAfterWrite(10, TimeUnit.MINUTES);
    }

    public String generateValue(String key) {
        // generate value and return
        return "";
    }

}
