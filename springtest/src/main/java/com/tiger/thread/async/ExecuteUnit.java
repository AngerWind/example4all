package com.tiger.thread.async;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title ExecuteUnit
 * @date 2021/8/25 13:52
 * @description
 */

import java.util.concurrent.TimeUnit;

/**
 * 执行单元.
 *
 * @param <I>
 *            入参类型
 * @param <O>
 *            出参类型
 */
public interface ExecuteUnit<I, O> {

    /**
     * 执行任务.
     *
     * @param input
     *            输入待处理数据
     * @return 返回处理结果
     * @throws Exception
     *             执行期异常
     */
    O execute(I input) throws Exception;

    /**
     * 设置Future.get()的超时时间
     *
     * @return 超时时间
     */
    default long getTimeOut() {
        return 0L;
    }

    /**
     * 设置Future.get()的超时时间单位
     *
     * @return 超时时间的时间单位
     */
    default TimeUnit getTimeUnit() {
        return TimeUnit.SECONDS;
    }
}

