package com.example.demo.flux.transform;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/22
 * @description
 */
@SpringBootTest
public class LogicOperationsTest {

    @Test
    public void filter() {
        Flux.range(1, 10)
            .filter(i -> i % 2 == 0) // 符合条件的发给下游
            .subscribe(System.out::println);
    }

    @Test
    public void map() {
        Flux.range(1, 10)
            .map(i -> i * 10) // 映射, 即一个元素转换为另一个元素
            .subscribe(System.out::println);
    }

    @Nested
    class FlatMapTest {

        // flatMap和flatMapSequential都是将一个元素, 转换为一个流, 流中可以有多个元素
        // 不同点在于:
        //      flatMapSequential 对于返回的流, 会先订阅一个, 等这个流接收之后, 再订阅一个
        //          所以发送给下游的流的元素, 是按照顺序发送的。
        //      flatMap 会对生成的流立即订阅, 这样流就会立刻开始发送元素。
        //          所以发送给下游的流的元素, 是按实际生产的顺序发送的
        @Test
        public void flatMap() throws InterruptedException {

            Mono<List<Integer>> list =
                Flux.range(0, 4).flatMap(i -> Flux.just(i, i * 10).delayElements(Duration.ofSeconds(i)))
                    .collect(Collectors.toList());
            System.out.println(list.block()); // [0, 0, 1, 10, 2, 3, 20, 30]
        }

        @Test
        public void flatMapSequential() throws InterruptedException {
            List<Integer> list =
                Flux.range(0, 4).flatMapSequential(i -> Flux.just(i, i * 10).delayElements(Duration.ofSeconds(i)))
                    .collect(Collectors.toList()).block();
            System.out.println(list); // [0, 0, 1, 10, 2, 20, 3, 30]
        }
    }

    @Nested
    class ConcatTest {
        @Test
        public void concatMap() {
            // 接受一个元素, 然后将其转换为一个流

            // 与 flatMap 不同的是，concatMap 会根据原始流中的元素顺序依次把转换之后的流进行合并
            // 与 flatMapSequential 不同的是，concatMap 对转换之后的流的订阅是动态进行的，而 flatMapSequential 在合并之前就已经订阅了所有的流。
            Flux.range(1, 10)
                .concatMap(i -> Flux.just(i, i * 10))
                .collectList().subscribe(System.out::println);
            // [1, 10, 2, 20, 3, 30, 4, 40, 5, 50, 6, 60, 7, 70, 8, 80, 9, 90, 10, 100]
        }

        @Test
        public void concat() {
            // 先订阅一个, 等这个流接收之后, 再订阅另一个, 所以发送给下游的流是按照顺序发送的。
            Flux.concat(Flux.range(0, 4), Flux.just("zhangsna", "LISI"))
                .collect(Collectors.toList()).subscribe(System.out::println);
            // [0, 1, 2, 3, zhangsna, LISI]
        }

        @Test
        public void concatWith() {
            // 连接两个流, 和concat不同的是, concatWith连接的两个流元素类型必须是一样的
            Flux.range(0, 4)
                .concatWith(Flux.range(4, 3))
                .collect(Collectors.toList()).subscribe(System.out::println);
            // [0, 1, 2, 3, 4, 5, 6]
        }
    }

    @Nested
    class TransformTest {

        @Test
        public void transformDeferred() {
            AtomicInteger atomic = new AtomicInteger(0);

            Flux<String> flux1 = Flux.just("a", "b", "c")
                .transformDeferred(flux -> {
                    System.out.println(atomic.getAndIncrement());
                    return flux.map(v -> v + "1");
                });
            // 每个订阅者都会执行一次transformDeferred
            flux1.subscribe(System.out::println);
            flux1.subscribe(System.out::println);
        }

        @Test
        void transform() {
            AtomicInteger atomic = new AtomicInteger(0);

            Flux<String> flux1 = Flux.just("a", "b", "c")
                .transform(flux -> {
                    System.out.println(atomic.getAndIncrement());
                    return flux.map(v -> v + "1");
                });
            // 无论有多少个订阅者, transform只执行一次
            flux1.subscribe(System.out::println);
            flux1.subscribe(System.out::println);
        }
    }

    @Nested
    class MergeTest {

        @Test
        void merge() throws IOException {
            // 会先订阅所有流, 然后依次发送元素, 所以发送给下游的流是按照实际生产顺序发送的。
            Flux.merge(
                    Flux.just(1, 2, 3).delayElements(Duration.ofSeconds(1)),
                    Flux.just("a", "b").delayElements(Duration.ofMillis(1500)),
                    Flux.just("haha", "hehe", "heihei", "xixi").delayElements(Duration.ofMillis(500)))
                .subscribe();
        }

        @Test
        void mergeWith() {
            // 和merge的区别是, 只能合并两个同类型的流
            Flux.just(1, 2, 3).mergeWith(Flux.just(4, 5, 6)).subscribe();
        }

        @Test
        void mergeSequential() {
            // 先订阅所有的流, 然后只把第一个流的元素发送给下游, 直到收到第一个流的结束信号, 再发送第二个流的元素, 依次类推
            Flux.mergeSequential();
        }

    }

    @Test
    public void zip() {
        // 将多个流的元素按照顺序组合成一个tuple, 无法组队的元素被丢弃
        Flux.zip(Flux.just(1, 2, 3), Flux.just("a", "b", "c"), Flux.just("A", "B"))
            .map(tuple3 -> {
                return tuple3.getT1() + tuple3.getT2() + tuple3.getT3();
            })
            .collectList()
            .subscribe(System.out::println);
        // [1aA, 2bB]
    }


}
