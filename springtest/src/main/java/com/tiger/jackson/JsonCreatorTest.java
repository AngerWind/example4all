package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

import java.beans.ConstructorProperties;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonCreatorTest
 * @date 2021/6/21 20:32
 * @description
 */
public class JsonCreatorTest {

    public static class Student{
        private String name;
        private Integer age;
        @JsonCreator
        public Student(@JsonProperty("name")String name){
            this.name = name;
            this.age = 10;
        }

        // @JsonCreator和@ConstructorProperties搭配使用，不需要使用@JsonProperty， @ConstructorProperties只能使用在构造方法上
        // @JsonCreator
        // @ConstructorProperties({"name", "age"})
        // public Student(String name, Integer age){
        //     this.name = name;
        //     this.age = age;
        // }

        // 工厂方法
        @JsonCreator
        public static Student getInstance(@JsonProperty("name")String name, @JsonProperty("age")Integer age){
            return new Student(name, age);
        }

        public Student(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    @SneakyThrows
    public void test(){
        JsonValueTest.Student student = new JsonValueTest.Student("zhangsan", 18);
        String s = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(student);
        System.out.println(s);
    }
}
