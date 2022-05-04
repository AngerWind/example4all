package com.tiger.datastream._11_state.managed_state.keyed_state;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.junit.Test;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;

public class ValueStateDemo {

    /**
     * 统计各个user出现的次数, 隔10秒输出一次结果
     */
    @Test
    public void valueStateDemo() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream =
                env.addSource(new MultiParallelSource()).assignTimestampsAndWatermarks(WatermarkStrategy
                        .<Event>forMonotonousTimestamps().withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                            @Override
                            public long extractTimestamp(Event element, long recordTimestamp) {
                                return element.getTimestamp();
                            }
                        }));
        stream.print("input");
        // 统计每个用户的 pv，隔一段时间（10s）输出一次结果
        stream.keyBy(Event::getUser).process(new KeyedProcessFunction<String, Event, String>() {
            // 保存当前user出现的次数
            ValueState<Long> countState;

            // 保存当前定时器的时间
            ValueState<Long> timerTsState;

            @Override
            public void open(Configuration parameters) throws Exception {
                countState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("count", Long.class));
                timerTsState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("timerTs", Long.class));
            }

            @Override
            public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
                // 更新 count 值
                Long count = countState.value();
                if (count == null) {
                    countState.update(1L);
                } else {
                    countState.update(count + 1);
                }

                // 当前timerTsState为null, 说明是第一个数据到来, 需要注册一个定时器
                if (timerTsState.value() == null) {
                    ctx.timerService().registerEventTimeTimer(value.getTimestamp() + 10 * 1000L);
                    timerTsState.update(value.getTimestamp());
                }
            }

            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                out.collect(ctx.getCurrentKey() + " uv: " + countState.value());

                // 注册一个新的定时器
                ctx.timerService().registerEventTimeTimer(timestamp + 10 * 1000);

            }

        }).print();
        env.execute();
    }

}
