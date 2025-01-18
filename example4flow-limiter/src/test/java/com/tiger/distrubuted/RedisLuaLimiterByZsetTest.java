package com.tiger.distrubuted;

import java.io.IOException;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/12
 * @description
 */
class RedisLuaLimiterByZsetTest {

    @Test
    public void redisLuaLimiterTests() throws InterruptedException, IOException {
        for (int i = 0; i < 15; i++) {
            Thread.sleep(200);
            System.out.println(LocalTime.now() + " " + new RedisLuaLimiterByZset().acquire("user1"));
        }
    }

}