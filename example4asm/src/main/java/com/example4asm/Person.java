package com.example4asm;

import java.io.Serializable;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/21
 * @description
 */
public class Person implements Serializable {

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
