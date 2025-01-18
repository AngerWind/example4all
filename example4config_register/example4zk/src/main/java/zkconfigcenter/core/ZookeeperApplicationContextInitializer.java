package zkconfigcenter.core;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;

public class ZookeeperApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    //PropertySourceLocator接口支持扩展自定义配置加载到spring Environment中。
    private final List<PropertySourceLocator> propertySourceLocators;

    public ZookeeperApplicationContextInitializer(){
        //基于SPI机制加载所有的外部化属性扩展点
        ClassLoader classLoader= ClassUtils.getDefaultClassLoader();
        //这部分的代码是SPI机制
        propertySourceLocators=new ArrayList<>(SpringFactoriesLoader.loadFactories(PropertySourceLocator.class,classLoader));
    }
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        //获取运行的环境上下文
        ConfigurableEnvironment environment=applicationContext.getEnvironment();
        //MutablePropertySources它包含了一个CopyOnWriteArrayList集合，用来包含多个PropertySource。
        MutablePropertySources mutablePropertySources = environment.getPropertySources();
        for (PropertySourceLocator locator : this.propertySourceLocators) {
            //回调所有实现PropertySourceLocator接口实例的locate方法，收集所有扩展属性配置保存到Environment中
            Collection<PropertySource<?>> source = locator.locateCollection(environment,applicationContext);
            if (source == null || source.size() == 0) {
                continue;
            }
            //把PropertySource属性源添加到environment中。
            for (PropertySource<?> p : source) {
                //addFirst或者Last决定了配置的优先级
                mutablePropertySources.addFirst(p);
            }
        }
    }
}