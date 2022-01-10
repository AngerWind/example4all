package com.tiger.spring_sub_test.environment;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.StandardEnvironment;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title BinderTest
 * @date 2022/1/10 11:35
 * @description
 */
public class BinderTest {

    @Test
    @SneakyThrows
    public void test(){
        StandardEnvironment standardEnvironment = new StandardEnvironment();
        Binder binder = Binder.get(standardEnvironment);
        BindResult<String> javaHome = binder.bind("username", String.class);
        String s = javaHome.orElse("c://");
    }
}
