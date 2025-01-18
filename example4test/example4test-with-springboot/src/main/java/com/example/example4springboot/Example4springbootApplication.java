package com.example.example4springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.example4springboot.mapper")
public class Example4springbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(Example4springbootApplication.class, args);
    }

}

