package com.tiger.hadoop.mapreduce;

import java.net.URL;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title ClassPathSys
 * @date 2022/3/8 18:56
 * @description
 */
public class ClassPathSys {

    public static void main(String[] args) {
        URL resource = new ClassPathSys().getClass().getClassLoader().getResource("org.apache.hadoop.mapreduce.v2.app.MRAppMaster");
        System.out.println(resource.toString());
    }
}
