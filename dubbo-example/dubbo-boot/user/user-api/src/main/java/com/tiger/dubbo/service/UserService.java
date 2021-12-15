package com.tiger.dubbo.service;

import com.tiger.dubbo.bean.UserAddress;

import java.util.Arrays;
import java.util.List;

/**
 * @author Shen
 * @version v1.0
 * @Title UserService
 * @date 2021/12/11 22:21
 * @description
 */
public interface UserService {

    List<UserAddress> getUserAddressList(String userId);

}
