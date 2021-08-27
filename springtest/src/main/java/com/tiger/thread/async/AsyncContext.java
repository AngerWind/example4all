package com.tiger.thread.async;

import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步执行器上下文, 全局共享
 */

@Slf4j
public class AsyncContext {

    private volatile static ExecutorEngine executorEngine;
    private volatile static ListeningScheduledExecutorService scheduledExecutor;

    /**
     * 注册线程池的执行引擎
     *
     * @param maxWorkerNumber
     *            最大线程数
     */
    public static ExecutorEngine registerExecutorEngine(final int maxWorkerNumber) {
        if (executorEngine != null) {
            log.warn("ExecutorEngine is already init.");
            return executorEngine;
        }
        executorEngine = new ExecutorEngine(maxWorkerNumber);
        log.info("Init ExecutorEngine maxWorkerNumber = {} .", maxWorkerNumber);
        return executorEngine;
    }

    public static ListeningScheduledExecutorService registerScheduledExecutorEngine(final int maxWorkerNumber) {
        if (scheduledExecutor != null) {
            log.warn("ScheduledExecutorEngine is already init.");
            return scheduledExecutor;
        }
        scheduledExecutor = MoreExecutors.listeningDecorator(Executors.newScheduledThreadPool(maxWorkerNumber,
                new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ScheduledExecutorEngine-%d").build()));
        log.info("Init ScheduledExecutorEngine maxWorkerNumber = {} .", maxWorkerNumber);
        return scheduledExecutor;
    }

    /**
     * 获取定时执行器
     */
    public static ListeningScheduledExecutorService getScheduledExecutor() {
        if (scheduledExecutor != null) {
            return scheduledExecutor;
        }
        // 线程池维护线程的最少数量
        final int executorMinIdleSize = Runtime.getRuntime().availableProcessors();
        // 线程池维护线程的最大数量(CPU个数*4)
        final int executorMaxSize = executorMinIdleSize * 4;
        return registerScheduledExecutorEngine(executorMaxSize);
    }

    /**
     * 获取线程池的执行引擎
     */
    public static ExecutorEngine getExecutorEngine() {
        if (executorEngine != null) {
            return executorEngine;
        }
        // 线程池维护线程的最少数量
        final int executorMinIdleSize = Runtime.getRuntime().availableProcessors();
        // 线程池维护线程的最大数量(CPU个数*20)
        final int executorMaxSize = executorMinIdleSize * 20;
        return registerExecutorEngine(executorMaxSize);
    }
}
