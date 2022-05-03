package com.tiger.datastream._8_window.window_function;

import com.google.common.collect.Sets;
import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Set;

/**
 * 增量聚合函数和全窗口函数结合使用
 */
public class CombineWindowFunction {

    /**
     * 统计当前窗口的所有独立用户数(UV值) 同时使用AggregateFunction和ProcessWindowFunction
     */
    @Test
    public void combine() throws Exception {

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

        // 打印数据方便调试
        source.print();

        source
            // 所有Event发送到同一个分区中
            .keyBy(event -> "key").window(TumblingEventTimeWindows.of(Time.seconds(10)))
            // AggregateFunction的输出作为ProcessWindowFunction的输入
            .aggregate(new AggregateFunction<Event, Set<String>, Long>() {

                @Override
                public Set<String> createAccumulator() {
                    return Sets.newHashSet();
                }

                @Override
                public Set<String> add(Event value, Set<String> accumulator) {
                    accumulator.add(value.getUser());
                    return accumulator;
                }

                @Override
                public Long getResult(Set<String> accumulator) {
                    return (long)accumulator.size();
                }

                /**
                 * 只有会话窗口有merge操作, 这里不写也无所谓
                 */
                @Override
                public Set<String> merge(Set<String> a, Set<String> b) {
                    return null;
                }
            }, new ProcessWindowFunction<Long, String, String, TimeWindow>() {
                /**
                 * 这里的输入elements存放着上一步getResult的返回值
                 */
                @Override
                public void process(String key, Context context, Iterable<Long> input, Collector<String> out)
                    throws Exception {

                    // 获取uv值
                    Long userCount = input.iterator().next();

                    // 获取当前窗口的相关信息
                    long windowEnd = context.window().getEnd();
                    long windowStart = context.window().getStart();

                    // 输出数据
                    out.collect(String.format("窗口[%s, %s),  UV值:%d", new Timestamp(windowStart),
                            new Timestamp(windowEnd), userCount));
                }
            }).print();

        env.execute();
    }


    /**
     * 统计当前窗口的所有url的个数, 需要带时间窗口信息
     */
    @Test
    public void combine1() throws Exception {

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

        // 打印数据方便调试
        source.print();

        source
                // 按照url进行分组
                .keyBy(Event::getUrl).window(TumblingEventTimeWindows.of(Time.seconds(10)))
                // AggregateFunction的输出作为ProcessWindowFunction的输入
                .aggregate(new AggregateFunction<Event, Long, Long>() {

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

                    /**
                     * 只有会话窗口有merge操作, 这里不写也无所谓
                     */
                    @Override
                    public Long merge(Long a, Long b) {
                        return null;
                    }
                }, new ProcessWindowFunction<Long, String, String, TimeWindow>() {
                    /**
                     * 这里的输入elements存放着上一步getResult的返回值
                     */
                    @Override
                    public void process(String key, Context context, Iterable<Long> input, Collector<String> out)
                            throws Exception {

                        // 获取url的个数
                        Long urlCount = input.iterator().next();

                        // 获取当前窗口的相关信息
                        long windowEnd = context.window().getEnd();
                        long windowStart = context.window().getStart();

                        // 输出数据
                        out.collect(String.format("窗口[%s, %s),  url:%s, url个数:%d", new Timestamp(windowStart),
                                new Timestamp(windowEnd), key, urlCount));
                    }
                }).print();

        env.execute();
    }
}
