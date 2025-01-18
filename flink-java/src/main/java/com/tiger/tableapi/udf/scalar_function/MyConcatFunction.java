package com.tiger.tableapi.udf.scalar_function;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.ScalarFunction;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/16
 * @description
 */
public class MyConcatFunction extends ScalarFunction {

    /**
     * 必须要有无参的构造函数
     */
    public MyConcatFunction() {}

    // 不知道怎么使用FunctionHint来定义String...
    public String eval(String... args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg);
        }
        return sb.toString();
    }

    /**
     * 定义多个@FunctionHint, 表示同一个eval, 可以接收不同类型的输入
     */
    @FunctionHint(input = {@DataTypeHint("INT"), @DataTypeHint("INT")}, output = @DataTypeHint("STRING"))
    @FunctionHint(input = {@DataTypeHint("FLOAT"), @DataTypeHint("FLOAT")}, output = @DataTypeHint("STRING"))
    public String eval(Number a, Number b) {
        return String.valueOf(a) + String.valueOf(b);
    }
}
