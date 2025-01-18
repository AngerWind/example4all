package com.tiger.tableapi.udf.table_aggregate_function;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/16
 * @description
 */

import static org.apache.flink.table.api.Expressions.$;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.api.Expressions;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.TableAggregateFunction;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;
import org.junit.Test;

/**
 * 表聚合函数, 也叫udtagg
 * 就是将多行数据的一个字段或者多个字段, 进行聚合, 生成多行数据, 每行可以有多个字段
 */
public class TableAggregateFunctionTest {

    /**
     * TableAggregateFunction需要两个泛型
     * 第一个是最终的结果的类型, 因为我们输出的一行数据中有多个字段, 所以使用row来表示
     * 第二个是中间的聚合状态的类型
     *
     * 这里我们定义了一个表聚合函数, 他用来聚合一个字段, 并返回数据中最大的两个
     * 同时还会统计输入的数据的个数
     * 比如我们的数据如下:
     * user_id    amount
     *  1         100
     *  1         200
     *  1         300
     *  2         200
     *  2         400
     *
     * select * from order group by user_id flatten(top2(value)) AS (amount, rk, total)
     * 等到结果为
     * user_id    amount    rk    total
     *   1          300      1     3
     *   1          200      2     3
     *   2          400      1     2
     *   2          200      2     2
     */

    public static class Top2Accumulator {
        public Integer first;
        public Integer second;
        public Integer count;
    }
    public static class Top2 extends TableAggregateFunction<Row, Top2Accumulator> {

        @Override
        public Top2Accumulator createAccumulator() {
            Top2Accumulator acc = new Top2Accumulator();
            acc.first = Integer.MIN_VALUE; // 为方便比较，初始值给最小值
            acc.second = Integer.MIN_VALUE;
            acc.count = 0;
            return acc;
        }

        /**
         * 每来一个数据调用一次，判断是否更新累加器
         * accumulate方法会来来了一个新的数据，调用一次
         *
         * 可以定义多个accumulate方法, 这样就可以接收不同类型的输入
         * 如果一个accumulate方法可以接收不同类型的参数, 那么也可以定义多个@FunctionHint
         */
        @FunctionHint(input = {@DataTypeHint(bridgedTo = Integer.class)},
                accumulator = @DataTypeHint(bridgedTo = Top2Accumulator.class),
                output = @DataTypeHint("ROW<val INT, top INT, count INT>"))
        public void accumulate(Top2Accumulator acc, Integer value) {
            if (value > acc.first) {
                acc.second = acc.first;
                acc.first = value;
            } else if (value > acc.second) {
                acc.second = value;
            }
            acc.count++;
        }


        /**
         * 当所有的数据都聚合完毕了之后, 会调用一次emitValue方法, 来输出最终的结果
         */
        public void emitValue(Top2Accumulator acc, Collector<Row> out) {
            if (acc.first != Integer.MIN_VALUE) {
                out.collect(Row.of(acc.first, 1, acc.count));
            }
            if (acc.second != Integer.MIN_VALUE) {
                out.collect(Row.of(acc.second, 2, acc.count));
            }
        }
    }

    @Test
    public void test() {
        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);

        DataStreamSource<Tuple2<Integer, Integer>> source = streamEnv.fromElements(
                Tuple2.of(1, 100), Tuple2.of(1, 200), Tuple2.of(1, 300), Tuple2.of(2, 200), Tuple2.of(2, 400)
        );

        tableEnv.createTemporarySystemFunction("top2", Top2.class);
        tableEnv.createTemporaryView("event", source, $("f0").as("user_id"), $("f1").as("amount"));

        /**
         * 表聚合函数还不支持直接通过sql来调用, 只能通过table api的形式
         */
        tableEnv.from("event")
                .groupBy($("user_id"))
                .flatAggregate(Expressions.call("top2", $("amount")).as("val", "rk", "cnt"))
                .select($("user_id"), $("val"), $("rk"), $("cnt"))
                .execute()
                .print();
    }
}
