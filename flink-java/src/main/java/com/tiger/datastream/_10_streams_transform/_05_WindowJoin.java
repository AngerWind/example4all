package com.tiger.datastream._10_streams_transform;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.Test;

import java.time.Duration;

public class _05_WindowJoin {

    /**
     * windows join就是对两条流同时进行开窗
     * 然后等到!!!!!窗口关闭时, 落在同一个窗口的数据进行!!!!inner join
     */
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

        stream1.join(stream2)
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
                .apply(new JoinFunction<Tuple2<String, Integer>, Tuple2<String, Long>, String>() {
                    @Override
                    public String join(Tuple2<String, Integer> first, Tuple2<String, Long> second) throws Exception {
                        return first + ", " + second;
                    }
                })
                .print();

        env.execute();
    }

    /**
     * 同一个窗口内的数据做笛卡尔积
     */
    @Test
    public void join1() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> stream1 =
                env.addSource(new MultiParallelSource())
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                                            @Override
                                            public long extractTimestamp(Event element, long recordTimestamp) {
                                                return element.getTimestamp();
                                            }
                                        }));

        SingleOutputStreamOperator<Event> stream2 =
                env.addSource(new MultiParallelSource())
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                                            @Override
                                            public long extractTimestamp(Event element, long recordTimestamp) {
                                                return element.getTimestamp();
                                            }
                                        }));

        stream1.join(stream2)
                .where(new KeySelector<Event, String>() {
                    @Override
                    public String getKey(Event value) throws Exception {
                        return value.getUser();
                    }
                })
                .equalTo(new KeySelector<Event, String>() {
                    @Override
                    public String getKey(Event value) throws Exception {
                        return value.getUser();
                    }
                })
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                // 在窗口关闭的时候, 两个窗口中的数据做内连接
                .apply(new JoinFunction<Event, Event, String>() {
                    @Override
                    public String join(Event first, Event second) throws Exception {
                        return first + ", " + second;
                    }
                })
                .print();

        env.execute();
    }
}
