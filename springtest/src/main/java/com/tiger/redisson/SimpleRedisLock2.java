package com.tiger.redisson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SimpleRedisLock2
 * @date 2021/12/16 10:18
 * @description
 */
@Component
public class SimpleRedisLock2 {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String lockValue;

    private ThreadLocal<String> keyMap = new ThreadLocal<>();

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;


    public boolean tryLock(String key) {
        keyMap.set(key);
        String uuid = UUID.randomUUID().toString();

        if (redisTemplate.opsForValue().setIfAbsent(key, uuid)) {
            this.lockValue = uuid;
            renewKey(Thread.currentThread(), key);

            return true;
        }

        return false;
    }

    public boolean tryLock(String key, long time) {
        keyMap.set(key);
        String uuid = UUID.randomUUID().toString();
        Instant endTime = Instant.now().plusMillis(time);

        while(Instant.now().getEpochSecond() < endTime.getEpochSecond()) {

            if (redisTemplate.opsForValue().setIfAbsent(key, uuid)) {
                this.lockValue = uuid;
                renewKey(Thread.currentThread(), key);

                return true;
            }
        }
        keyMap.remove();
        return false;
    }

    public void lock(String key) {
        keyMap.set(key);
        String uuid = UUID.randomUUID().toString();

        while (true) {
            if (redisTemplate.opsForValue().setIfAbsent(key, uuid)) {
                this.lockValue = uuid;
                renewKey(Thread.currentThread(), key);

                break;
            }
        }
    }

    public void unlock() {
        String key = keyMap.get();
        System.out.println(lockValue);
        System.out.println(redisTemplate.opsForValue().get(key));

        if (lockValue != null && lockValue.equals(redisTemplate.opsForValue().get(key))) {
            System.out.println(LocalDateTime.now() + " 我的锁我自己释放了");
            redisTemplate.delete(key);
            keyMap.remove();
        } else {
            System.out.println(LocalDateTime.now() + " 不是我的锁我不释放");
        }
    }

    /**
     * 定时续费
     * @param thread
     * @param key
     */
    public void renewKey(Thread thread, String key) {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (thread.isAlive() && redisTemplate.hasKey(key)) {
                System.out.println(LocalDateTime.now() + " 线程还在,给key续30秒");
                redisTemplate.expire(key, 30, TimeUnit.SECONDS);
            } else {
                System.out.println("线程已经不存在,终止定时任务");
                throw new RuntimeException("终止定时任务");
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

}
