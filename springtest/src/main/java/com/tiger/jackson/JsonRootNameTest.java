package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonRootNameTest
 * @date 2021/6/22 14:13
 * @description
 */
public class JsonRootNameTest {


    @JsonRootName(value = "student", namespace = "stu")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Student{
        private String name;
        private Integer age;
    }

    @Test
    @SneakyThrows
    public void test(){
        Student student = new Student("zhangsan", 18);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);
        System.out.println(s);

        objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        Student student1 = objectMapper.readValue(s, Student.class);
        System.out.println(student1);
    }

    @Test
    @SneakyThrows
    public void xml(){
        Student student = new Student("zhangsan", 18);
        XmlMapper xmlMapper = new XmlMapper();
        String s = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);
        System.out.println(s);

        Student student1 = xmlMapper.readValue(s, Student.class);
        System.out.println(student1);
    }
}
