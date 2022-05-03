package com.tiger.datastream._8_window.window_function.full_window_function;

import com.google.common.collect.Sets;
import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Set;

/**
 * 全窗口函数, 数据统一收集之后做一次批处理计算
 */
public class FullWindowFunction {

    /**
     * 统计当前窗口的所有独立用户数(UV值)
     * 使用老版本的apply()和WindowFunction
     */
    @Test
    public void applyWithWindowFunction() throws Exception {

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

        // 打印数据流中的数据, 方便调试
        source.print();

        source
                // 所有Event发送到同一个分区中
                .keyBy(event -> "key")
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                // 第一个泛型表示输入, 第二个表示输出, 第三表示keyBy中使用的key
                .apply(new WindowFunction<Event, String, String, TimeWindow>() {
                    @Override
                    public void apply(String key, TimeWindow window, Iterable<Event> input, Collector<String> out) throws Exception {
                        // 所有的user放入到set中去重
                        Set<String> allUser = Sets.newHashSet();
                        input.forEach(event -> allUser.add(event.getUser()));

                        // 获取当前窗口的相关信息
                        long windowEnd = window.getEnd();
                        long windowStart = window.getStart();

                        // 输出数据
                        out.collect(String.format("窗口[%s, %s),  UV值:%d", new Timestamp(windowStart),
                                new Timestamp(windowEnd), allUser.size()));
                    }
                })
                .print();

        env.execute();
    }

    /**
     * 统计当前窗口的所有独立用户数(UV值)
     * 使用新版本的process()和ProcessWindowFunction
     * 相较于老版本的apply()方法可以获取到更多的信息
     */
    @Test
    public void processWithProcessWindowFunction() throws Exception {

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
        source
                // 所有Event发送到同一个分区中
                .keyBy(event -> "key")
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .process(new ProcessWindowFunction<Event, String, String, TimeWindow>() {
                    @Override
                    public void process(String key, Context context, Iterable<Event> input, Collector<String> out) throws Exception {
                        // 所有的user放入到set中去重
                        Set<String> allUser = Sets.newHashSet();
                        input.forEach(event -> allUser.add(event.getUser()));

                        // 获取当前窗口的相关信息
                        long windowEnd = context.window().getEnd();
                        long windowStart = context.window().getStart();

                        // 输出数据
                        out.collect(String.format("窗口[%s, %s),  UV值:%d", new Timestamp(windowStart),
                                new Timestamp(windowEnd), allUser.size()));
                    }
                })
                .print();

        env.execute();
    }
}
