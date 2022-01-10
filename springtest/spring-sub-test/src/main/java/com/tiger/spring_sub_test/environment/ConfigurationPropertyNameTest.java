package com.tiger.spring_sub_test.environment;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.boot.context.properties.source.ConfigurationProperty;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;

public class ConfigurationPropertyNameTest {

    @Test
    @SneakyThrows
    public void test(){
        ConfigurationPropertyName hello = ConfigurationPropertyName.of("hello[0].world[map].tag");
        ConfigurationProperty hello1 = new ConfigurationProperty(hello, "hello", null);

    }
}
