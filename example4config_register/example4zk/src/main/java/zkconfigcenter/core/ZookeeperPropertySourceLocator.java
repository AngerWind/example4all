package zkconfigcenter.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Map;

public class ZookeeperPropertySourceLocator implements PropertySourceLocator{

    private final CuratorFramework curatorFramework;

    private final String DATA_NODE="/data";  //仅仅为了演示，所以写死目标数据节点

    public ZookeeperPropertySourceLocator() {
        curatorFramework= CuratorFrameworkFactory.builder()
                .connectString("192.168.221.128:2181")
                .sessionTimeoutMs(20000).connectionTimeoutMs(20000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .namespace("config").build();
        curatorFramework.start();
    }

    @Override
    public PropertySource<?> locate(Environment environment, ConfigurableApplicationContext applicationContext) {
        System.out.println("开始加载远程配置到Environment中");
        CompositePropertySource composite = new CompositePropertySource("configService");
        try {
            Map<String,Object> dataMap=getRemoteEnvironment();
            //基于Map结构的属性源
            MapPropertySource mapPropertySource=new MapPropertySource("configService",dataMap);
            composite.addPropertySource(mapPropertySource);
            addListener(environment,applicationContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return composite;
    }

    private Map<String,Object> getRemoteEnvironment() throws Exception {
        String data=new String(curatorFramework.getData().forPath(DATA_NODE));
        //暂时支持json格式
        ObjectMapper objectMapper=new ObjectMapper();
        Map<String,Object> map=objectMapper.readValue(data, Map.class);
        return map;
    }
    //添加节点变更事件
    private void addListener(Environment environment, ConfigurableApplicationContext applicationContext){
        NodeDataCuratorCacheListener curatorCacheListener=new NodeDataCuratorCacheListener(environment,applicationContext);
        CuratorCache curatorCache=CuratorCache.build(curatorFramework,DATA_NODE,CuratorCache.Options.SINGLE_NODE_CACHE);
        CuratorCacheListener listener=CuratorCacheListener
                .builder()
                .forChanges(curatorCacheListener).build();
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }
}