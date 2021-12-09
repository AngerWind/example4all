package com.tiger.curator;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title LeaderLatchExample
 * @date 2021/12/7 15:38
 * @description
 */
public class LeaderLatchExample {
    private static final int CLIENT_QTY = 10;
    private static final String PATH = "/examples/leader";

    @Test
    public  void test() throws Exception {
        List<CuratorFramework> clients = Lists.newArrayList();
        List<LeaderLatch> examples = Lists.newArrayList();
        try {
            for (int i = 0; i < CLIENT_QTY; ++i) {
                CuratorFramework client =
                    CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
                clients.add(client);
                client.start();
                LeaderLatch example = new LeaderLatch(client, PATH, "Client #" + i);
                examples.add(example);
                example.start();

            }
            System.out.println("LeaderLatch初始化完成！");
            Thread.sleep(10 * 1000);// 等待Leader选举完成
            LeaderLatch currentLeader = null;
            for (int i = 0; i < CLIENT_QTY; ++i) {
                LeaderLatch example = examples.get(i);
                if (example.hasLeadership()) {
                    currentLeader = example;
                }
            }
            System.out.println("当前leader：" + currentLeader.getId());
            currentLeader.close();
            examples.get(0).await(10, TimeUnit.SECONDS);
            System.out.println("当前leader：" + examples.get(0).getLeader());
            System.out.println("输入回车退出");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            for (LeaderLatch exampleClient : examples) {
                System.out.println("当前leader：" + exampleClient.getLeader());
                try {
                    CloseableUtils.closeQuietly(exampleClient);
                } catch (Exception e) {
                    System.out.println(exampleClient.getId() + " -- " + e.getMessage());
                }
            }
            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }
        }
        System.out.println("OK!");
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
