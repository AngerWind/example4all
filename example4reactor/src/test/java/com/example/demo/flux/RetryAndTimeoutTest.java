package com.example.demo.flux;

import java.time.Duration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import reactor.core.publisher.Flux;
import org.junit.jupiter.api.Test;
/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/22
 * @description
 */
@SpringBootTest
public class RetryAndTimeoutTest {

    @Test
    void testRetry() throws InterruptedException {
        Flux.range(1, 4)
            .delayElements(Duration.ofSeconds(3))
            .timeout(Duration.ofSeconds(2)) // 超时的时候, 向上发送cancel信号, 向下发送error信号
            .retry(2) // 接收到error信号的时候, 重新整个流, 当超过重试次数之后, 会下error信号向下发送
            .log("retry")
            .onErrorReturn(2) // 接受到error信号的时候, 返回一个默认值
            .map(i-> i+"haha")
            .subscribe(v-> System.out.println("v = " + v));

        Thread.sleep(5000000);
    }

    @Test
    void testTimeout() {
        Flux.range(1, 5)
            .flatMap(v -> {
                System.out.println(v+" ajhah ");
                return Flux.just(v).delayElements(Duration.ofSeconds(v));
            })
            .timeout(Duration.ofSeconds(3))
            .subscribe(System.out::println);
    }
}
