package com.tiger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 漏桶算法:
 * <p>
 * 请求就像水一样以任意速度注入漏桶，而桶会按照固定的速率将水漏掉, 当注入速度持续大于漏出的速度时，漏桶会变满，此时新进入的请求将会被丢弃。 限流和整形是漏桶算法的两个核心能力。
 */
public class LeakyBucketRateLimiter {

    // 桶的容量
    private final int capacity;
    // 漏出速率
    private final int permitsPerSecond;
    // 剩余水量, 即还没有处理的请求的数量
    private long leftWater;
    // 上次注入时间, 即上次请求的时间
    private long timeStamp = System.currentTimeMillis();

    public LeakyBucketRateLimiter(int permitsPerSecond, int capacity) {
        this.capacity = capacity;
        this.permitsPerSecond = permitsPerSecond;
    }

    /**
     * 如果单单只是这个方法, 那么他只能判断漏桶满了的时候丢弃掉请求, 而不能实现漏桶恒定的输出 想要以恒定的速率漏出流量，通常还应配合一个FIFO队列来实现，
     * 当tryAcquire返回true时，将请求入队，然后再以固定频率从队列中取出请求进行处理。
     */
    public synchronized boolean tryAcquire() {

        long now = System.currentTimeMillis();
        long timeGap = (now - timeStamp) / 1000; // 计算上一次请求到现在的时间间隔, 秒
        leftWater = Math.max(0, leftWater - timeGap * permitsPerSecond); // 计算剩余水量, 即剩余还没有处理的请求的数量
        timeStamp = now;

        // 如果未满，则放行；否则限流
        if (leftWater < capacity) {
            leftWater += 1; // 请求数量+1
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        ExecutorService singleThread = Executors.newSingleThreadExecutor();

        LeakyBucketRateLimiter rateLimiter = new LeakyBucketRateLimiter(20, 20);
        // 存储流量的队列
        Queue<Integer> queue = new LinkedList<>();
        // 模拟请求 不确定速率注水
        singleThread.execute(() -> {
            int count = 0;
            while (true) {
                count++;
                boolean flag = rateLimiter.tryAcquire();
                if (flag) {
                    queue.offer(count);
                    System.out.println(count + "--------流量被放行--------");
                } else {
                    System.out.println(count + "流量被限制");
                }
                try {
                    Thread.sleep((long)(Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 模拟处理请求 固定速率漏水
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (!queue.isEmpty()) {
                System.out.println(queue.poll() + "被处理");
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

        // 保证主线程不会退出
        while (true) {
            Thread.sleep(10000);
        }

    }
}