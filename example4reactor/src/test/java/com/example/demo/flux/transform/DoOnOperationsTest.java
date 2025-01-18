package com.example.demo.flux.transform;


import com.example.demo.flux.SubscribeOperationsTest.SubscriberImpl;
import java.io.IOException;
import java.time.Duration;
import org.springframework.boot.context.properties.bind.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import org.junit.jupiter.api.Test;


@SpringBootTest
public class DoOnOperationsTest {
        @Test
        void test1() {
            // doOn会生成一个新的flux, 而不是对当前的flux设置监听
            Flux<Integer> range = Flux.range(1, 10);
            Flux<Integer> flux = range.doOnNext(System.out::println);

            System.out.println(range == flux);
            System.out.println(range.equals(flux));
        }
        
        @Test
        void test2() {
            // 当前代码不会输出, 应为doOn是一个传输操作, 只有当有订阅者订阅了流之后才会开始执行
            // 类比于Stream, Stream.map().filter()都不会执行, 只有当调用toList()等结束操作后, 流才会开始执行
            Flux<Integer> flux = Flux.range(1, 10).doOnNext(System.out::println);
        }

        @Test
        void doOn() throws IOException, InterruptedException {

            // 比如doOnError, 他的处理逻辑就是将上游接受的数据和非error信号发送给下游,
            // 只有在接受到上游的error信号时, 才会执行我们的自定义逻辑, 然后将error信号发送给下游

            // 比如doOnNext, 他的逻辑就是接受到上游的数据时, 执行我们的自定义逻辑, 然后把数据原封不动的给下游

            Flux<Integer> flux = Flux.range(1, 3)
                // 在接受到上游的数据后, 延迟1s再发送给下游
                .delayElements(Duration.ofSeconds(1))
                .log("delayElements")
                .doOnRequest(n -> {
                    // 当前流接收到下游的request信号时调用
                    // 在该方法调用后, 会把请求信号发送给上游
                    System.out.println("doOnRequest: 当前流请求upstream的数据, 个数为: " + n);
                })
                .log("doOnRequest")
                .doOnComplete(() -> {
                    // 接受到上游的正常结束信号时调用
                    // 调用之后会把结束信号发送给下游
                    System.out.println("doOnComplete: 接受到上游的正常结束信号");
                })
                .log("doOnComplete")
                .doOnError(e -> {
                    // 接受到上游的error信号时调用
                    // 调用之后会把结束信号发送给下游
                    System.out.println("doOnError: 接受到上游的异常结束信号: " + e);
                })
                .log("doOnError")
                .doOnCancel(() -> {
                    // 当前流接受到下游的cancel信号时调用
                    // 执行该方法后, 会将取消信号发送给上游
                    System.out.println("doOnCancel: 当前流接收到了下游cancel信号, 或者当前流被调用了cancel信号, 需要向上游发送cancel信号");
                })
                .log("doOnCancel")
                .doOnSubscribe(subscription -> {
                    // 当前流订阅upstream时, 会发出subscribe信号给upstream, upstream依次向上传播
                    // 然后upstream依次向下发送订阅成功的信号

                    // 当当前流接收到上游的订阅成功信号时, 会调用该方法
                    // 该方法执行成功后, 会把订阅成功信号发送给下游
                    System.out.println("doOnSubscribe: 当前流成功订阅了upstream");
                })
                .log("doOnSubscribe")
                .doOnNext(n -> {
                    // 当上游的数据到来时被调用
                    // 在该方法调用后, 会把数据发送给下游
                    System.out.println("doOnNext: 接收到上游数据: " + n);
                    // if (n == 2) {
                    //     throw new RuntimeException("n == 2");
                    // }
                })
                .log("doOnNext")
                .doOnEach(signal -> {
                    // 在接收到上游的数据或者信号(不管是正常结束还是异常结束)时调用
                    // 调用该方法之后, 会把数据/信号发送给下游
                    if (signal.isOnNext()) {
                        System.out.println("doOnEach, 接收到上游数据: " + signal.get());
                    } else if (signal.isOnError()) {
                        System.out.println("doOnEach, 接收到上游异常: " + signal.getThrowable());
                    } else if (signal.isOnComplete()) {
                        System.out.println("doOnEach, 接收到上游正常结束信号");
                    } else if (signal.isOnSubscribe()) {
                        System.out.println("doOnEach, 当前流成功订阅了upstream: " + signal.getSubscription());
                    }
                })
                .log("doOnEach") ;
            flux.subscribe(new SubscriberImpl());

            
            // 当调用subscription.cancel()取消订阅的时候, 会发送cancel信号给上游, 然后依次传播, 会触发doOnCancel()

            // 当调用Flux.subscribe()方法的时候, 首先会调用doFirst()方法, 然后从源头发出一个onSubscribe信号给下游, 依次传播
            // 该信号会触发doOnSubscribe()方法, 最终到达订阅者, 然后调用Subscriber.onSubscribe()


            // todo 当flux在处理数据的时候报错了, 会怎么样调用

            Thread.sleep(100000);
        }
}