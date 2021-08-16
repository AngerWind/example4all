package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Date;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonInjectTest
 * @date 2021/6/22 15:22
 * @description
 */
public class JsonInjectTest {

    @Data
    public static class Student{
        private String name;
        private Integer age;
        @JacksonInject(useInput = OptBoolean.FALSE)
        private Date date;
    }

    @Test
    @SneakyThrows
    public void test(){
        String json = "{\"name\":\"zhangsan\",\"age\":18}";
        ObjectMapper objectMapper = new ObjectMapper();
        InjectableValues.Std std = new InjectableValues.Std();
        std.addValue("date", new Date());
        objectMapper.setInjectableValues(std);
        Student student = objectMapper.readValue(json, Student.class);
        System.out.println(student);
    }

    @Test
    @SneakyThrows
    public void test1(){
        String json = "{\n" +
                "  \"name\" : \"zhangsan\",\n" +
                "  \"age\" : 18\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        InjectableValues.Std std = new InjectableValues.Std();
        std.addValue(Date.class, new Date());
        objectMapper.setInjectableValues(std);
        Student student = objectMapper.readValue(json, Student.class);
        System.out.println(student);
    }

    @Test
    @SneakyThrows
    public void test2(){
        String json = "{\"name\":\"zhangsan\",\"age\":18,\"date\":1624420664620}";
        ObjectMapper objectMapper = new ObjectMapper();
        InjectableValues.Std std = new InjectableValues.Std();
        std.addValue(Date.class, new Date());
        objectMapper.setInjectableValues(std);
        Student student = objectMapper.readValue(json, Student.class);
        System.out.println(student);
    }

    @SneakyThrows
    public static void main(String[] args) {
        Student student = new Student();
        student.setName("zhangsan");
        student.setAge(18);
        student.setDate(new Date());
        System.out.println(new ObjectMapper().writeValueAsString(student));
    }

    @SneakyThrows
    @Test
    public void test3(){
        Student student = new Student();
        Integer age = student.getAge();
        boolean equals = age.equals(new Integer(1));
        System.out.println(equals);
    }



}
