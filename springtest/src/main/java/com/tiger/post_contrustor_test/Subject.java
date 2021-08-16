package com.tiger.post_contrustor_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.sql.SQLOutput;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Subject
 * @date 2021/1/27 10:20
 * @description
 */
@SpringBootApplication
public class Subject extends Super{

    public Subject() {
        System.out.println("subject contrustor");
    }


    @PostConstruct
    public void init1() {
        System.out.println("subject init");
    }

    public static void main(String[] args) {
        SpringApplication.run(Subject.class);
    }
}
