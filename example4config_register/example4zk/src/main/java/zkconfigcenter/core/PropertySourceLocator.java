package zkconfigcenter.core;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public interface PropertySourceLocator {

    PropertySource<?> locate(Environment environment, ConfigurableApplicationContext applicationContext);
	//Environment表示环境变量信息
    //applicationContext表示应用上下文
    default Collection<PropertySource<?>> locateCollection(Environment environment, ConfigurableApplicationContext applicationContext) {
        return locateCollection(this, environment,applicationContext);
    }

    static Collection<PropertySource<?>> locateCollection(PropertySourceLocator locator,
                                                          Environment environment,ConfigurableApplicationContext applicationContext) {
        PropertySource<?> propertySource = locator.locate(environment,applicationContext);
        if (propertySource == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(propertySource);
    }
}