package com.tiger;

import com.google.common.collect.Maps;
import com.tiger.springtest.SpringtestApplication;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.Map;

@Configuration
// @ComponentScan(basePackageClasses = SpringTestApplication.class, nameGenerator = SimpleBeanNameGenerator.class, scopeResolver = SimpleScopeMetadataResolver.class, lazyInit = false, resourcePattern = "**/*Test.class")
// @ComponentScan(useDefaultFilters = false)
// @ComponentScan(includeFilters = {
//         @Filter(type = FilterType.ANNOTATION, value = {Component.class})
// })
// @ComponentScan(excludeFilters = {
//         @Filter(type = FilterType.ASSIGNABLE_TYPE, value = {SimpleScopeMetadataResolver.class})
// })
// @ComponentScan(includeFilters = {
//         @Filter(type = FilterType.ASSIGNABLE_TYPE, value = {SimpleScopeMetadataResolver.class})
// })
@SpringBootApplication
public class SpringTestApplication implements BeanNameAware, Serializable {

    @Override
    public void setBeanName(String name) {
        System.out.println("this is " + this.getClass().getName());
        System.out.println("this bean name is " + name);
    }


    public static void main(String[] args) {
        // ConfigurableApplicationContext context = SpringApplication.run(new Class[]{SpringtestApplication.class}, args);

        Map<String, Object> defaultProperties = Maps.newHashMap();
        defaultProperties.put("spring.main.cloud-platform", "KUBERNETES");
        defaultProperties.put("spring.config.import", "classpath:import/*");
        defaultProperties.put("spring.config.additional-location", "aa");
        defaultProperties.put("spring.config.on-not-found", "ignore");
        defaultProperties.put("spring.config.name", "application, config");
        ConfigurableApplicationContext context = new SpringApplicationBuilder(SpringApplication.class)
            .properties(defaultProperties)
            .run(args);
        System.out.println(context.getEnvironment().getProperty("my"));
        System.out.println(context.getEnvironment().getProperty("my.arrayProp[0]"));
        System.out.println(context.getEnvironment().getProperty("my.arrayProp1[1]"));
        System.out.println(context.getEnvironment().getProperty("my.list[0]"));
        System.out.println(context.getEnvironment().getProperty("my.map.key1"));
//        AnnotationConfigApplicationContext context1 = new AnnotationConfigApplicationContext();

        // context1.register(SpringTestApplication.class);
        // context1.refresh();
        // SpringtestApplication springtestApplication = context.getBean(SpringtestApplication.class);
        //
        // Collection<Student1> values = context.getBeansOfType(Student1.class).values();
        // System.out.println(values.size());
        // System.out.println(springtestApplication.name);
    }
}
