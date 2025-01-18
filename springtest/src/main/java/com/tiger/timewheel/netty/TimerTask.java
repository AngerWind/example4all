package com.tiger.timewheel.netty;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2023/12/27
 * @description
 */


import java.util.concurrent.TimeUnit;

/**
 * A task which is executed after the delay specified with
 * {@link Timer#newTimeout(TimerTask, long, TimeUnit)}.
 */
public interface TimerTask {

    /**
     * Executed after the delay specified with
     * {@link Timer#newTimeout(TimerTask, long, TimeUnit)}.
     *
     * @param timeout a handle which is associated with this task
     */
    void run(Timeout timeout) throws Exception;
}
