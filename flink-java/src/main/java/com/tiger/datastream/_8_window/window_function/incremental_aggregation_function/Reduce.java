package com.tiger.datastream._8_window.window_function.incremental_aggregation_function;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.Test;

import java.time.Duration;


/**
 * 增量聚合函数, 窗口每来一个数据就做一次计算
 */
public class Reduce {

    /**
     * 每10秒钟统计一次各个user出现的次数
     */
    @Test
    public void reduce() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> source = env.addSource(new MultiParallelSource())
                // 处理乱序数据, 因为MultiParallelSource中的timestamp本来就是递增的, 所以最大乱序数据可以是0
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                            @Override
                            public long extractTimestamp(Event element, long recordTimestamp) {
                                return element.getTimestamp();
                            }
                        }));
        // 打印生成的数据, 方便调试
        source.print();

        source .map(event -> Tuple2.of(event.getUser(), 1))
            .returns(Types.TUPLE(Types.STRING, Types.INT))
            .keyBy(tuple -> tuple.f0)
            .window(TumblingEventTimeWindows.of(Time.seconds(10)))
            // reduce要求输入类型, 输出类型, 中间结果都需要一致
            .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
                @Override
                public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2)
                    throws Exception {
                    return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                }
            })

            // 除了做reduce操作外, 还可以做一些特定的聚合操作, 下面示例除了表示语法外无任何意义
            // .sum(1)
            // .max(1)
            // .maxBy(1)
            // .min(1)
            // .minBy(1)
            .print();

        env.execute();
    }
}
