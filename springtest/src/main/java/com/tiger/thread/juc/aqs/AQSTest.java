package com.tiger.thread.juc.aqs;

import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title AQSTest
 * @date 2021/9/7 17:40
 * @description
 */
public class AQSTest {


    @Test
    @SneakyThrows
    public  void test() {

        ReentrantLock lock = new ReentrantLock(true);
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                lock.lock();
                lock.unlock();
            }).start();
        }

    }

    @Test
    @SneakyThrows
    public  void test1() {

        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        new Thread(() -> {
            writeLock.lock();
            writeLock.unlock();
        }).start();
        new Thread(() -> {
            readLock.lock();
            readLock.unlock();
        }).start();
        new Thread(() -> {
            readLock.lock();
            readLock.unlock();
        }).start();

    }
}
