package com.tiger.springtest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.tiger.spring.SpringTestApplication;
import com.tiger.springtest.other.SimpleBeanNameGenerator;
import com.tiger.springtest.pojo.Student;
import com.tiger.springtest.pojo.Student1;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
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
        // SpringApplication application = new SpringApplicationBuilder(SpringApplication.class)
        //     // .profiles("add1", "add2")
        //     // .properties("spring.config.additional-location=addlocaltion", "spring.config.import=configimport")
        //     .build();
        // ConfigurableApplicationContext applicationContext = application.run(args);
        LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("com.springframework").setLevel(Level.ALL);
        ConfigurableApplicationContext run = SpringApplication.run(SpringApplication.class);

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
