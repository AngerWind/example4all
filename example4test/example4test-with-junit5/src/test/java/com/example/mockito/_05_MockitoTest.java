package com.example.mockito;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.extension.*;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/28
 * @description
 */
// 让Junit加载Mockito扩展, 这样Mockito就加载到了Junit中了
@ExtendWith(MockitoExtension.class)
public class _05_MockitoTest {

    // MockitoExtension会创建Repository代理, 并注入进来
    @Mock
    Repository repository;

    // MockitoExtension会创建Service对象, 并注入进来,
    // 同时通过setter/构造函数将Repository注入到Service中
    @InjectMocks
    Service service;

    @Test
    void testSuccess() {

        try {
            // 设置Repository Mock对象的行为
            // 指定在调用repository.getStuff()的时候, 需要返回特定的结果集
            Mockito.when(repository.getStuff()).thenReturn(Arrays.asList("A", "B", "CDEFGHIJK", "12345", "1234"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 执行我们要测试的方法
        List<String> stuff = service.getStuffWithLengthLessThanFive();

        // 验证结果
        Assertions.assertNotNull(stuff);
        Assertions.assertEquals(3, stuff.size());
    }

    @Test
    void testException() {

        try {
            // 指定当我们调用代理对象的getStuff()时, 需要抛出异常
            Mockito.when(repository.getStuff()).thenThrow(new SQLException("Connection Exception"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 执行我们的测试代码
        List<String> stuff = service.getStuffWithLengthLessThanFive();

        // 验证结果
        Assertions.assertNotNull(stuff);
        Assertions.assertEquals(0, stuff.size());
    }

}
