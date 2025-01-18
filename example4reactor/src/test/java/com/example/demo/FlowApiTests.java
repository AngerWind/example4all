package com.example.demo;

import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FlowApiTests {

    public static class MySubscriber implements Subscriber<String> {

        // 保存订阅关系, 其中维护了上游和下游
        Subscription subscription;

        @Override
        public void onSubscribe(Subscription subscription) {
            System.out.println("subscriber 开始订阅");
            // 订阅成功时调用
            this.subscription = subscription;
            // 想上游的缓冲区中获取一个数据
            subscription.request(1);
        }

        @Override
        public void onNext(String item) {
            // 接收到数据时调用
            System.out.println("接收到数据：" + item);
            if ("cancel".equals(item)) {
                // 接受到自己想要的数据后, 可以取消对上游的订阅
                subscription.cancel();
            }
            subscription.request(1); // 再次从上游的缓冲区中取一个数据
        }

        @Override
        public void onError(Throwable throwable) {
            // 上游出现了异常, 关闭了
            // 有可能是调用了 publisher.closeExceptionally();
            // 也有可能是在处理数据的时候出现了异常
            System.out.println("上游发生错误: " + throwable.getMessage());
        }

        @Override
        public void onComplete() {
            // 上游关闭时调用
            System.out.println("上游正常关闭");
        }

    }

    // 应为processor用来接收上游数据, 处理之后, 发送给下游
    // 所以Processor继承了Subscriber<T>, Publisher<R>两个接口
    // Subscriber<T>中的方法定义了Processor如何处理上游的数据
    // Publisher<R>中的方法定义了Processor如何将数据发送给下游
    public static class MyProcessor extends SubmissionPublisher<String> implements Processor<String, String> {
        // 保存订阅关系, 其中维护了上游和下游
        Subscription subscription;

        @Override
        public void subscribe(Subscriber<? super String> subscriber) {
            // 这里直接继承SubmissionPublisher, 借用父类上的方法
            super.subscribe(subscriber);
        }


        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
            System.out.println("processor 开始订阅");
            subscription.request(1);
        }

        @Override
        public void onNext(String item) {
            item += "哈哈^^";
            super.submit(item); // 将数据发送给下游
            subscription.request(1); // 再次从上游的缓冲区中取一个数据
        }

        @Override
        public void onError(Throwable throwable) {
            // 上游出现了异常, 关闭了
            // 有可能是调用了 publisher.closeExceptionally();
            // 也有可能是在处理数据的时候出现了异常
            System.out.println("上游发生错误: " + throwable.getMessage());
            // 关闭自己
            super.closeExceptionally(throwable);
        }

        @Override
        public void onComplete() {
            // 上游关闭时调用
            System.out.println("上游正常关闭");
            // 关闭自己
            super.close();
        }
    }

    @Test
    void testFlow() {
        // 创建一个发布者, 必须实现Publisher接口
        // 这里使用了JDK自带的一个发布者,
        // 创建SubmissionPublisher对象时,需要传入一个线程池作为底层支撑;
        // 该类也提供了一个无参数的构造器,该构造器使用ForkJoinPool.commonPool()方法来提交发布者,
        // 以此实现发布者向订阅者提供数据项的异步特性。
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        // 创建一个Processor
        MyProcessor processor = new MyProcessor();

        // 创建一个订阅者
        MySubscriber subscriber = new MySubscriber();

        // 将订阅者订阅到发布者, 一个发布者可以有多个订阅者
        publisher.subscribe(processor);
        processor.subscribe(subscriber);


        // 开始发布数据
        for (int i = 0; i < 100; i++) {
            publisher.submit(String.valueOf(i));
            if (i == 10) {
                // 发布者关闭通道, 将会调用下游的onComplete方法
                publisher.close();
                // 发布者异常关闭通道, 将会调用下游的onError方法
                // publisher.closeExceptionally(new RuntimeException("发布者异常关闭"));
                break;
            }
        }

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
