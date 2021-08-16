package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonValueTest
 * @date 2021/6/21 20:08
 * @description
 */
public class JsonValueTest {

    @Data
    @AllArgsConstructor
    public static class Student{
        @JsonValue
        private String name;
        private Integer age;

        // 使用toString()方法序列化实例
        // @JsonValue
        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    @SneakyThrows
    public void test(){
        Student student = new Student("zhangsan", 18);
        String s = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(student);
        System.out.println(s);
    }
}
