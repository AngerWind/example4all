package com.example.demo;

import com.example.demo.FlowApiTests.MySubscriber;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

@SpringBootTest
public class FluxTests {

    /**
     * Flux表示有n个元素和1个信号的流 Mono表示有0/1个元素和1个信号的流
     *
     * 不管流里面有没有元素, 他都会有一个信号 可以是完成信号, 或者错误信号
     */







    @Test
    void signal() {
        // 流中没有元素, 但是有一个信号
        // 如果没有订阅者, 那么这个结束信号永远不会发出
        Flux<Object> flow = Flux.empty().doOnComplete(() -> {
            System.out.println("流已经结束");
        });
        // 在订阅之前, flow不会发出结束信号
        // 只有订阅了之后, 才会发出结束信号
        flow.subscribe(System.out::println);
        flow.subscribe(System.out::println);
    }



}
