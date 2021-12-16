package com.tiger.redisson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SimpleRedisLock
 * @date 2021/12/15 15:55
 * @description
 */
public class SimpleRedisLock  {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ThreadLocal<String> uuidLocal = new ThreadLocal<>();

    private ThreadLocal<Integer> lockCount = new ThreadLocal<>();

    private String lockKey;

    private boolean lock = false;

    private Thread exclusiveOwnerThread;

    public SimpleRedisLock(String key) {
        this.lockKey = key;
    }

    public boolean tryLock(long timeout, TimeUnit unit) {
        if (!lock) {
            String uuid = UUID.randomUUID().toString();
            boolean isLocked = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, uuid, timeout, unit);
            if (isLocked) {
                exclusiveOwnerThread = Thread.currentThread();
                lock = true;
                uuidLocal.set(uuid);
            }
            return false;
        } else if (exclusiveOwnerThread == Thread.currentThread()) {
            lockCount.set(lockCount.get() + 1);
        }
        return false;

        // Boolean isLocked = false;
        // if (threadLocal.get() == null) {
        //     String uuid = UUID.randomUUID().toString();
        //     threadLocal.set(uuid);
        //     isLocked = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, uuid, timeout, unit);
        //     // 尝试获取锁失败，则自旋获取锁直至成功
        //     // if (!isLocked) {
        //     //     for (;;) {
        //     //         isLocked = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, uuid, timeout, unit);
        //     //         if (isLocked) {
        //     //             break;
        //     //         }
        //     //     }
        //     // }
        //     // 启动新的线程来定期检查当前线程是否执行完成，并更新过期时间
        //     new Thread(new UpdateLockTimeoutTask(uuid, stringRedisTemplate, lockKey)).start();
        // } else {
        //     isLocked = true;
        // }
        // // 重入次数加1
        // if (isLocked) {
        //     Integer count = threadLocalInteger.get() == null ? 0 :threadLocalInteger.get();
        //     threadLocalInteger.set(count++);
        // }
        //
        // return isLocked;
    }

    public void releaseLock(String key) {
        // 判断当前线程所对应的uuid是否与Redis对应的uuid相同，再执行删除锁操作
        if (threadLocal.get().equals(stringRedisTemplate.opsForValue().get(key))) {
            Integer count = threadLocalInteger.get();
            // 计数器减为0时才能释放锁
            if (count == null || --count <= 0) {
                stringRedisTemplate.delete(key);
                // 获取更新锁超时时间的线程并中断
                long threadId = stringRedisTemplate.opsForValue().get(uuid);
                Thread updateLockTimeoutThread = ThreadUtils.getThreadByThreadId(threadId);
                if (updateLockTimeoutThread != null) {
                    // 中断更新锁超时时间的线程
                    updateLockTimeoutThread.interrupt();
                    stringRedisTemplate.delete(uuid);
                }
            }
        }
    }

    public class UpdateLockTimeoutTask implements Runnable {

        private String uuid;
        private String key;
        private StringRedisTemplate stringRedisTemplate;

        public UpdateLockTimeoutTask(String uuid, StringRedisTemplate stringRedisTemplate, String key) {
            this.uuid = uuid;
            this.key = key;
            this.stringRedisTemplate = stringRedisTemplate;
        }

        @Override
        public void run() {
            // 将以uuid为Key，当前线程Id为Value的键值对保存到Redis中
            stringRedisTemplate.opsForValue().set(uuid, Thread.currentThread().getId());
            // 定期更新锁的过期时间
            while (true) {
                stringRedisTemplate.expire(key, 10, TimeUnit.SECONDS);
                try{
                    // 每隔3秒执行一次
                    Thread.sleep(10000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static class ThreadUtils {

        // 根据线程 id 获取线程句柄
        public static Thread getThreadByThreadId(long threadId) {
            ThreadGroup group = Thread.currentThread().getThreadGroup();
            while(group != null){
                Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
                int count = group.enumerate(threads, true);
                for (int i = 0; i < count; i++){
                    if (threadId == threads[i].getId()) {
                        return threads[i];
                    }
                }
            }
        }

    }

}
