package zkconfigcenter.core;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

public class CustomPropertySourceLocator implements PropertySourceLocator{

    @Override
    public PropertySource<?> locate(Environment environment, ConfigurableApplicationContext applicationContext) {
        Map<String, Object> source = new HashMap<>();
        source.put("age","18");
        MapPropertySource propertiesPropertySource = new MapPropertySource("configCenter",source);
        return propertiesPropertySource;
    }
}