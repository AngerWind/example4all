package zkconfigcenter.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

public class NodeDataCuratorCacheListener implements CuratorCacheListenerBuilder.ChangeListener {
    private Environment environment;
    private ConfigurableApplicationContext applicationContext;
    public NodeDataCuratorCacheListener(Environment environment, ConfigurableApplicationContext applicationContext) {
        this.environment = environment;
        this.applicationContext=applicationContext;
    }
    @Override
    public void event(ChildData oldNode, ChildData node) {
        System.out.println("数据发生变更");
        String resultData=new String(node.getData());
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            Map<String,Object> map=objectMapper.readValue(resultData, Map.class);
            ConfigurableEnvironment cfe=(ConfigurableEnvironment)environment;
            MapPropertySource mapPropertySource=new MapPropertySource("configService",map);
            cfe.getPropertySources().replace("configService",mapPropertySource);
            //发布事件，用来更新@Value注解对应的值（事件机制可以分两步演示）
            applicationContext.publishEvent(new EnvironmentChangeEvent(this));
            System.out.println("数据更新完成");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}