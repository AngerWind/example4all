package com.tiger.datastream._11_state.managed_state.keyed_state;

import com.google.common.collect.Lists;
import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.*;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.time.Duration;
import java.util.LinkedList;

public class KeyedStateDemo {

    /**
     * 说明各个类型的state的使用
     */
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
                // 用于保存前三个数据
                private ListState<Event> listState;
                // 用于保存所有的url和url出现的次数
                private MapState<String, Long> mapState;
                // 统计所有timestamp的和
                private ReducingState<Event> reducerState;
                // 统计该用户出现的次数
                private AggregatingState<Event, Long> aggregatingState;

                @Override
                public void open(Configuration parameters) throws Exception {
                    valueState =
                        getRuntimeContext().getState(new ValueStateDescriptor<Event>("valueState", Event.class));
                    listState =
                        getRuntimeContext().getListState(new ListStateDescriptor<Event>("listState", Event.class));
                    mapState = getRuntimeContext()
                        .getMapState(new MapStateDescriptor<String, Long>("mapState", String.class, Long.class));
                    reducerState = getRuntimeContext().getReducingState(
                        new ReducingStateDescriptor<Event>("reducerState", new ReduceFunction<Event>() {
                            @Override
                            public Event reduce(Event value1, Event value2) throws Exception {
                                return new Event(value1.getUser(), value1.getUrl(),
                                    value1.getTimestamp() + value2.getTimestamp());
                            }
                        }, Event.class));
                    aggregatingState =
                        getRuntimeContext().getAggregatingState(new AggregatingStateDescriptor<Event, Long, Long>(
                            "aggregatingState", new AggregateFunction<Event, Long, Long>() {
                                @Override
                                public Long createAccumulator() {
                                    return 0L;
                                }

                                @Override
                                public Long add(Event value, Long accumulator) {
                                    return ++accumulator;
                                }

                                @Override
                                public Long getResult(Long accumulator) {
                                    return accumulator;
                                }

                                @Override
                                public Long merge(Long a, Long b) {
                                    return null;
                                }
                            }, Long.class));
                }

                @Override
                public void processElement(Event value, Context ctx, Collector<Event> out) throws Exception {
                    // 输出上一个数据
                    System.out.println("Key: " + ctx.getCurrentKey() + ", 上一个保存的数据是: " + valueState.value());
                    valueState.update(value);

                    // 输出前三个数据
                    LinkedList<Event> events = Lists.newLinkedList(listState.get());
                    // 清空listState
                    listState.clear();
                    for (int i = 0; i < 3 && !events.isEmpty(); i++) {
                        // 取出最后的数据, 并添加进listState中
                        Event event = events.removeLast();
                        System.out.println(String.format("Key: %s, 前第%d个数据是: %s", ctx.getCurrentKey(), i + 1, event));
                        listState.add(event);
                    }

                    // 输出同一个user的所有url的个数
                    mapState.put(value.getUrl(),
                        mapState.get(value.getUrl()) == null ? 1 : mapState.get(value.getUrl()) + 1);
                    mapState.entries().forEach(entry -> {
                        System.out.println(String.format("Key: %s, url: %s, count: %d", ctx.getCurrentKey(),
                            entry.getKey(), entry.getValue()));
                    });

                    // 输出所有timestamp的和
                    reducerState.add(value);
                    Event event = reducerState.get();
                    System.out.println(
                        String.format("Key: %s, 当前所有timestamp的和: %d", ctx.getCurrentKey(), event.getTimestamp()));

                    // 输出当前user出现的次数
                    aggregatingState.add(value);
                    Long count = aggregatingState.get();
                    System.out.println(String.format("Key: %s, 当前user出现的次数: %d", ctx.getCurrentKey(), count));
                }
            }).print();
    }

}
