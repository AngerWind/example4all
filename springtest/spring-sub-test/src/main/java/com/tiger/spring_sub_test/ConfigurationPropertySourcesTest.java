package com.tiger.spring_sub_test;

import org.junit.Test;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * @author Shen
 * @version v1.0
 * @Title ConfigurationPropertySourcesTest
 * @date 2022/1/8 0:39
 * @description
 */
public class ConfigurationPropertySourcesTest {

    @Test
    public void test1() {
        StandardServletEnvironment environment = new StandardServletEnvironment();
        ConfigurationPropertySources.attach(environment);
        environment.getProperty("mavenhome");
    }
}
