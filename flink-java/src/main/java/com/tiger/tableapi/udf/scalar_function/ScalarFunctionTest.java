package com.tiger.tableapi.udf.scalar_function;

import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/16
 * @description
 */

/**
 * 标量函数, 即输入一行数据的一个或者多个字段, 然后输出一个字段
 */
public class ScalarFunctionTest {

    @Test
    public void test() {
        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);

        DataStream<Tuple4<Integer, Integer, String, String>> stream = streamEnv.fromElements(Tuple4.of(1, 2, "3", "4"), Tuple4.of(4, 5, "6", "7"));

        tableEnv.createTemporaryView("event", stream,
                $("f0").as("c1"), $("f1").as("c2"),
                $("f2").as("c3"), $("f3").as("c4"));


        tableEnv.createTemporarySystemFunction("my_concat", MyConcatFunction.class);


        tableEnv.executeSql("select my_concat(c1, c2), my_concat(c3, c4) from event").print();
    }
}
