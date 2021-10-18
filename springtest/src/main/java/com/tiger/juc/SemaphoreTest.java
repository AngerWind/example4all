package com.tiger.juc;

import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.Semaphore;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SemaphoreTest
 * @date 2021/10/14 15:50
 * @description
 */
public class SemaphoreTest {

    @Test
    @SneakyThrows
    public void test() {

        Semaphore sem = new Semaphore(0);

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                sem.acquireUninterruptibly();
            }).start();
        }

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                sem.release();
            }).start();
        }
    }
}
