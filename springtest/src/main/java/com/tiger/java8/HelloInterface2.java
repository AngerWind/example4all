package com.tiger.java8;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title HelloInterface
 * @date 2021/6/3 14:12
 * @description
 */
@FunctionalInterface
public interface HelloInterface2 {

    void sayHello(String say);

    static void say(){
        System.out.println("hello world");
    }
}
