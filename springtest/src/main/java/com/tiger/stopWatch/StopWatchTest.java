package com.tiger.stopWatch;

import lombok.SneakyThrows;
import org.springframework.util.StopWatch;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title StopWatchTest
 * @date 2021/7/22 10:14
 * @description
 */
public class StopWatchTest {

    @SneakyThrows
    public static void main(String[] args) {
        StopWatch sw = new StopWatch();

        sw.start("校验耗时");
        Thread.sleep(1000);
        sw.stop();

        sw.start("组装报文耗时");
        Thread.sleep(2000);
        sw.stop();

        sw.start("请求耗时");
        Thread.sleep(1000);
        sw.stop();



        System.out.println(sw.prettyPrint());
        System.out.println(sw.getTotalTimeMillis());
    }
}
