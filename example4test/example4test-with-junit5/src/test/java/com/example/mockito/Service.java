package com.example.mockito;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private final Repository repository;

    public Service(Repository repository) {
        this.repository = repository;
    }

    public List<String> getStuffWithLengthLessThanFive() {
        try {
            // 返回获取到的所有长度小于5的字符串
            return repository.getStuff().stream()
                    .filter(stuff -> stuff.length() < 5)
                    .collect(Collectors.toList());
            // 如果数据库抛出了异常, 那么就返回一个空列表
        } catch (SQLException e) {
            // 如果数据库抛出了异常, 那么就返回一个空列表
            return List.of();
        }
    }
}