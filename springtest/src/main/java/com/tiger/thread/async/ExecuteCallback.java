package com.tiger.thread.async;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title ExecuteCallback
 * @date 2021/8/25 13:54
 * @description
 */
public interface ExecuteCallback {

    /**
     * 执行成功的后续逻辑
     *
     * @param t
     *            任务结果
     * @param <T>
     *            结果的类型
     */
    <T> void onSuccess(T t);

    /**
     * 执行失败的处理逻辑
     *
     * @param throwable
     *            失败的异常信息
     */
    void onFailure(Throwable throwable);

    /**
     * 执行失败的处理逻辑
     *
     * @param allFutures
     *            所有的futures
     * @param throwable
     *            失败的异常信息
     */
    default <T> void onFailure(ListenableFuture<T> allFutures, Throwable throwable) {
        onFailure(throwable);
    }
}

