package com.example.demo.flux;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/22
 * @description
 */
@SpringBootTest
public class ParallelTest {

    @Test
    void testParallel() {
        // todo paraller和subscribeOn, publishOn的区别
        Flux.range(1, 100)
            .buffer(10)// 每批10个数据
            .parallel(4)// 将1个流的数据轮询发送到4个流上
            .runOn(Schedulers.newParallel("parallel", 2))// 指定上面四个流的工作线程池
            .log()
            .subscribe();
    }
}
