package com.tiger.tableapi.udf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.annotation.InputGroup;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/16
 * @description
 */
public class FunctionHintTest {

    /**
     * FunctionHint用于告诉flink, 这个函数可以接收什么类型的参数, 以及返回什么类型的结果
     *
     * 可以将多个FunctionHint放在一个方法上, 表示他可以接受多个类型的参数
     *
     * 有三种方式来指定eval接收的参数个数和类型
     *   1. 使用DataTypeHint直接传入一个字符串
     *   2. 使用DataTypeHint的bridgedTo来指定对应的class
     *   3. 直接什么都不要做, flink会自动解析返回值和参数, 来自动匹配
     */

    /**
     * 可以有多个重载的eval方法, 用于应对不同类型的输入
     */

    // 使用inputGroup = InputGroup.ANY表示任意类型
    @FunctionHint(input = {@DataTypeHint(inputGroup = InputGroup.ANY)}, output = @DataTypeHint("STRING"))
    public String eval(Object args) {
        return args.toString();
    }

    /**
     * 定义多个@FunctionHint, 表示同一个eval, 可以接收不同类型的输入
     */
    @FunctionHint(input = {@DataTypeHint("INT"), @DataTypeHint("INT")}, output = @DataTypeHint("STRING"))
    @FunctionHint(input = {@DataTypeHint("FLOAT"), @DataTypeHint("FLOAT")}, output = @DataTypeHint("STRING"))
    public String eval(Number a, Number b) {
        return String.valueOf(a) + String.valueOf(b);
    }

    /**
     * 如果你不知道如何使用字符串来指定类型
     * 那么你也可以直接使用bridgedTo来指定
     */
    // @FunctionHint(input = {@DataTypeHint("ARRAY<STRIGN>")}, output = @DataTypeHint("STRING"))
    @FunctionHint(input = {@DataTypeHint(bridgedTo = String[].class)}, output = @DataTypeHint(bridgedTo = String.class))
    public String eval(String[] args) {
        return String.join("", args);
    }

    /**
     * 不知道怎么表示Integer...
     * 就不要写FunctionHint, flink会使用反射来解析参数和返回值类型
     */
    public String eval(Integer... args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg);
        }
        return sb.toString();
    }
}
