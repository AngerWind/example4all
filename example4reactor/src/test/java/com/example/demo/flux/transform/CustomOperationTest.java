package com.example.demo.flux.transform;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/21
 * @description
 */
@SpringBootTest
public class CustomOperationTest {

    @Test
    public void customTest() {
        Flux.range(1, 10)
            // 和map的区别就是, handle可以拿到sink, 可以调用他的error()和complete()发出结束和异常信号
            .handle((value, sink) -> {
                System.out.println("value:" + value);
                // do something, 查db, redis
                int next = value + 10;
                sink.next(next);
                // sink.error(new RuntimeException("error"));
                // sink.complete();
            } ).subscribe();
    }

}
