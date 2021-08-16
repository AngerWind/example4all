package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonAutoDetect
 * @date 2021/7/5 11:45
 * @description
 */
public class JsonAutoDetectTest {

   @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
   @NoArgsConstructor
   @AllArgsConstructor
    public static class Student{
         private String name;
         protected Integer age;
         Integer sex;
         public Integer id;
    }

    @Test
    @SneakyThrows
    public void test1(){
        Student student = new Student("zhangsan", 1, 18, 123);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        System.out.println(objectMapper.writeValueAsString(student));
    }
}
