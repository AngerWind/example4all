package com.tiger.dubbo.consumer.controller;

import com.google.gson.Gson;
import com.tiger.dubbo.quickstart.entity.Address;
import com.tiger.dubbo.quickstart.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Tiger.Shen
 * @date 2020/7/26 17:44
 */
@Controller
public class OrderController {

    // 通过@DubboReference来生产远程代理对象
    @DubboReference
    UserService userService;


    @GetMapping("/address")
    @ResponseBody
    public String getUserAddressList() {
        List<Address> userAddressList = userService.getUserAddressList();
        System.out.println(userAddressList);
        return new Gson().toJson(userAddressList);
    }
}
