package com.tiger.thread;

import org.junit.Test;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title ThreadGroupTest
 * @date 2021/8/25 14:51
 * @description
 */
public class ThreadGroupTest {

    @Test
    public void test(){

        ThreadGroup currentThreadGroup = Thread.currentThread().getThreadGroup();

        ThreadGroup group = new ThreadGroup("group1");
        ThreadGroup group2 = new ThreadGroup(group, "group2");


        Thread thread = new Thread(group2, () -> {
            System.out.println("hello world");
        }, "thread1");

        group2.list();

        thread.start();

        System.out.println("ha");
    }

}
