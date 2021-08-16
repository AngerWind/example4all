package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonManagedReferenceAndJsonBackReferenceTest
 * @date 2021/7/7 15:55
 * @description
 */
public class JsonManagedReferenceAndJsonBackReferenceTest {

    @Data
    @NoArgsConstructor
    public static class Boss{
        String name;
        String department;
        @JsonManagedReference
        List<Employee> employees;
    }

    @Data
    @NoArgsConstructor
    public static class Employee{
        String name;
        @JsonBackReference
        Boss boss;
    }


    @Test
    public void test() throws Exception {
        Employee employee1 = new Employee();
        employee1.setName("employee1");

        Employee employee2 = new Employee();
        employee2.setName("employee2");

        Boss boss = new Boss();
        boss.setName("boss");
        boss.setDepartment("cto");
        boss.setEmployees(Lists.newArrayList(employee1, employee2));

        employee1.setBoss(boss);
        employee2.setBoss(boss);

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(boss));
    }

    @Test
    @SneakyThrows
    public void test2(){
        String json = "{\"name\":\"boss\",\"department\":\"cto\",\"employees\":[{\"name\":\"employee1\"}," +
                "{\"name\":\"employee2\"}]}";
        ObjectMapper objectMapper = new ObjectMapper();
        Boss boss = objectMapper.readValue(json, Boss.class);
        System.out.println(boss);
    }
}
