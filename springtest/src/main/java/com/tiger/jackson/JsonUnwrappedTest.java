package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import groovy.lang.Tuple;
import groovy.lang.Tuple2;
import lombok.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonUnwrapped
 * @date 2022/5/10 15:31
 * @description
 */
public class JsonUnwrappedTest {

    public static class Money {
        public double remain;
    }
    public static class PersonInfo {
        public String name;
        public int id;
    }

    public static class Account {
        @JsonUnwrapped
        public Money money;
        @JsonUnwrapped
        public PersonInfo personInfo;
    }

    public static class Student {
        public int age;
        public Set<Account> accounts = new HashSet<>();
    }

    @SneakyThrows
    public static void main(String[] args) {
        Money money = new Money();
        money.remain = 2.5d;

        PersonInfo personInfo = new PersonInfo();
        personInfo.id = 4;
        personInfo.name = "zahang";

        Account account = new Account();
        account.money = money;
        account.personInfo = personInfo;

        System.out.println(new ObjectMapper().writeValueAsString(account));

        Student student = new Student();
        student.age = 13;
        student.accounts.add(account);

        System.out.println(new ObjectMapper().writeValueAsString(student));

        HashSet<Integer> s1 = Sets.newHashSet(1, 2, 3, 4);
        HashSet<Integer> s2 = Sets.newHashSet(3, 4, 5, 6);


        // 求s1 - s2的结果
        Sets.SetView<Integer> difference1 = Sets.difference(s1, s2);
        // 求s2 - s1的结果
        Sets.SetView<Integer> difference2 = Sets.difference(s2, s1);
        // 求s1，s2的交集
        Sets.SetView<Integer> intersection = Sets.intersection(s1, s2);
        // 求s1，s2的并集
        Sets.SetView<Integer> union = Sets.union(s1, s2);

        System.out.println(difference1); // [1, 2]
        System.out.println(difference2); // [5, 6]
        System.out.println(intersection); // [3, 4]
        System.out.println(union);  // [1, 2, 3, 4, 5, 6]
    }

    @Test
    @SneakyThrows
    public void test(){
        Person zhangsan = new Person(1, "zhangsan");
        Person lisi = new Person(2, "lisi");
        Person wangwu = new Person(3, "wangwu");

        HashSet<Person> s1 = Sets.newHashSet(zhangsan, lisi);
        HashSet<Person> s2 = Sets.newHashSet(lisi, wangwu);

        Map<Integer, Person> map1 = s1.stream().collect(Collectors.toMap(person -> person.getId(), person -> person));
        Map<Integer, Person> map2 = s2.stream().collect(Collectors.toMap(person -> person.getId(), person -> person));

        // 求s1 - s2的结果
        Sets.SetView<Integer> difference1 = Sets.difference(map1.keySet(), map2.keySet());
        // 求s2 - s1的结果
        Sets.SetView<Integer> difference2 = Sets.difference(map2.keySet(), map1.keySet());
        // 求s1，s2的交集
        Sets.SetView<Integer> intersection = Sets.intersection(map1.keySet(), map2.keySet());
        // 求s1，s2的并集
        Sets.SetView<Integer> union = Sets.union(map1.keySet(), map2.keySet());

        difference1.forEach(id -> System.out.println(map1.get(id)));
        difference2.forEach(id -> System.out.println(map2.get(id)));
        intersection.forEach(id -> System.out.println(map2.get(id)));
        union.forEach(id -> System.out.println(map1.containsKey(id) ? map1.get(id) : map2.get(id)));

    }

    @Data
    @AllArgsConstructor
    public class Person{
        int id;
        String name;
    }

}
