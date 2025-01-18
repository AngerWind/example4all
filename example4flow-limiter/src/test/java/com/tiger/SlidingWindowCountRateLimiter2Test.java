package com.tiger;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/13
 * @description
 */
class SlidingWindowCountRateLimiter2Test {

    @Test
    public void testTryAcquire() {
        SlidingWindowCountRateLimiter2 limiter = new SlidingWindowCountRateLimiter2(2, 10, 1000);
        for (int i = 0; i < 10; i++) {
            limiter.tryAcquire("aaa");
        }
    }

}