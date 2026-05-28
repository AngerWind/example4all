package com.tiger.im;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       ExecutorServiceFactory.java</p>
 * <p>@PackageName:     com.freddy.im</p>
 * <b>
 * <p>@Description:     线程池工厂，负责重连和心跳线程调度</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/05 05:12</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */

@Slf4j
public class ExecutorServiceFactory {

    private ExecutorService connectExecutor;// 一个线程池, 负责执行重连任务
    private ExecutorService heartBeatExecutor;// 一个线程池, 用于执行心跳任务

    /**
     * 初始化boss线程池
     */
    public synchronized void initBossLoopGroup() {
        initBossLoopGroup(1);
    }

    /**
     * 初始化boss线程池
     * 重载
     *
     * @param size 线程池大小
     */
    public synchronized void initBossLoopGroup(int size) {
        destroyBossLoopGroup();
        connectExecutor = Executors.newFixedThreadPool(size);
    }

    /**
     * 初始化work线程池
     */
    public synchronized void initWorkLoopGroup() {
        initWorkLoopGroup(1);
    }

    /**
     * 初始化work线程池
     * 重载
     *
     * @param size 线程池大小
     */
    public synchronized void initWorkLoopGroup(int size) {
        destroyWorkLoopGroup();
        heartBeatExecutor = Executors.newFixedThreadPool(size);
    }

    /**
     * 执行boss任务
     */
    public void execBossTask(Runnable r) {
        if (connectExecutor == null) {
            initBossLoopGroup();
        }
        connectExecutor.execute(r);
    }

    /**
     * 执行work任务
     */
    public void execWorkTask(Runnable r) {
        if (heartBeatExecutor == null) {
            initWorkLoopGroup();
        }
        heartBeatExecutor.execute(r);
    }

    /**
     * 释放boss线程池
     */
    public synchronized void destroyBossLoopGroup() {
        if (connectExecutor != null) {
            try {
                connectExecutor.shutdownNow();
            } catch (Throwable t) {
                log.trace("destroy boss loop group error, e: {}", t.getMessage());
            } finally {
                connectExecutor = null;
            }
        }
    }

    /**
     * 释放work线程池
     */
    public synchronized void destroyWorkLoopGroup() {
        if (heartBeatExecutor != null) {
            try {
                heartBeatExecutor.shutdownNow();
            } catch (Throwable t) {
                log.error("destroy work loop group error, e: {}", t.getMessage());
            } finally {
                heartBeatExecutor = null;
            }
        }
    }

    /**
     * 释放所有线程池
     */
    public synchronized void destroy() {
        destroyBossLoopGroup();
        destroyWorkLoopGroup();
    }
}
