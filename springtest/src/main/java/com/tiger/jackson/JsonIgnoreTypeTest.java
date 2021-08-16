package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonIgnoreTypeTest
 * @date 2021/7/5 10:21
 * @description
 */

@NoArgsConstructor
@JsonIgnoreType
public class JsonIgnoreTypeTest {

    public Integer id;
    public Student student;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreType
    public static class Student{
        private String name;
        private Integer age;
    }

    @Test
    @SneakyThrows
    public void test1(){
        Student zhangsan = new Student("zhangsan", 18);
        JsonIgnoreTypeTest test = new JsonIgnoreTypeTest();
        test.id = 2313123;
        test.student = zhangsan;
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(test));
        System.out.println(objectMapper.writeValueAsString(zhangsan));
    }

    @JsonIgnoreType
    public static class StudentType{}

    @Test
    @SneakyThrows
    public void test2(){
        Student zhangsan = new Student("zhangsan", 18);
        JsonIgnoreTypeTest test = new JsonIgnoreTypeTest();
        test.id = 2313123;
        test.student = zhangsan;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(String.class, Student.class);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(test));
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(zhangsan));

    }
}
