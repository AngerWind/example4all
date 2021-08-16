package com.tiger.innerbean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title AnnotationConfigMainClass
 * @date 2020/12/23 11:19
 * @description
 */
public class AnnotationConfigMainClass {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationClass2.class, Address.class);
        Student student = context.getBean(Student.class);
        System.out.println(student.address == null);

        System.out.println(AnnotationConfigApplicationContext.class.getClassLoader()
        == Student.class.getClassLoader());
        System.out.println(Student.class.getClassLoader());

        ConfigurableEnvironment environment = context.getEnvironment();
        environment.setActiveProfiles("production", "akka");
        environment.acceptsProfiles(Profiles.of("production & ( akka | hello )"));
        System.out.println("hello");
    }
}
