package com.tiger.classloader;

import java.util.Arrays;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title MyClassLoader
 * @date 2020/12/23 14:09
 * @description
 */
public class MyClassLoader  extends ClassLoader{

    public static void main(String[] args) {
        Arrays.stream(args).forEach(System.out::println);
    }

    MyClassLoader(String a, String b){

    }

    MyClassLoader(int a) {

    }
}
