package com.tiger.dubbo.quickstart.service.impl;

import com.tiger.dubbo.quickstart.entity.Address;
import com.tiger.dubbo.quickstart.service.OrderService;
import com.tiger.dubbo.quickstart.service.RoleService;
import com.tiger.dubbo.quickstart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tiger.Shen
 * @date 2020/7/25 18:40
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    public void initOrder() {
        List<Address> userAddressList = userService.getUserAddressList();
        System.out.println(userAddressList);
    }

    @Override
    public void getRole() {
        String role = roleService.getRole("helll world");
        System.out.println(role);
    }
}
