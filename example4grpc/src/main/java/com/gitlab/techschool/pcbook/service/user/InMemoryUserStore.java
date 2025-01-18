package com.gitlab.techschool.pcbook.service.user;

import com.gitlab.techschool.pcbook.service.exception.AlreadyExistsException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/19
 * @description
 */
public class InMemoryUserStore implements UserStore{

    // key是username, value是User
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        if (users.containsKey(user.getName())) {
            throw new AlreadyExistsException("User already exists: " + user.getName());
        }
        users.put(user.getName(), user);
    }

    @Override
    public User find(String name) {
        // 返回一个clone
        return users.get(name).clone();
    }
}
