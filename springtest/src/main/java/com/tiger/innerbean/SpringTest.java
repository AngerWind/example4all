package com.tiger.innerbean;

import lombok.var;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SpringTest
 * @date 2020/12/22 18:22
 * @description
 */
@SpringBootApplication
public class SpringTest {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SpringTest.class);
        ConfigurationClass2 class2 = run.getBean(ConfigurationClass2.class);
        // Student student1 = class2.student();
        // Student student = class2.student();
        // System.out.println(student == student1);
    }
}
