package com.tiger.curator;

import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import static org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode.POST_INITIALIZED_EVENT;


public class PathChildrenCacheTest {

    @Test
    @SneakyThrows
    public void test(){
        try (CuratorFramework client = CuratorFrameworkFactory
            .newClient("10.2.0.201:2181", new ExponentialBackoffRetry(1000, 3))) {
            client.start();
            client.blockUntilConnected();

            try (PathChildrenCache pathChildrenCache = new PathChildrenCache(client,
                "/default/MasterSelector/welab-skyscanner-tenma/task/local", true)) {

                pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                        if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                                @Override
                                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                                    throws Exception {
                                    System.out.println(event);
                                }
                            });
                        }
                    }
                });
                pathChildrenCache.start(POST_INITIALIZED_EVENT);
                // pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
                Thread.sleep(Long.MAX_VALUE);
            }
        }
    @SneakyThrows
    @Test
    public void test() {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3, 5000);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        zkClient.blockUntilConnected();

        PathChildrenCache childrenCache = new PathChildrenCache(zkClient, "/children", true, false, Executors.newSingleThreadExecutor());
        childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    // initialized事件，只有在StartMode为POST_INITIALIZED_EVENT才会触发，表示已经异步初始化完成。
                    case INITIALIZED:
                        break;

                    case CHILD_ADDED:
                        break;
                    case CHILD_REMOVED:
                        break;
                    case CHILD_UPDATED:
                        break;

                    case CONNECTION_LOST:
                        break;
                    case CONNECTION_RECONNECTED:
                        break;
                    case CONNECTION_SUSPENDED:
                        break;
                }
            }
        });
        List<ChildData> currentData = childrenCache.getCurrentData();
        for (ChildData childData : currentData) {
            childData.getData();
            childData.getPath();
            childData.getStat();
        }
        ChildData data = childrenCache.getCurrentData("full");

    }
}
