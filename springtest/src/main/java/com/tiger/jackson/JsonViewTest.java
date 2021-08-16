package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonViewTest
 * @date 2021/7/7 10:45
 * @description
 */
public class JsonViewTest {
    public interface Base{}
    public interface Big extends Base{}
    public interface Small extends Base{}

    @Data
    @AllArgsConstructor
    public static class Student{
        @JsonView(Base.class)
        private String name;
        @JsonView(Big.class)
        private Integer big;
        @JsonView(Small.class)
        private Integer small;
        private Integer middle;
    }

    @Data
    @AllArgsConstructor
    public static class Student2{
        @Getter
        @Setter
        @JsonView(Base.class)
        private Student student;
    }

    @Test
    @SneakyThrows
    public void test(){
        ObjectMapper objectMapper = new ObjectMapper();
        Student student = new Student("zahngsan", 999, 111, 555);
        System.out.println(objectMapper.writerWithView(Base.class).writeValueAsString(student));
        System.out.println(objectMapper.writerWithView(Big.class).writeValueAsString(student));
        System.out.println(objectMapper.writerWithView(Small.class).writeValueAsString(student));

        Student2 student2 = new Student2(student);
        System.out.println(objectMapper.writerWithView(Big.class).writeValueAsString(student2));

    }
}
