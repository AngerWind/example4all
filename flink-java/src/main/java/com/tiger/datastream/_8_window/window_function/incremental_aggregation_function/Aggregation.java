package com.tiger.datastream._8_window.window_function.incremental_aggregation_function;

import com.google.common.collect.Sets;
import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.junit.Test;

import java.time.Duration;
import java.util.Set;

/**
 * 增量聚合函数, 窗口每来一个数据就做一次计算
 */
public class Aggregation {

    /**
     * 每10秒钟统计一次各个user的时间戳的平均值
     */
    @Test
    public void reduce() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.addSource(new MultiParallelSource())
            // 处理乱序数据, 因为MultiParallelSource中的timestamp本来就是递增的, 所以最大乱序数据可以是0
            .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                    }
                }))
            .keyBy(Event::getUser)
            .window(TumblingEventTimeWindows.of(Time.seconds(10)))
            // aggregate可以输入类型, 输出类型, 中间结果(累加器)都不一致, 比较通用
            // 三个泛型分别表示输入类型, 中间结果类型, 输出类型
            // 这里的输入类型为Event, 累加器记录了用户名, 所有timestamp的和与次数, 输出类型记录了user与timestamp的平均数
            .aggregate(new AggregateFunction<Event, Tuple3<String, Long, Integer>, Tuple2<String, Long>>() {

                /**
                 * 在开始的时候调用一次 创建一个累加器的初始状态, 也就是初始的中间结果
                 */
                @Override
                public Tuple3<String, Long, Integer> createAccumulator() {
                    return Tuple3.of(null, 0L, 0);
                }

                /**
                 * 每来一个数据调用一次, 传入累加器和当前的数据, 并且返回一个累加器
                 */
                @Override
                public Tuple3<String, Long, Integer> add(Event value, Tuple3<String, Long, Integer> accumulator) {
                    return Tuple3.of(value.getUser(), accumulator.f1 + value.getTimestamp(), accumulator.f2++);
                }

                /**
                 * 获取聚合的最终的结果
                 */
                @Override
                public Tuple2<String, Long> getResult(Tuple3<String, Long, Integer> accumulator) {
                    return Tuple2.of(accumulator.f0, accumulator.f1 / accumulator.f2);
                }

                /**
                 * 只在会话窗口中涉及merge操作
                 */
                @Override
                public Tuple3<String, Long, Integer> merge(Tuple3<String, Long, Integer> a,
                    Tuple3<String, Long, Integer> b) {
                    return Tuple3.of(a.f0, a.f1 + b.f1, b.f2 + a.f2);
                }
            }).print();

        env.execute();
    }

    /**
     * 每10秒钟统计一次用户平均访问次数, 即10秒内所有访问量(pv) / 10秒内独立用户个数
     */
    @Test
    public void test() throws Exception {

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

        // 打印source生成的数据, 方便调试
        source.print();

        source
            // 将所有数据发送到一个分区中
            .keyBy(enent -> true)
            // 设置一个滑动窗口
            .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(2)))
            // 这里的输入类型为Event, 累加器记录了总的访问次数和所有的访问用户, 输出类型代表用户平均访问次数
            .aggregate(new AggregateFunction<Event, Tuple2<Long, Set<String>>, Double>() {

                /**
                 * 在开始的时候调用一次 创建一个累加器的初始状态, 也就是初始的中间结果
                 */
                @Override
                public Tuple2<Long, Set<String>> createAccumulator() {
                    return Tuple2.of(0L, Sets.newHashSet());
                }

                /**
                 * 每来一个数据调用一次, 传入累加器和当前的数据, 并且返回一个累加器
                 * 这里必须使用一个Set去重
                 */
                @Override
                public Tuple2<Long, Set<String>> add(Event value, Tuple2<Long, Set<String>> accumulator) {
                    accumulator.f0++;
                    accumulator.f1.add(value.getUser());
                    return accumulator;
                }

                /**
                 * 获取聚合的最终的结果
                 */
                @Override
                public Double getResult(Tuple2<Long, Set<String>> accumulator) {
                    return (double)accumulator.f0 / accumulator.f1.size();
                }

                /**
                 * 只在会话窗口中设计merge操作
                 */
                @Override
                public Tuple2<Long, Set<String>> merge(Tuple2<Long, Set<String>> a, Tuple2<Long, Set<String>> b) {
                    Set<String> allUser = Sets.newHashSet(a.f1);
                    allUser.addAll(b.f1);
                    return Tuple2.of(a.f0 + b.f0, allUser);
                }
            }).print();

        env.execute();
    }
}
