package com.tiger.datastream._10_streams_transform;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.time.Duration;


/**
 * 和Window Join类似, 执行传入的不是一个一个的键值对
 * 传入的是, 同一个窗口中两条流的集合, 可以自己来实现inner join, outer join
 */
public class _07_WindowCoGroup {

    @Test
    public void join() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Tuple2<String, Integer>> stream1 =
                env.fromElements(Tuple2.of("a", 3000), Tuple2.of("b", 4000), Tuple2.of("a", 4500), Tuple2.of("b", 5500))
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy.<Tuple2<String, Integer>>forBoundedOutOfOrderness(Duration.ZERO)
                                        .withTimestampAssigner(new SerializableTimestampAssigner<Tuple2<String, Integer>>() {
                                            @Override
                                            public long extractTimestamp(Tuple2<String, Integer> element, long recordTimestamp) {
                                                return element.f1;
                                            }
                                        }))
                        .returns(Types.TUPLE(Types.STRING, Types.INT));

        SingleOutputStreamOperator<Tuple2<String, Long>> stream2 =
                env.fromElements(Tuple2.of("a", 3000L), Tuple2.of("b", 4000L), Tuple2.of("a", 4500L), Tuple2.of("b", 5500L))
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy.<Tuple2<String, Long>>forBoundedOutOfOrderness(Duration.ZERO)
                                        .withTimestampAssigner(new SerializableTimestampAssigner<Tuple2<String, Long>>() {
                                            @Override
                                            public long extractTimestamp(Tuple2<String, Long> element, long recordTimestamp) {
                                                return element.f1;
                                            }
                                        }))
                        .returns(Types.TUPLE(Types.STRING, Types.LONG));

        stream1.keyBy(tuple2 -> tuple2.f0)
                .coGroup(stream2.keyBy(tuple2 -> tuple2.f0))
                .where(new KeySelector<Tuple2<String, Integer>, String>() {
                    @Override
                    public String getKey(Tuple2<String, Integer> value) throws Exception {
                        return value.f0;
                    }
                })
                .equalTo(new KeySelector<Tuple2<String, Long>, String>() {
                    @Override
                    public String getKey(Tuple2<String, Long> value) throws Exception {
                        return value.f0;
                    }
                })
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .apply(new CoGroupFunction<Tuple2<String, Integer>, Tuple2<String, Long>, String>() {
                    @Override
                    public void coGroup(Iterable<Tuple2<String, Integer>> first, Iterable<Tuple2<String, Long>> second, Collector<String> out) throws Exception {

                    }
                })
                .print();

        env.execute();
    }
}
