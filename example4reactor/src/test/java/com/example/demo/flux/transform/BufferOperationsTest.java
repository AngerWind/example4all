package com.example.demo.flux.transform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/21
 * @description
 */
@SpringBootTest
public class BufferOperationsTest {

    @Test
    public void testBuffer() {
        // buffer的功能是: 接受到下游的request信号后, 给下游发送一个list数据
        Flux.range(0, 10)
            .buffer(3)
            .subscribe(System.out::println); // [0, 1, 2]  [3, 4, 5] [6, 7, 8] [9]
    }
}
