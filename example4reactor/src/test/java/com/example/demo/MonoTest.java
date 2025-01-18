package com.example.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/22
 * @description
 */
public class MonoTest {

    @Test
    public void testMono() {
        Mono<String> mono = Mono.just("Hello, Mono!");
        mono.subscribe(System.out::println); // 订阅, 当数据到来的时候, 执行回调
    }

    @Test
    public void testMono1() {
        Mono<String> mono = Mono.just("Hello, Mono!");
        String s = mono.block(); // 订阅, 等待数据到来并返回
    }

    @Test
    public void defaultIfEmpty() {
        // 如果Mono为空, 给一个默认值
        Object haha = Mono.empty()
            .defaultIfEmpty("HAHA")
            .block();
        System.out.println(haha);

        // 如果Mono为空, 调用switchIfEmpty的参数来获取一个默认值
        Object haha1 = Mono.empty()
            .switchIfEmpty(Mono.just("haha"))
            .block();
        System.out.println(haha1);
    }
}
