package com.tiger;

/**
 * 来源: https://www.rdiachenko.com/posts/arch/rate-limiting/sliding-window-algorithm/ <p/>
 * 滑动窗口计数算法
 * <p>
 * 考虑这样一个场景：允许您每 2 秒发出 100 个请求。
 * 假设您在前 2 秒内发出了 100 个请求，然后在下一周期的前 400 毫秒（这是新固定窗口的 20%）内发出了 15 个请求。
 * 当前窗口经过 20%后，前一个窗口的计数将加权 80%，如图 3 所示。
 * <p>
 * <img src="img.png">
 * <p>
 * 因此，当前请求计数计算如下：
 * count = 100 * 0.8 + 15 = 95 requests
 * <p>
 * 通用的公式是: count = previousWindowCount * weight + currentWindowCount
 * 使用这个公式的优点是 只需要保存两个窗口的数据即可, 及其的省内存!!!!!!!
 * 缺点是, 他假定了前一个窗口中的请求是均匀到来的, 所以才可以使用这个公式, 但是实际上不是, 所以使用这个公式有一定的误差!!!!!
 */
public class SlidingWindowCountRateLimiter {

    private final int maxCount; // 最大请求次数
    private final long windowLengthMillis; // 总窗口大小

    private FixedWindow previousFixedWindow; // 上一个固定窗口
    private FixedWindow currentFixedWindow; // 当前的固定窗口

    SlidingWindowCountRateLimiter(int maxCount, long windowLengthMillis) {
        long now = System.currentTimeMillis();
        this.maxCount = maxCount;
        this.windowLengthMillis = windowLengthMillis;

        this.previousFixedWindow = new FixedWindow(now, 0);
        this.currentFixedWindow = new FixedWindow(now, 0);
    }

    boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // Transition to a new fixed window when the current one expires.
        if (currentFixedWindow.timestamp() + windowLengthMillis < now) {
            previousFixedWindow = currentFixedWindow;
            currentFixedWindow = new FixedWindow(now, 0);
        }
        if (previousFixedWindow.timestamp() + windowLengthMillis * 2 < now) {
            previousFixedWindow = new FixedWindow(now, 0);
        }


        // Weight calculation for the previous window.
        long slidingWindowStart = Math.max(0, now - windowLengthMillis); // 计算以当前时间的窗口的开始时间
        long previousFixedWindowEnd = previousFixedWindow.timestamp() + windowLengthMillis; // 计算上一个窗口的关闭时间
        // Weight of the previous window based on overlap with the sliding window.
        double previousFixedWindowWeight =
            Math.max(0, previousFixedWindowEnd - slidingWindowStart) / (double)windowLengthMillis;

        // Calculate total request count within the sliding window.
        int count = (int)(previousFixedWindow.count() * previousFixedWindowWeight + currentFixedWindow.count());

        // Check if the request count within the sliding window exceeds the limit.
        // If so, reject the request; otherwise, update the request count
        // in the current fixed window and allow the request.
        if (count >= maxCount) {
            return false;
        } else {
            currentFixedWindow = new FixedWindow(currentFixedWindow.timestamp(), currentFixedWindow.count() + 1);
            return true;
        }
    }

    // 固定窗口
    private record FixedWindow(long timestamp, int count) {
    }
}