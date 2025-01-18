package com.tiger;

import com.google.common.util.concurrent.RateLimiter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GuavaRateLimiter {

    public static void main(String[] args) throws InterruptedException {
        // Google 的 Java 开发工具包 Guava 中的限流工具类 RateLimiter 就是令牌桶的一个实现，
        // 日常开发中我们也不会手动实现了，这里直接使用 RateLimiter 进行测试。

        // 每秒限制两个请求, 有点像固定窗口限流
        RateLimiter rateLimiter = RateLimiter.create(2);
        for (int i = 0; i < 10; i++) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
            System.out.println(time + ":" + rateLimiter.tryAcquire());
            Thread.sleep(250);
        }

    }
}
