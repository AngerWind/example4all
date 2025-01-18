package com.example;

import org.junit.jupiter.api.*;

import java.time.Duration;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/27
 * @description
 */
public class _02_AssertionsTest {

    /**
     * Assertions的使用比较简单, 当前类主要列举了部分常用的方法
     */
    @Test
    public void testAssertions() {

        int a = 5;
        int c = 5;
        // 断言相等和不相等, 内部调用equals
        Assertions.assertEquals(10, a + c);
        Assertions.assertNotEquals(15, a + c);

        // 断言引用相等和不相等, 内部调用 ==
        Assertions.assertNotSame(new Object(), new Object());
        Assertions.assertSame(Integer.valueOf("1"), Integer.valueOf("1"));

        // 断言数组相等和不相等
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        Assertions.assertArrayEquals(arr1, arr2);

        // 断言true和false
        int c1 = 10;
        int c2 = 10;
        int c3 = 11;
        Assertions.assertTrue(c1 < c3);
        Assertions.assertFalse(c1 > c2);

        // 断言null和not null
        Assertions.assertNull(null);
        Assertions.assertNotNull("Hello");

        // 断言抛出异常
        Assertions.assertThrows(ArithmeticException.class, () -> {
            int x = 10 / 0;
        });
        // 断言不抛出异常
        Assertions.assertDoesNotThrow(() -> {
            System.out.println("Hello");
        });

        // 断言如下代码不会超时
        // 注意, executable会在当前线程中执行, 如果executable执行超时了, 那么会一直卡住, 直到executable执行完毕
        Assertions.assertTimeout(Duration.ofSeconds(10), () -> {
            Thread.sleep(5 * 1000);
        });
        // 断言如下代码不会超时, executable会在新线程中执行, 如果executable执行超时了, 那么会中断executable线程
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            Thread.sleep(5 * 1000);
        });
    }
}
