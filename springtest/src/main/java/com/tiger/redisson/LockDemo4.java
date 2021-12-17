package com.tiger.redisson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title LockDemo4
 * @date 2021/12/17 10:27
 * @description
 */

@Component
public class LockDemo4 {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String lockValue;

    public boolean lockVersion4() {
        String uuid = UUID.randomUUID().toString();

        if (redisTemplate.opsForValue().setIfAbsent("lock:consume", uuid)) {
            this.lockValue = uuid;
            renewKey(Thread.currentThread(), "lock:consume");

            return true;
        }

        return false;
    }

    public void unlockVersion4() {
        if (lockValue != null && lockValue.equals(redisTemplate.opsForValue().get("lock:consume"))) {
            System.out.println("我的锁我自己释放了");
            redisTemplate.delete("lock:consume");
        } else {
            System.out.println("不是我的锁我不释放");
        }
    }

    /**
     * 定时续费
     * @param thread
     * @param key
     */
    public void renewKey(Thread thread, String key) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (thread.isAlive() && redisTemplate.hasKey(key)) {
                    System.out.println("线程还在,给key续30秒");
                    redisTemplate.expire(key, 30, TimeUnit.SECONDS);
                } else {
                    System.out.println("线程已经不存在,终止定时任务");
                    throw new RuntimeException("终止定时任务");
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }
}

