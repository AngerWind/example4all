package com.example.demo.flux;

import java.time.Duration;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/22
 * @description
 */
@SpringBootTest
public class SinksTest {

    @Test
    void testUnicast() throws InterruptedException {
        Sinks.many(); // 创建一个发布者, 发送flux数据
        Sinks.one(); // 创建一个发布者, 发送Mono数据


        // unicast 单播, 只运行一个订阅者
        // broadcast 广播, 可以有多个订阅者
        // replay, 重放, 是否给后来的订阅者把之前的元素依然发给它；
        Sinks.many().multicast().onBackpressureBuffer(3);

        Many<Object> many = Sinks.many()
            .unicast()// 单播
            .onBackpressureBuffer(new LinkedBlockingQueue<>(3)); // 背压队列为3

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                many.tryEmitNext(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        // 生成flux并订阅
        // 单播, 只能有一个订阅者
        many.asFlux().subscribe(System.out::println);

        Thread.sleep(5000000);

    }

    @Test
    void testBroadcast() throws InterruptedException {

        Many<Object> many = Sinks.many()
            .multicast()// 单播
            .onBackpressureBuffer(3); // 背压队列为3

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                many.tryEmitNext(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        // 多播
        many.asFlux().subscribe(System.out::println);
        // 延迟一会儿, 再订阅, 模拟丢失数据
        Thread.sleep(5000);
        // 应为这个订阅者后订阅, 所以之前发的消息接不到了
        many.asFlux().subscribe(System.out::println);

        Thread.sleep(5000000);
    }

    @Test
    void testReplay() throws InterruptedException {

        Many<Object> many = Sinks.many()
            .replay() // 默认多播, 具有重放功能
            // .all() // 缓存所有元素, 一次性把缓存的元素发给订阅者
            // .all(2) // 缓存所有元素, 一次性发2个元素给订阅者
            .limit(3); // 最多缓存三个元素, 缓存满了, 丢弃最旧的元素, 订阅者来的时候, 会一次性全部给订阅者
            // .latest() // 只缓存最新的元素, 相当于limit(1)
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                many.tryEmitNext(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        // 多播
        many.asFlux().subscribe(System.out::println);
        // 延迟一会儿, 再订阅, 模拟丢失数据
        Thread.sleep(5000);
        // 应为这个订阅者后订阅, 所以之前发的消息接不到了
        many.asFlux().subscribe(System.out::println);

        Thread.sleep(5000000);
    }

    @Test
    void testCache() throws InterruptedException {
        Flux<Integer> flux = Flux.range(1, 100)
            .delayElements(Duration.ofMillis(500))
            .cache(3); // 不加cache默认缓存所有, 即直接具有replay功能
        flux.subscribe();

        // sleep 7s, 会错过14个数据, 但是应为缓存了3个所以只会错过11个数据
        Thread.sleep(7000);
        flux.subscribe(System.out::println);

        Thread.sleep(5000000);
    }

}
