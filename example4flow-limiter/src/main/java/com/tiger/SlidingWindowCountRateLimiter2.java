package com.tiger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 将一个大的窗口分为多个小的窗口, 然后分别统计落在每个小窗口上的请求,  小窗口越多越准确, 同时占用内存也越多
 */
public class SlidingWindowCountRateLimiter2 {

    public long TIME_LIMIT; // 总的窗口大小, 毫秒

    public long RATE_LIMIT; // 阈值

    public long NUMBER_OF_TIME_WINDOWS; // 小窗口的个数

    private static final Logger logger = LoggerFactory.getLogger(SlidingWindowCountRateLimiter2.class);
    private Map<String, Map<Long, AtomicLong>> UserTimeMap = new ConcurrentHashMap<>();

    public SlidingWindowCountRateLimiter2(long limit, long numberOfWindow, long windowSize) {
        this.TIME_LIMIT = windowSize;
        this.NUMBER_OF_TIME_WINDOWS = numberOfWindow;
        this.RATE_LIMIT = limit;
    }

    public synchronized boolean tryAcquire(String username) {
        // 计算小窗口的开始时间, 作为小窗口的编号
        long windowStart = Instant.now().toEpochMilli() / (TIME_LIMIT / NUMBER_OF_TIME_WINDOWS);

        // Reference for individual user count data
        Map<Long, AtomicLong> individualUserHits;

        // Case 1: User specific Entry does not exist
        if (!UserTimeMap.containsKey(username)) {
            individualUserHits = new TreeMap<>();
            individualUserHits.put(windowStart, new AtomicLong(1L));
            UserTimeMap.put(username, individualUserHits);
            return true;
        }
        // Case 2: User specific Entry exists, Time Window for the user may or may not exist in the map
        else {
            individualUserHits = UserTimeMap.get(username);
            return checkAndAddForExistingUsers(username, windowStart, individualUserHits);
        }
    }

    public boolean checkAndAddForExistingUsers(String username, long currentTimeWindow,
        Map<Long, AtomicLong> timeWindowVSCountMap) {

        // 移除过期的小窗口数据, 并返回滑动窗口内的请求次数
        long countInOverallTime = removeOldEntriesForUser(currentTimeWindow, timeWindowVSCountMap);

        // 请求次数小于阈值
        if (countInOverallTime < RATE_LIMIT) {
            // Handle new time windows
            long newCount = timeWindowVSCountMap.getOrDefault(currentTimeWindow, new AtomicLong(0)).longValue() + 1;
            timeWindowVSCountMap.put(currentTimeWindow, new AtomicLong(newCount));
            return true;
        }
        return false;
    }

    // 移除旧的entry, 并且返回窗口内的请求的次数
    public long removeOldEntriesForUser(long currentTimeWindow, Map<Long, AtomicLong> timeWindowVSCountMap) {
        long overallCount = 0L;
        Iterator<Entry<Long, AtomicLong>> iterator = timeWindowVSCountMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Long, AtomicLong> entry = iterator.next();
            long timeWindow = entry.getKey();
            // 当前时间窗口的编号 - entry的时间窗口编号 >= 窗口数量, 说明过期了
            if ((currentTimeWindow - timeWindow) >= NUMBER_OF_TIME_WINDOWS)
                iterator.remove();
            else
                // 计算窗口内的请求次数
                overallCount += entry.getValue().longValue();
        }
        return overallCount;
    }
}