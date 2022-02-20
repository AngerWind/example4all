package com.tiger.dubbo.quickstart;

import com.tiger.dubbo.quickstart.service.OrderService;
import com.tiger.dubbo.quickstart.service.RoleService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Tiger.Shen
 * @date 2020/7/26 14:46
 */
public class ConsumerApplication {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        OrderService orderService = context.getBean(OrderService.class);
        orderService.initOrder();
        orderService.getRole();

        System.in.read();
    }
}
