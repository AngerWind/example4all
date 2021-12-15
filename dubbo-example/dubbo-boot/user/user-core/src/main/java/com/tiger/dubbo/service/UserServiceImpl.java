package com.tiger.dubbo.service;

import com.tiger.dubbo.bean.UserAddress;

import java.util.Arrays;
import java.util.List;

/**
 * @author Shen
 * @version v1.0
 * @Title UserServiceImpl
 * @date 2021/12/11 22:21
 * @description
 */
public class UserServiceImpl implements UserService{

    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        System.out.println("UserServiceImpl.....old...");
        UserAddress address1 = new UserAddress(1, "北京市昌平区宏福科技园综合楼3层", "1", "李老师", "010-56253825", "Y");
        UserAddress address2 = new UserAddress(2, "深圳市宝安区西部硅谷大厦B座3层（深圳分校）", "1", "王老师", "010-56253825", "N");
        return Arrays.asList(address1,address2);
    }

}
