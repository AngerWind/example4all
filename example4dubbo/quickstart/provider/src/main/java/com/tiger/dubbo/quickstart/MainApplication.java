package com.tiger.dubbo.quickstart;

/**
 * @author Tiger.Shen
 * @date 2020/7/25 23:04
 */

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApplication {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("provider.xml");
        context.start();
        System.in.read(); // 按任意键退出
    }
}
