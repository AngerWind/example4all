package com.gitlab.techschool.pcbook.service.user;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/19
 * @description
 */
public interface UserStore {

    void save(User user);

    User find(String name);
}
