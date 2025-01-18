package com.example.demo.flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.FluxSink;

@SpringBootTest
public class CreateOperationsTest {
    @Test
    void createFlow() {
        // 从多个元素中创建流
        Flux.just("Hello", "World");
        Flux.fromArray(new String[] {"Hello", "World"});
        Flux.fromIterable(Arrays.asList("Hello", "World"));
        Flux.fromStream(Stream.of("Hello", "World"));

        // 创建一个空的流, 其中包含一个正常结束的信号
        Flux.empty();

        // 产生一个0-9的整数流
        Flux.range(0, 10);

        // 创建一个空流, 其中有一个异常信号
        Flux.error(new RuntimeException("error"));

        // 创建一个不发送任何消息的流
        Flux.never();

        // 创建一个流，该流在指定的时间间隔后发送一个消息
        Flux.interval(Duration.ofSeconds(1));
    }

    @Test
    void generate() {
        // 自定义一个发布者
        // 等效于Flux.range(0, 11)

        // generate主要用于单线程环境, 即sink不能被并发调用
        Flux.generate(
            // 生成一个初始值, 推荐使用原子类, 防止并发问题
            () -> new AtomicInteger(),
            // 接受到一个数据后调用
            (state, sink) -> {
                int i = state.getAndIncrement();
                // 发送i给下游
                sink.next(i);
                if (i == 10) {
                    // 如果i=10, 发送完成信号
                    sink.complete();
                    sink.error(new RuntimeException("不想发了"));
                }
                // 返回一个数据, 重新调用该lambda表达式
                return state;
            }, (state) -> {
                // 但调用sink.complete()或者sink.error()后, 会调用该lambda表达式
                // 主要用于资源清理
                // 也可以不传该lambda, 只传上面两个参数!!!!!!!!!!!!
                System.out.println("资源清理");
                System.out.println("最后的state为: " + state);
            }).subscribe(System.out::println);
    }

    @Test
    void create() {
        // 自定义一个发布者, 使用create方法
        AtomicReference<FluxSink<Integer>> fluxSink = new AtomicReference<>();
        // create()主要用于多线程的环境中, 即多个线程同时调用sink.next()
        Flux.<Integer>create(sink -> {
            // 接受到一个sink, 该sink用于给下游发送一个数据, 可以多次调用next
            fluxSink.set(sink);
        }).subscribe(System.out::println);


        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                FluxSink<Integer> sink = fluxSink.get();
                sink.next(finalI);
                if (finalI == 5){
                    sink.complete();
                    // sink.error(new RuntimeException("异常"));
                }
            }).start();
        }
    }
}