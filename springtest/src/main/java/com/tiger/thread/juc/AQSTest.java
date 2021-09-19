package com.tiger.thread.juc;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title AQSTest
 * @date 2021/9/7 17:40
 * @description
 */
public class AQSTest {


    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock(true);
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                lock.lock();

                lock.unlock();
            }).start();
        }
    }
}
