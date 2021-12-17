package com.tiger.redisson.lock;



public interface LockRegistry {
    /**
     * 创建锁
     * @param key
     * @return
     */
    Lock obtain(String key);
}
