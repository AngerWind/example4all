package com.tiger.tableapi.udf.aggregate_function;

import static org.apache.flink.table.api.Expressions.$;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/16
 * @description
 */

/**
 * 聚合函数, 也叫udaf
 * 就是将多行数据的一个或者多个字段进行聚合, 得到一个结果
 */
public class AggregateFunctionTest {

    @Test
    public void test() {
        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);

        DataStream<Tuple3<Integer, Integer, Integer>> stream = streamEnv.fromElements(Tuple3.of(1, 2, 2), Tuple3.of(1, 3, 3));

        tableEnv.createTemporaryView("event", stream,
                $("f0").as("id"),
                $("f1").as("key1"),
                $("f2").as("key2"));


        tableEnv.createTemporarySystemFunction("weight_avg", AggregateFunctionExample.WeightedAvg.class);


        tableEnv.executeSql("select weight_avg(key1, key2) from event group by id").print();
    }
}
