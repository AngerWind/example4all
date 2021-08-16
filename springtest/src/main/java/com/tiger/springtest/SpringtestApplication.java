package com.tiger.springtest;

import com.tiger.spring.SpringTestApplication;
import com.tiger.springtest.other.SimpleBeanNameGenerator;
import com.tiger.springtest.pojo.Student;
import com.tiger.springtest.pojo.Student1;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.Collection;
import java.util.Map;

@SpringBootApplication
// @ComponentScan(basePackageClasses = SpringtestApplication.class)
// @ComponentScan(nameGenerator = SimpleBeanNameGenerator.class, basePackageClasses = {SpringTestApplication.class})
public class SpringtestApplication {


    // @Value("")
    // private String name;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(new Class[]{SpringtestApplication.class}, args);
        // AnnotationConfigApplicationContext context1 = new AnnotationConfigApplicationContext();
        // context1.register(SpringTestApplication.class);
        // context1.refresh();
        // SpringtestApplication springtestApplication = context.getBean(SpringtestApplication.class);
        //
        // Collection<Student1> values = context.getBeansOfType(Student1.class).values();
        // System.out.println(values.size());
        // System.out.println(springtestApplication.name);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setIgnoreResourceNotFound(true);
        propertySourcesPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        return propertySourcesPlaceholderConfigurer;
    }

    // @Bean
    // public Student student1() {
    //     return new Student("zhangsan");
    // }

    // @Bean
    // public Student student() {
    //     return new Student("lisi");
    // }

}
