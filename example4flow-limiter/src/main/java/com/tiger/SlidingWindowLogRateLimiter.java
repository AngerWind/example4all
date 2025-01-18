package com.tiger;

import java.time.Clock;
import java.time.LocalTime;
import java.util.*;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/12
 * @description
 */

/**
 * 来源: https://www.rdiachenko.com/posts/arch/rate-limiting/sliding-window-algorithm/
 * 滑动窗口日志算法
 * 原理: 记录每一次通过的请求的时间, 然后比较当前时间和前limit次的请求时间, 是否超过了一个timeWindow大小
 */
public class SlidingWindowLogRateLimiter {

    private final int maxCount; // 阈值
    private final long windowLengthMillis; // 窗口大小
    private final Deque<Long> slidingWindow = new LinkedList<>(); // 保存窗口的所有请求时间

    SlidingWindowLogRateLimiter(int maxCount, long windowLengthMillis, Clock clock) {
        this.maxCount = maxCount;
        this.windowLengthMillis = windowLengthMillis;
    }

    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // 移除掉过期的时间
        while (!slidingWindow.isEmpty()
                && slidingWindow.getFirst() + windowLengthMillis < now) {
            slidingWindow.removeFirst();
        }

        // 请求是否达到阈值
        if (slidingWindow.size() >= maxCount) {
            return false;
        } else {
            slidingWindow.addLast(now);
            return true;
        }
    }
}