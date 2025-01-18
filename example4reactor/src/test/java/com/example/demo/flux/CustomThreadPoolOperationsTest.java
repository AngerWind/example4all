package com.example.demo.flux;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/21
 * @description
 */
@SpringBootTest
public class CustomThreadPoolOperationsTest {

    @Test
    public void test() throws InterruptedException {
        // 创建一个线程池, 该线程池在当前线程中执行
        Schedulers.immediate();

        // 创建一个线程池, 其中只有一个线程
        Schedulers.single();

        // 创建一个线程池, 最大线程数为2, 队列长度为3, ttl为100s, worker是守护线程
        Schedulers.newBoundedElastic(2, 3, "my-bounded-elastic-scheduler", 100, true);

        // 创建一个线程池, 使用默认参数
        // 等效于Schedulers.newBoundedElastic(10*cpu个数, 10000, "boundedElastic", 60, true);
        Schedulers.boundedElastic();

        // 使用自定义的线程池
        Schedulers.fromExecutor(Executors.newFixedThreadPool(2));


        // todo 看不懂publishOn和subscisrbeOn的工作原理
        new Thread(() -> {
            Flux.range(1, 10)
                .log("range")
                .publishOn(Schedulers.single())
                .log("publishOn")
                .doOnNext(System.out::println)
                .log("doOnNext")
                .subscribeOn(Schedulers.fromExecutor(Executors.newFixedThreadPool(1)))
                .log("subscribeOn")
                .subscribe();
        }, "startThread").start();

        Thread.sleep(1000000);


    }
}
