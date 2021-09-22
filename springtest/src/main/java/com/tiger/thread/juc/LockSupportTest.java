package com.tiger.thread.juc;

import lombok.SneakyThrows;
import org.junit.Test;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title LockSupportTest
 * @date 2021/8/25 16:11
 * @description
 */
public class LockSupportTest {

    @SneakyThrows
    @Test
    public void test() {
        Object lock = new Object();

        Runnable doSomething = () -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + " in");
                LockSupport.park();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("被中断了");
                }
                System.out.println("继续执行");
            }
        };

        Thread thread = new Thread(doSomething);
        thread.start();
        Thread.sleep(1000);

        Thread thread2 = new Thread(doSomething);
        thread2.start();
        Thread.sleep(1000);

        thread.interrupt();
        LockSupport.unpark(thread2);

        System.out.println("main end...");
    }

    static class FIFOMutex {
        private final AtomicBoolean locked = new AtomicBoolean(false);
        private final Queue<Thread> waiters
                = new ConcurrentLinkedQueue<Thread>();

        public void lock() {
            boolean wasInterrupted = false;
            Thread current = Thread.currentThread();
            waiters.add(current);

            // Block while not first in queue or cannot acquire lock
            while (waiters.peek() != current ||
                    !locked.compareAndSet(false, true)) {
                LockSupport.park(this);
                if (Thread.interrupted()) // ignore interrupts while waiting
                    wasInterrupted = true;
            }

            waiters.remove();
            if (wasInterrupted)          // reassert interrupt status on exit
                current.interrupt();
        }

        public void lock2() {
            boolean wasInterrupted = false;
            Thread current = Thread.currentThread();
            waiters.add(current);

            // Block while not first in queue or cannot acquire lock
            while (waiters.peek() != current ) {
                if (!locked.compareAndSet(false, true))
                LockSupport.park(this);
                if (Thread.interrupted()) // ignore interrupts while waiting
                    wasInterrupted = true;
            }

            waiters.remove();
            if (wasInterrupted)          // reassert interrupt status on exit
                current.interrupt();
        }

        public void unlock() {
            locked.set(false);
            LockSupport.unpark(waiters.peek());
        }
    }

    @Test
    public void test2(){
        FIFOMutex fifoMutex = new FIFOMutex();

        Runnable target;
        Thread t1 = new Thread(()->{
            fifoMutex.lock();
        });

        Thread t2 = new Thread(()->{
            fifoMutex.lock();
        });
        t1.start();
        t2.start();

        fifoMutex.unlock();
    }


    @Test
    public void test1(){
        Thread thread = new Thread(() -> {
            System.out.println();
            LockSupport.park(this);
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("interrupt");
            } else {
                System.out.println("not");
            }
        });
        thread.start();
        // 被park的线程会被interrupte中断
        thread.interrupt();

        System.out.println("he");
    }
}
