package com.tiger.curator;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.Participant;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title LeaderLatchExample
 * @date 2021/12/7 15:38
 * @description
 */
public class LeaderLatchExample {

    @Before
    public void  before() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger("root").setLevel(Level.ERROR);
    }

    @Test
    public  void test() throws Exception {

        final int CLIENT_QTY = 5;
        final String PATH = "/examples/leader";

        List<CuratorFramework> clients = Lists.newArrayList();
        List<LeaderLatch> examples = Lists.newArrayList();

        try {
            for (int i = 0; i < CLIENT_QTY; ++i) {
                CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                        .build();
                client.start();
                client.blockUntilConnected();

                // 指定选举路径， 该LeaderLatch的id
                LeaderLatch leaderLatch = new LeaderLatch(client, PATH, "LeaderLatch #" + i);
                leaderLatch.addListener(new SimpleLeaderLatchListener(leaderLatch));
                // 开始竞选
                leaderLatch.start();

                clients.add(client);
                examples.add(leaderLatch);

            }
            System.in.read();
        } finally {
            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }
        }
    }

    @Data
    @AllArgsConstructor
    public static class SimpleLeaderLatchListener implements LeaderLatchListener {

        public LeaderLatch leaderLatch;

        @SneakyThrows
        @Override
        public void isLeader() {
            System.out.println(leaderLatch.getId() + " 成为leader");

            System.out.println("当前leader和所有follower的状态：");
            for (Participant participant: leaderLatch.getParticipants()) {
                System.out.print(participant.getId() + " , isLeader: " + participant.isLeader() + "\n");
            }

            Thread.sleep(1000);
            System.out.println(leaderLatch.getId() + "退出选举");
            leaderLatch.close(LeaderLatch.CloseMode.NOTIFY_LEADER);
        }

        @Override
        public void notLeader() {
            System.out.println(leaderLatch.getId() + " 失去leader身份\n");
        }
    }

    @Test
    @SneakyThrows
    public void test2(){
        CuratorFramework client =
            CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        client.blockUntilConnected();

        try (LeaderLatch leaderLatch = new LeaderLatch(client, "/leader")) {
            leaderLatch.start();
            Thread.sleep(2000);
            leaderLatch.addListener(new LeaderLatchListener() {
                @Override
                public void isLeader() {
                    System.out.println("*******************************************");
                }

                @Override
                public void notLeader() {
                    System.out.println("-----------------------------------------------");
                }
            });
            System.out.println(leaderLatch.hasLeadership());

        }

        System.out.println("aa");
    }
}
