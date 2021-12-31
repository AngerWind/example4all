package com.tiger.redisson;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title LockTest
 * @date 2021/12/13 15:55
 * @description
 */
public class LockTest {

    @Before
    public void before() {
        LoggerContext iLoggerFactory = (LoggerContext)LoggerFactory.getILoggerFactory();
        iLoggerFactory.getLogger("root").setLevel(Level.INFO);
    }

    @Test
    @SneakyThrows
    public void test(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        RedissonClient redisson = Redisson.create(config);
        RLock lock = redisson.getLock("/lock");
        lock.lock();
        lock.tryLock();
        lock.unlock();
    }
}
