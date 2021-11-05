package com.tiger.juc;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title ThreadPoolExecutorTest
 * @date 2021/7/15 17:00
 * @description
 */
public class ThreadPoolExecutorTest {

    static class SimpleThreadFactory implements ThreadFactory{
        
        private static final AtomicInteger COUNTER = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("simple-thread-" + COUNTER.incrementAndGet());
            return thread;
        }
    }

    @Test
    public void test(){
        int corePoolSize = 1;
        int maximumPoolSize = 4;
        long keepAliveTime = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(2);
        ThreadFactory threadFactory = new SimpleThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                unit, queue, threadFactory, handler);

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                Future future = threadPoolExecutor.submit(() -> {
                    System.out.println("Thread: " + Thread.currentThread().getName() + "start to sleep, time is " + System.currentTimeMillis());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread: " + Thread.currentThread().getName() + "weak up, time is " + System.currentTimeMillis());
                    return null;
                });
                futures.add(future);
            }).start();

        }
        threadPoolExecutor.shutdownNow();
        // while (true) {
        //     for (Future<?> future : futures) {
        //         try {
        //             future.get();
        //         } catch (InterruptedException | ExecutionException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // }

    }
}
