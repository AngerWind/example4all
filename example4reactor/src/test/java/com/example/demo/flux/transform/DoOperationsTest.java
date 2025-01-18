package com.example.demo.flux.transform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/21
 * @description
 */
@SpringBootTest
public class DoOperationsTest {

    @Test
    public void testBuffer() {
        // doFirst()会最先执行, 然后range()才会发出onSubscribe信号
        // doFinally会在接收到上游的complete/error信号, 或者接受到下游的cancel信号时被调用
        // doAfterTerminate会在接收到上游的complete/error信号时被调用

        // 需要注意的是, doFinally和doAfterTerminate都是现将信号发送出去, 然后信号到达publish或者subscriber后, 再调用doFinally和doAfterTerminate
        // 所以多个doFinally和doAfterTerminate之间的调用属性是!!!!!反的!!!!!!

        // 在这个案例中, range发出complete信号后, 会依次到达subscriber, 然后在调用doAfterTerminate, 然后调用doFinally
        Flux.range(0, 10)
            .log("range")
            .doFirst(() -> {
                System.out.println("doFirst.....");
            })
            .log("doFirst")
            .doFinally(signalType -> {
                System.out.println("doFinally: 接收到上游的正常/异常结束信号, 或者是下游的cancel信号: " + signalType);
            })
            .log("doFinally")
            .doAfterTerminate(() -> {
                System.out.println("doAfterTerminate: 接受到上游的正常/异常结束信号");
            })
            .log("doAfterTerminate")
            .subscribe();
    }
}
