package com.tiger.thread.juc;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title FutureTaskTest
 * @date 2021/7/14 19:51
 * @description
 */
public class FutureTaskTest {

    @Test
    public void test(){
        Callable<Void> callable = () -> null;
        FutureTask<Void> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();
    }


}
