package com.tiger.curator;

import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import static org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode.POST_INITIALIZED_EVENT;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title ZookeeperListener
 * @date 2021/12/7 19:39
 * @description
 */
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

    }
}
