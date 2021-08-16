package com.tiger.innerbean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title ConfigurationClass2
 * @date 2020/12/22 18:24
 * @description
 */
@Configuration
public class ConfigurationClass2 {

    @Bean
    public Student student(Address address) {
        Student student = new Student();
        student.address = address;
        return student;
    }
}
