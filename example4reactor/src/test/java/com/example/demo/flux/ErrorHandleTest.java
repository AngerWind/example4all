package com.example.demo.flux;

import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import org.junit.jupiter.api.Test;
/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/22
 * @description
 */
@SpringBootTest
public class ErrorHandleTest {

    @Test
    void handleWithConsumer() {
        Flux.just(1, 2, 3, 0)
            .map(i -> 10 / i)
            .subscribe(System.out::println, e -> {
                System.out.println("Error: " + e.getMessage());
            });
    }

    @Test
    void handleWithOnErrorReturn() {
        // onErrorReturn 将错误信号转换为一个默认数据, 然后给下游发送一个complete信号
        /*
            try{
                return doSomething();
            } catch {
                return -1;
            }
         */
        Flux.just(1, 2, 3, 0)
            .map(i -> 10 / i)
            .onErrorReturn(-1)
            .subscribe(System.out::println, e -> {
                System.out.println("Error: " + e.getMessage());
            }, () -> {
                System.out.println("Complete");
            });
    }

    @Test
    void handleWithOnErrorComplete() {
        // onErrorComplete 将错误信号转换为一个complete信号
        /*
            try{
                return doSomething();
            } catch {
                return -1;
            }
         */
        Flux.just(1, 2, 3, 0)
            .map(i -> 10 / i)
            .onErrorComplete()
            .subscribe(System.out::println, e -> {
                System.out.println("Error: " + e.getMessage());
            }, () -> {
                System.out.println("Complete");
            });
    }

    @Test
    void handleWithOnErrorContinue() {
        // onErrorComplete 执行回调后, 将错误丢掉, 相当于忽略错误
        /*
            try{
                return doSomething();
            } catch {

            }
         */
        Flux.just(1, 2, 3, 0)
            .map(i -> 10 / i)
            .onErrorContinue((e, i) -> {
                System.out.println("onErrorContinue: " + e.getMessage());
            })
            .subscribe(System.out::println, e -> {
                System.out.println("Error: " + e.getMessage());
            }, () -> {
                System.out.println("Complete");
            });
    }

    @Test
    void handleWithOnErrorMap() {
        // onErrorMap, 将异常转换为另外一种异常
        /*
            try{
                return doSomething();
            } catch {
                throw new RuntimeException(e);
            }
         */
        Flux.just(1, 2, 3, 0)
            .map(i -> 10 / i)
            .onErrorMap((e) -> {
                return new RuntimeException(e);
            })
            .subscribe(System.out::println, e -> {
                System.out.println("Error: " + e.getMessage());
            }, () -> {
                System.out.println("Complete");
            });
    }

    @Test
    void handleWithOnErrorResume() {
        // onErrorResume, 收到异常后, 重新订阅另外一个流, 然后执行后续的步骤
        /*
            try{
                return doSomething(100);
            } catch {
                return doOtherthing(100);
            }
         */
        Flux.just(1, 2, 3, 0)
            .map(i -> 10 / i)
            .onErrorResume(e -> {
               return Flux.range(1, 2);
            })
            .subscribe(System.out::println, e -> {
                System.out.println("Error: " + e.getMessage());
            }, () -> {
                System.out.println("Complete");
            });
    }

    @Test
    void handleWithDoOnError() {
        /*
            try{
                return doSomething(100);
            } catch(e) {
                doOnError(e);
                throw e;
            }
         */
        Flux.just(1, 2, 3, 0)
            .map(i -> 10 / i)
            .doOnError(e -> {
                // 通常用于日志记录
                System.out.println("doOnError");
            })
            .subscribe(System.out::println, e -> {
                System.out.println("Error: " + e.getMessage());
            }, () -> {
                System.out.println("Complete");
            });
    }

    @Test
    void handleWithDoFinally() {
        /*
            try{
                return doSomething(100);
            } finally {
                doFinally();
            }
         */
        Flux.just(1, 2, 3, 0)
            .map(i -> 10 / i)
            // 多个finally的执行顺序是反的
            .doFinally(signalType -> {
                // 通常用于资源回收
                System.out.println("doFinally");
            })
            .subscribe(System.out::println, e -> {
                System.out.println("Error: " + e.getMessage());
            }, () -> {
                System.out.println("Complete");
            });
    }

    @Test
    void handleWithDoAfterTerminate() {
        /*
            try{
                return doSomething(100);
            } finally {
                doFinally();
            }
         */
        Flux.just(1, 2, 3, 0)
            .map(i -> 10 / i)
            // 多个doAfterTerminate的执行顺序是反的
            .doAfterTerminate(() -> {
                // 通常用于资源回收
                System.out.println("doFinally");
            })
            .subscribe(System.out::println, e -> {
                System.out.println("Error: " + e.getMessage());
            }, () -> {
                System.out.println("Complete");
            });
    }
}
