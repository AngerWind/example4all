package com.example;

import org.junit.jupiter.api.*;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/27
 * @description
 */
public class _03_LifecycleTest {

    @BeforeAll
    static void beforeAll() {
        // 静态方法, 在所有测试运行前执行一次
        System.out.println("Connect to the database");
    }

    @AfterAll
    static void afterAll() {
        // 静态方法, 在所有测试运行后执行一次
        System.out.println("Disconnect from the database");
    }

    @BeforeEach
    void beforeEach() {
        // 每个测试方法运行前执行
        System.out.println("Load the schema");
    }

    @AfterEach
    void afterEach() {
        // 每个测试方法运行后执行
        System.out.println("Drop the schema");
    }

    @Test
    void test1() {
        System.out.println("Test 1");
    }

    @Test
    void test2() {
        System.out.println("Test 2");
    }
}
