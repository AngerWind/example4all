package com.example.demo.flux;

import org.junit.platform.commons.annotation.Testable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.context.properties.bind.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.SignalType;

@SpringBootTest
public class SubscribeOperationsTest {

    /**
     * flux.subscribe()有很多重载的方法
     * 可以分为两类:
     *      1. 传入Consumer对象, 一般是lambda表达式
     *          这种情况会自动调用requestUnbooded()来请求无限数据
     *      2. 传入Subscriber对象
     *          这种请求要自己请求数据
     */
    public static class SubscriberImpl implements Subscriber<Integer> {
        Subscription subscription;

        @Override
        public void onSubscribe(Subscription s) {
            System.out.println("Subscriber 订阅upstream成功: " + s);
            subscription = s;
            System.out.println("-----------------------------");
            subscription.request(1);
        }

        @Override
        public void onNext(Integer integer) {
            System.out.println("Subscriber 接收到上游数据: " + integer);
            System.out.println("-----------------------------");

            // if (integer == 2) {
            //     subscription.cancel();
            // }
            // if (integer == 2) {
            //     throw new RuntimeException("异常");
            // }

            subscription.request(1);
            // subscription.request(Integer.MAX_VALUE); // 请求无限的数据, 即关闭背压

        }

        @Override
        public void onError(Throwable t) {
            System.out.println("Subscriber 接收到上游异常: " + t);
        }

        @Override
        public void onComplete() {
            System.out.println("Subscriber 接收到上游正常结束信号");
        }
    }

    @Test
    void subscribeWithMutilpleSubscriber() {
        // 一个flow可以有多个订阅者
        Flux<String> just = Flux.just("Hello", "World");
        just.subscribe(System.out::println);
        just.subscribe(s -> System.out.println("Received: " + s));
    }

    @Test
    void subscribeWithConsumer() {
        // 订阅后, 会自动请求无限的数据
        Flux<Integer> just = Flux.range(0, 10);
        just.subscribe((n) -> System.out.println("Received: " + n), // 相当与Subscriber.onNext()
            (e) -> System.out.println("Error: " + e),  // 相当于Subscriber.onError()
            () -> System.out.println("Completed"),  // 相当于Subscriber.onComplete()
            (s) -> System.out.println("Subscription: " + s)); // 相当于Subscriber.onSubscribe()
    }

    @Test
    void subscribeWithEmpth() {
        Flux<Integer> just = Flux.range(0, 10);
        // 只起一个订阅作用, 让数据流动起来
        just.subscribe();
    }

    @Test
    void subscribeWithSubscriber() {
        Flux<Integer> just = Flux.range(0, 10);
        just.subscribe(new SubscriberImpl());
    }

    @Test
    void subscribeWithBaseSubscriber() {
        /*
         * BaseSubscriber是一个抽象类, 但是没有要实现的方法,
         * 所以在创建BaseSubscriber的时候, 需要使用new BaseSubscriber(){}的方式来创建
         *
         * BaseSubscriber中实现了Subscriber接口, 并实现了onNext, onComplete, onError, onSubscribe方法
         * 同时BaseSubscriber还提供了五个钩子函数
         * 1. hookOnSubscribe
         *      在Subscriber.onSubscribe()中调用, 默认实现是requstUnbounded()
         *      如果重写了该方法, 一定要在方法中调用request()
         *      并且看情况再hookOnNext()方法中调用request()
         * 2. hookOnNext
         *      在Subscriber.onNext()中调用
         * 3. hookOnError
         *      看源码吧, 很简单
         * 4. hookOnComplete
         *      在Subscriber.onComplete()中调用
         * 5. hookOnCancel
         *      在Subscription.cancel()中调用
         * 6. hookFinally
         *      看源码吧, 很简单, 懒得写了
         */
        Flux.range(1, 5)
            .subscribe(new BaseSubscriber<Integer>() {

                @Override
                protected void hookOnSubscribe(Subscription subscription) {
                    System.out.println("Subscriber 订阅upstream成功: " + subscription);
                    request(1);
                    // requestUnbounded(); // 等于request(Integer.MAX_VALUE)
                }

                @Override
                protected void hookOnNext(Integer value) {
                    super.hookOnNext(value);
                    System.out.println("Subscriber 接收到上游数据: " + value);
                    request(1);
                }

                @Override
                protected void hookOnComplete() {
                    // 在Subscriber.onComplete()中调用
                    System.out.println("Subscriber 接收到上游正常结束信号");
                }

                @Override
                protected void hookOnError(Throwable throwable) {
                }

                @Override
                protected void hookOnCancel() {
                }

                @Override
                protected void hookFinally(SignalType type) {
                }
            });
    }
}