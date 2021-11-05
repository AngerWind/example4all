package com.tiger.spring;

import com.tiger.os.SysInfoAcquirerService;
import com.tiger.springtest.SpringtestApplication;
import com.tiger.springtest.other.SimpleBeanNameGenerator;
import com.tiger.springtest.pojo.Student1;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SpringApplication
 * @date 2020/12/11 11:28
 * @description
 */
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
        ConfigurableApplicationContext context = SpringApplication.run(new Class[]{SpringtestApplication.class}, args);
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
