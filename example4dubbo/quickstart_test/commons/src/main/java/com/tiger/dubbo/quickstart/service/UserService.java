package com.tiger.dubbo.quickstart.service;

import com.tiger.dubbo.quickstart.entity.Address;

import java.util.List;

/**
 * @author Tiger.Shen
 * @date 2020/7/25 18:29
 */
public interface UserService {

    List<Address> getUserAddressList();
}
