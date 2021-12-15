package com.tiger.curator;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LeaderSelectorTest {
    static int CLINET_COUNT = 5;
    static String LOCK_PATH = "/leader_selector";

    @Before
    public void before() {
        LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
        lc.getLogger("org").setLevel(Level.ERROR);
    }

    /**
     * 5个LeaderSelector竞选，当选两次后退出竞选
     */
    @Test
    public void test() throws Exception {

        List<CuratorFramework> clientsList = Lists.newArrayListWithCapacity(CLINET_COUNT);
        //启动10个zk客户端，每几秒进行一次leader选举
        for (int i = 0; i < CLINET_COUNT; i++) {
            ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3, 5000);
            CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
            zkClient.start();
            zkClient.blockUntilConnected();
            SimpleLeaderSelector exampleClient = new SimpleLeaderSelector(zkClient, LOCK_PATH, "client_" + i);

            clientsList.add(zkClient);
            exampleClient.start();
        }

        System.in.read();
        clientsList.forEach(CuratorFramework::close);
    }

    static class SimpleLeaderSelector extends LeaderSelectorListenerAdapter implements Closeable {
        private final LeaderSelector leaderSelector;
        private final AtomicInteger leaderCount = new AtomicInteger(0);

        public SimpleLeaderSelector(CuratorFramework client, String path, String name) {
            leaderSelector = new LeaderSelector(client, path, this);
            leaderSelector.setId(name);

            // 该方法能让客户端在释放leader权限后 重新加入leader权限的争夺中
            leaderSelector.autoRequeue();
        }

        public void start() throws IOException {
            leaderSelector.start();
        }

        @Override
        public void close() throws IOException {
            leaderSelector.close();
        }

        @Override
        public void takeLeadership(CuratorFramework client) throws Exception {
            leaderCount.incrementAndGet();
            log.info("{} 竞选成功, 当前当选次数：{}", this.leaderSelector.getId(), this.leaderCount.get());
            log.info("当前leader和所有竞选者：{}", leaderSelector.getParticipants());
            if (leaderCount.get() == 2) {
                log.info("{} 退出选举", this.leaderSelector.getId());
                leaderSelector.close();
            }
            log.info("{} 完成操作，释放leader\n", this.leaderSelector.getId());
        }
    }
}

