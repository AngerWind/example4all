package com.tiger.tableapi.udf.aggregate_function;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.AggregateFunction;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/16
 * @description
 */
public class AggregateFunctionExample {

    // 累加器类型定义
    public static class WeightedAvgAccumulator {
        public int sum = 0; // 加权和
        public int count = 0; // 数据个数
    }
    /**
     * 聚合函数需要两个泛型 第一个是最终输出的结果的类型 第二个是中间的聚合状态的类型
     */
    public static class WeightedAvg extends AggregateFunction<Integer, WeightedAvgAccumulator> {
        /**
         * 创建累加器
         */
        @Override
        public WeightedAvgAccumulator createAccumulator() {
            return new WeightedAvgAccumulator(); // 创建累加器
        }

        /**
         * 计算最终的结果
         */
        @Override
        public Integer getValue(WeightedAvgAccumulator acc) {
            if (acc.count == 0) {
                return null; // 防止除数为 0
            } else {
                return acc.sum / acc.count; // 计算平均值并返回
            }
        }

        /**
         * accumulate方法会来来了一个新的数据，调用一次
         *
         * 可以定义多个accumulate方法, 这样就可以接收不同类型的输入
         * 如果一个accumulate方法可以接收不同类型的参数, 那么也可以定义多个@FunctionHint
         */
        @FunctionHint(input = {@DataTypeHint(bridgedTo = Integer.class), @DataTypeHint(bridgedTo = Integer.class)},
                accumulator = @DataTypeHint(bridgedTo = WeightedAvgAccumulator.class),
                output = @DataTypeHint(bridgedTo = Integer.class))
        public void accumulate(WeightedAvgAccumulator acc, Integer iValue, Integer iWeight) {
            acc.sum += iValue;
            acc.count += iWeight;
        }
    }

}
