package com.tiger.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.threadpool.TtlExecutors;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/15
 * @description
 */
public class InheritableThreadLocalTest {

    @Test
    public void test() {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("hello");

        InheritableThreadLocal<Object> inheritableThreadLocal = new InheritableThreadLocal<>();
        inheritableThreadLocal.set("world");

        Thread thread = new Thread(() -> {
            System.out.println(threadLocal.get()); // null
            System.out.println(inheritableThreadLocal.get()); // world
        });
        thread.start();
    }

    @Test
    public void transmittableThreadLocalTest() {

        TransmittableThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();

        // 在主线程中设置TransmittableThreadLocal的值
        threadLocal.set("Hello, World!");

        // 在新线程中获取TransmittableThreadLocal的值
        Thread thread = new Thread(() -> {
            String value = threadLocal.get();
            System.out.println("TransmittableThreadLocal value in new thread: " + value);
        });
        thread.start();
    }

    @Test
    public void test2() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();
        // 在主线程中设置TransmittableThreadLocal的值
        context.set("Hello, World!");

        // 在线程池中执行任务
        // TtlRunnable.get()会对当前task进行包装, 保存当前线程的TransmittableThreadLocal
        // 然后在线程池执行任务的开始, 把TransmittableThreadLocal设置到执行线程中
        executorService.execute(TtlRunnable.get(() -> {
            String value = context.get();
            System.out.println(value);
        }));

        // 等待任务执行完成
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }

    @Test
    public void test3() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // 对线程池进行包装
        executorService = TtlExecutors.getTtlExecutorService(executorService);
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();
        // 在父线程中设置
        context.set("value-set-in-parent");

        executorService.submit(() -> {
            System.out.println(context.get()); // value-set-in-parent
        });
    }

}
