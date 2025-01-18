package com.example;

import org.junit.jupiter.api.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/27
 * @description
 */
class _01_MathToolsTest {

    /**
     * 测试正常情况, 使用@DisplayName注解指定测试的名称
     */
    @Test
    @DisplayName("测试正常情况")
    void testConvertToDecimalSuccess() {
        double result = MathTools.convertToDecimal(3, 4);
        // 对结果进行断言
        Assertions.assertEquals(0.75, result);
    }

    /**
     * 测试异常情况
     */
    @Test
    @DisplayName("测试异常情况")
    void testConvertToDecimalInvalidDenominator() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> MathTools.convertToDecimal(3, 0));
    }
}