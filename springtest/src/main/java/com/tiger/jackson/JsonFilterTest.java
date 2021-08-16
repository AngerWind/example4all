package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;
import lombok.AllArgsConstructor;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonFilterTest
 * @date 2021/7/5 14:52
 * @description
 */
public class JsonFilterTest {

    @Data
    @AllArgsConstructor
    @JsonFilter("first")
    public static class Student{
        private String name;
        private Integer age;
        private Integer id;
    }

    @Test
    @SneakyThrows
    public void test1(){
        Student student = new Student("zhangsan", 14, 391);
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
        simpleFilterProvider.addFilter("first", SimpleBeanPropertyFilter.serializeAll());
        simpleFilterProvider.addFilter("second", SimpleBeanPropertyFilter.serializeAllExcept("age", "name"));
        simpleFilterProvider.addFilter("third", SimpleBeanPropertyFilter.filterOutAllExcept("name"));
        objectMapper.setFilterProvider(simpleFilterProvider);
        objectMapper.writer(simpleFilterProvider).writeValueAsString(student);
        System.out.println(student);
    }
}
