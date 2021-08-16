package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Date;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonAliasTest
 * @date 2021/7/1 17:31
 * @description
 */
public class JsonAliasTest {

    @Data
    public static class Student{
        @JsonAlias({"Name", "namE"})
        private String name;
        private Integer age;
    }

    @Test
    @SneakyThrows
    public void test1(){
        String json = "{\"namE\" : \"zhangsan\",  \"age\" : 18}";
        ObjectMapper objectMapper = new ObjectMapper();
        Student student = objectMapper.readValue(json, Student.class);
        System.out.println(student);
    }
}
