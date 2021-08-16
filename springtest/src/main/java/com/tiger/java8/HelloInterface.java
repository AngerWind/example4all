package com.tiger.java8;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title HelloInterface
 * @date 2021/6/3 14:12
 * @description
 */
public interface HelloInterface {

    void sayHello();

    static void say(){
        System.out.println("hello world");
    }

    default void play(String s) {
        System.out.println("interface");
    }
}
