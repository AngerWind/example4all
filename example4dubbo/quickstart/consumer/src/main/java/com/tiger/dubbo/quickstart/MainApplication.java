package com.tiger.dubbo.quickstart;

import com.tiger.dubbo.quickstart.service.OrderService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Tiger.Shen
 * @date 2020/7/26 14:46
 */
public class MainApplication {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("consumer.xml");
        OrderService orderService = context.getBean(OrderService.class);
        orderService.initOrder();
        System.in.read();
    }
}
