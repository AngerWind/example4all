package com.tiger.redisson.lock;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Lock
 * @date 2021/12/17 10:36
 * @description
 */

public interface Lock {
    /**
     * 尝试加锁，如果获取不到，就阻塞
     */
    void lock();

    /**
     * 尝试加锁，如果获取到返回true,如获取不到返回false
     * @return
     */
    boolean tryLock();

    /**
     * 尝试加锁，如果获取指定时间内没有获取到返回true,如获取不到返回false
     * @param time
     * @return
     */
    boolean tryLock(long time);

    /**
     * 释放锁
     */
    void unlock();
}
