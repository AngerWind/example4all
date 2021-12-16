package com.tiger.redisson;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title LockTest
 * @date 2021/12/13 15:55
 * @description
 */
public class LockTest {

    @Test
    @SneakyThrows
    public void test(){
        Config config = new Config();
        config.useClusterServers().setNodeAddresses(Lists.newArrayList("127.0.0.1:6379"));
        RedissonClient redisson = Redisson.create(config);
        RLock lock = redisson.getLock("/lock");
        lock.lock();
        lock.unlock();
    }
}
