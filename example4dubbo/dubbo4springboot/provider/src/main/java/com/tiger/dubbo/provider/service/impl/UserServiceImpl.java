package com.tiger.dubbo.provider.service.impl;

import com.tiger.dubbo.quickstart.entity.Address;
import com.tiger.dubbo.quickstart.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tiger.Shen
 * @date 2020/7/26 18:26
 */
// 需要暴露的接口使用@DubboService进行标注
@DubboService
public class UserServiceImpl implements UserService {

    public List<Address> getUserAddressList() {
        Address address = new Address("beijin", 1);
        Address address1 = new Address("shanghai", 1);
        List<Address> addresses = Arrays.asList(address, address1);
        return addresses;
    }
}
