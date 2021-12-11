package com.tiger.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

/**
 * @author Shen
 * @version v1.0
 * @Title StringCommand
 * @date 2021/10/31 11:34
 * @description
 */
public class StringCommand {

    JedisPool jedisPool;
    Jedis jedis;

    @Before
    public void init() throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        jedisPool = new JedisPool(config, "127.0.0.0", 6379, 999, "");
        jedis = jedisPool.getResource();
    }

    @After
    public void close() {
        if (jedis != null) {
            jedis.close();
        }
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    @Test
    public void set() {
        String replyCode = jedis.set("key", "value");
        String replyCode1 = jedis.set("key".getBytes(), "value".getBytes());

        SetParams setParams = new SetParams();
        setParams.px(1000L).nx();
        String replyCode2 = jedis.set("key", "value", setParams);
    }

    @Test
    public void setex() {
        String replyCode = jedis.setex("key", 10, "value");
    }

    @Test
    public void setnx() {
        // 1成功设置， 0设置失败
        Long replyCode = jedis.setnx("key","value");
    }


}
