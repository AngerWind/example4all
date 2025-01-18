package com.example4asm;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/21
 * @description
 */
public class Student<T> extends Person implements Singer {

    private int x = 10;
    public static final String hello = "hello";

    public T something;

    public Student(String name, int age) {
        super(name, age);
    }

    public int add(int a, int b) {
        return a + b;
    }

    public static String getHello() {
        return hello;
    }

    @Override
    public void sing() {
        System.out.println("student... sing...");
    }
}
