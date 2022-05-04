package com.tiger.datastream._11_state.managed_state.keyed_state;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.time.Duration;

public class StateTTL {

    @Test
    public void simpleDemo() {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        env.addSource(new MultiParallelSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                            @Override
                            public long extractTimestamp(Event element, long recordTimestamp) {
                                return element.getTimestamp();
                            }
                        }))
                .keyBy(Event::getUser)
                // 保存状态
                .process(new KeyedProcessFunction<String, Event, Event>() {

                    // 用于保存上一个数据
                    private ValueState<Event> valueState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        ValueStateDescriptor<Event> valueState = new ValueStateDescriptor<>("valueState", Event.class);
                        StateTtlConfig ttlConfig = StateTtlConfig
                                .newBuilder(Time.seconds(10))
                                // 设置ttl的更新类型
                                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                                // 清除操作并不是实时的，所以当状态过期之后还有可能基于存在, 这里设置过期但是未清除的状态是否可读
                                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                                .build();
                        ValueStateDescriptor<String> stateDescriptor = new ValueStateDescriptor<>("my state", String.class);
                                stateDescriptor.enableTimeToLive(ttlConfig);
                        this.valueState = getRuntimeContext().getState(valueState);
                    }

                    @Override
                    public void processElement(Event value, Context ctx, Collector<Event> out) throws Exception {
                        // 输出上一个数据
                        System.out.println("Key: " + ctx.getCurrentKey() + ", 上一个保存的数据是: " + valueState.value());
                        valueState.update(value);
                    }
                }).print();
    }
}
