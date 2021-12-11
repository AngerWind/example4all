package com.tiger.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * @author Shen
 * @version v1.0
 * @Title RedisCommandTest
 * @date 2021/10/30 17:49
 * @description
 */
public class KeyCommand {

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
    public void keys() {
        /*
        有3个通配符 *, ? ,[], *: 通配任意多个字符, ?: 通配单个字符, []: 通配括号内的某1个字符
        keys *查看当前库所有key
         */
        Set<String> keys = jedis.keys("[hal]?ll*");
    }

    @Test
    public void exists() {
        // 返回存在的key的数量
        Long exists = jedis.exists("key1", "key2");
    }

    @Test
    public void type() {
        // 返回none， string， list， set， zset， hash
        String type = jedis.type("key");
    }

    @Test
    public void del() {
        // 返回删除的key的数量
        Long num = jedis.del("key1", "key2");
    }

    @Test
    public void unlink() {
        // 根据value选择非阻塞删除，仅将keys从keyspace元数据中删除，真正的删除会在后续异步操作。
        // 返回删除的key的数量
        Long num = jedis.unlink("key1", "key2");
    }

    @Test
    public void expire() {
        int ttl = 9;
        // 设置成功返回1， 失败0
        Long key = jedis.expire("key", ttl);
    }

    @Test
    public void ttl() {
        Long ttl = jedis.ttl("key");
    }

    @Test
    public void select() {
        String replyCode = jedis.select(0);
    }

    @Test
    public void dbsize() {
        Long size = jedis.dbSize();
    }

    @Test
    public void flushdb() {
        String replyCode = jedis.flushDB();
    }

    @Test
    public void flushall() {
        String replyCode = jedis.flushDB();
    }
}
