package com.tiger.redis;

import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title RedisTest
 * @date 2021/10/29 15:07
 * @description
 */
public class RedisTest {

    JedisPool pool;

    @Before
    public void before() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置最大10个连接
        jedisPoolConfig.setMaxTotal(10);
        pool = new JedisPool(jedisPoolConfig, "localhost", 6379);
    }

    @After
    public void after() {
        if (pool != null) {
            pool.close();
        }
    }

    @Test
    public void test() {
        Jedis jedis = pool.getResource();
        new Thread(() -> {
            jedis.subscribe(new RedisPubSub(), "channel");
        }).start();
        pool.getResource().publish("channel", "hello world");
        System.out.println("he;;p");

    }

    @Test
    @SneakyThrows
    public void test1(){
        Jedis resource = pool.getResource();
//        resource.hmget()
    }

    @Test
    @SneakyThrows
    public void test2(){
        Jedis resource = pool.getResource();
//        JedisCluster jedisCluster = new JedisCluster();
//        jedisCluster.
    }



    public static class RedisPubSub extends JedisPubSub {

        @Override
        public void onMessage(String channel, String message) {
            System.out.printf("channel: %s%nmessage: %s%n", channel, message);
        }

        @Override
        public void onSubscribe(String channel, int subscribedChannels) {
            System.out.printf("channel: %s%nsubscribedChannels: %d%n", channel, subscribedChannels);
        }

        @Override
        public void onPMessage(String pattern, String channel, String message) {
            System.out.printf("channel: %s%nmessage: %s%npattern: %s", channel, message, pattern);
        }
    }
}
