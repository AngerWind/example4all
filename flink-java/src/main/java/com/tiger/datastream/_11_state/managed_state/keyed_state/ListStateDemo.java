package com.tiger.datastream._11_state.managed_state.keyed_state;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;
import org.junit.Test;

public class ListStateDemo {

    /**
     * 相同的key做笛卡尔积
     */
    @Test
    public void listStateDemo() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Tuple3<String, String, Long>> stream1 = env
            .fromElements(Tuple3.of("a", "a", 1000L), Tuple3.of("a", "b", 1000L), Tuple3.of("b", "d", 1000L),
                Tuple3.of("b", "c", 2000L))
            .assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forMonotonousTimestamps()
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> t, long l) {
                        return t.f2;
                    }
                }));
        SingleOutputStreamOperator<Tuple3<String, String, Long>> stream2 = env
            .fromElements(Tuple3.of("a", "d", 3000L), Tuple3.of("a", "c", 3000L), Tuple3.of("b", "e", 3000L),
                Tuple3.of("b", "f", 4000L))
            .assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forMonotonousTimestamps()
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> t, long l) {
                        return t.f2;
                    }
                }));
        stream1.keyBy(r -> r.f0).connect(stream2.keyBy(r -> r.f0))
            // 这里两条KeyedStream做connect , 只有相同的key才会调用同一个CoProcessFunction
            .process(new CoProcessFunction<Tuple3<String, String, Long>, Tuple3<String, String, Long>, String>() {

                // 保存两条流的所有数据
                private ListState<Tuple3<String, String, Long>> stream1ListState;
                private ListState<Tuple3<String, String, Long>> stream2ListState;

                @Override
                public void open(Configuration parameters) throws Exception {
                    super.open(parameters);
                    stream1ListState =
                        getRuntimeContext().getListState(new ListStateDescriptor<Tuple3<String, String, Long>>(
                            "stream1-list", Types.TUPLE(Types.STRING, Types.STRING)));
                    stream2ListState =
                        getRuntimeContext().getListState(new ListStateDescriptor<Tuple3<String, String, Long>>(
                            "stream2-list", Types.TUPLE(Types.STRING, Types.STRING)));
                }

                @Override
                public void processElement1(Tuple3<String, String, Long> left, Context context,
                    Collector<String> collector) throws Exception {
                    stream1ListState.add(left);
                    for (Tuple3<String, String, Long> right : stream2ListState.get()) {
                        collector.collect(left + " => " + right);
                    }
                }

                @Override
                public void processElement2(Tuple3<String, String, Long> right, Context context,
                    Collector<String> collector) throws Exception {
                    stream2ListState.add(right);
                    for (Tuple3<String, String, Long> left : stream1ListState.get()) {
                        collector.collect(left + " => " + right);
                    }
                }
            }).print();
        env.execute();
    }
}
