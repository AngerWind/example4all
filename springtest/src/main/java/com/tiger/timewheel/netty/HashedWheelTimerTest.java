package com.tiger.timewheel.netty;

import java.util.concurrent.TimeUnit;

import io.netty.util.HashedWheelTimer;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2023/12/26
 * @description
 */
public class HashedWheelTimerTest {

    public static void main(String[] args) {

        HashedWheelTimer timer = new HashedWheelTimer(1, TimeUnit.SECONDS, 8);
        // add a new timeout
        timer.newTimeout(timeout -> {
        }, 5, TimeUnit.SECONDS);

        System.out.println("hello world");
    }

}
