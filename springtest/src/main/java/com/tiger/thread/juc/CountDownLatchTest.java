package com.tiger.thread.juc;

import com.tiger.thread.TerminationTest;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title CountDownLatchTest
 * @date 2021/10/13 16:44
 * @description
 */
public class CountDownLatchTest {

    @Test
    @SneakyThrows
    public void test(){
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }
}
