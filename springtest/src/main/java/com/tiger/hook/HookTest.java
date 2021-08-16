package com.tiger.hook;

import com.tiger.test1.Student1;

import java.util.Arrays;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title HookTest
 * @date 2021/5/7 10:40
 * @description
 */
public class HookTest {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

        }));

        Object[] objects = new Object[3];
        objects[1] = new Student1("100");
        objects[2] = new Student1("200");
        objects[0] = new Student1("000");
        Object[] objects1 = Arrays.copyOf(objects, objects.length);
        Student1 stu = (Student1) objects[0];
        stu.setSeconds("9999");
        System.out.println(objects);
        System.out.println(objects1);
    }


}
