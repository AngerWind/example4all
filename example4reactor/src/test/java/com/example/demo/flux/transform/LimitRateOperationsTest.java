package com.example.demo.flux.transform;

import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import org.junit.jupiter.api.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/4/21
 * @description
 */
@SpringBootTest
public class LimitRateOperationsTest {

    @Test
    public void testLimitRate() {
        /**
         * limitRate: 采用预取和补货策略, 将下游传来的request拆分为多个小的request
         *      预取: 即接受到下游的request信号后, 先request(highTide)个数据
         *      补货: 当预取的数据到了lowTide个时, 触发补货策略, 再request(lowTide)个数据
         *
         * 在当前案例中: limitRate(100)在接受到下游的request(Integer.MAX_VALUE)后
         *      会先发一个request(100)给上游, 此时range开始向下游开始发送数据
         *      当limitRate接受到了75个元素的时候, 又会发一个request(75)给上游
         *      当limitRate接受到了75个元素的时候, 又会发一个request(75)给上游, 依次循环
         */
        Flux.range(1, 1000)
            .log()
            .limitRate(100, 80)
            .subscribe();
    }

    @Test
    public void testLimitRate1() {
        // 与limitRate(highTide, lowTide)类似
        // 只不过highTide = prefetchRate
        // lowTide = prefetchRate * 75%  (向上取整)
        Flux.range(1, 1000)
            .log()
            .limitRate(100)
            .subscribe();
    }
}
