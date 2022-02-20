package com.tiger.dubbo.quickstart.service.impl;

import com.tiger.dubbo.quickstart.entity.Address;
import com.tiger.dubbo.quickstart.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tiger.Shen
 * @date 2020/7/25 18:29
 */
public class UserServiceImpl implements UserService {

    public List<Address> getUserAddressList() {
        Address address = new Address("shanghai", 1);
        Address address1 = new Address("beijin", 2);
        return Arrays.asList(address, address1);
    }
}
