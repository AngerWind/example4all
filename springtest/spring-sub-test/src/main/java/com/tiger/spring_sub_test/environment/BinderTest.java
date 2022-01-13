package com.tiger.spring_sub_test.environment;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.boot.context.properties.bind.*;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.core.env.StandardEnvironment;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title BinderTest
 * @date 2022/1/10 11:35
 * @description
 */

public class BinderTest {

    public static class Student{
        // some field
    }

    @Test
    @SneakyThrows
    public void test() {
        StandardEnvironment environment = new StandardEnvironment();
        // 获取Binder对象
        Binder binder = Binder.get(environment);

        // 绑定对象, 指定绑定的属性名，与返回的对象类型
        // 与Environment.getProperty(String name, Class<?> target)类型，但是更高级
        Student propertiesC = binder.bind("kaka.cream.mail-c", Bindable.of(Student.class)).get();

        // 绑定Map
        Map<String, Object> propMap =
            binder.bind("fish.jdbc.datasource", Bindable.mapOf(String.class, Object.class)).get();
        // 绑定List
        List<String> list = binder.bind("kaka.cream.list", Bindable.listOf(String.class)).get();

        // 转换以及默认值
        String datestr = binder.bind("kaka.cream.date", Bindable.of(String.class))
            // 映射为大写
            .map(String::toUpperCase)
            // 默认值
            .orElse("bad date string");

        // 绑定过程回调函数，高度定制
        // BindHandler是处理绑定过程的事件的回调函数
        LocalDate str =
            binder.bind("kaka.cream.date", Bindable.of(LocalDate.class), new BindHandler() {
                @Override
                public <T> Bindable<T> onStart(ConfigurationPropertyName name, Bindable<T> target,
                    BindContext context) {
                    // name为指定属性名的ConfigurationPropertyName
                    // target为指定的需要绑定的类
                    System.out.println("绑定开始");
                    return target;
                }
                @Override
                public Object onSuccess(ConfigurationPropertyName name, Bindable<?> target, BindContext context,
                    Object result) {
                    System.out.println("绑定成功");
                    return result;
                }
                @Override
                public Object onFailure(ConfigurationPropertyName name, Bindable<?> target, BindContext context,
                    Exception error) throws Exception {
                    System.out.println("绑定失败");
                    return "没有找到匹配的属性";
                }

                @Override
                public void onFinish(ConfigurationPropertyName name, Bindable<?> target, BindContext context,
                    Object result) throws Exception {
                    System.out.println("绑定结束");
                }
            }).get();
    }
}
