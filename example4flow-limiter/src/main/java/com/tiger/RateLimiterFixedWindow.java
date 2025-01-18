package com.tiger;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口限流算法
 */
public class RateLimiterFixedWindow {
    // 阈值
    private final Integer limit;
    // 时间窗口（毫秒）
    private  final long windowSize;
    // 计数器
    private  AtomicInteger REQ_COUNT = new AtomicInteger();

    private  long START_TIME = System.currentTimeMillis();

    public RateLimiterFixedWindow(int limit, int windowSize) {
        this.limit = limit;
        this.windowSize = windowSize;
    }

    public RateLimiterFixedWindow() {
        this(2, 1000);
    }

    public synchronized boolean tryAcquire() {
        if ((System.currentTimeMillis() - START_TIME) > windowSize) {
            REQ_COUNT.set(0);
            START_TIME = System.currentTimeMillis();
        }
        return REQ_COUNT.incrementAndGet() <= limit;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(250);
            LocalTime now = LocalTime.now();
            if (!new RateLimiterFixedWindow().tryAcquire()) {
                System.out.println(now + " 被限流");
            } else {
                System.out.println(now + " 做点什么");
            }
        }
    }
}
