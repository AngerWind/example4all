package com.tiger.datastream._8_window.window_function.incremental_aggregation_function;

import java.time.Duration;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.Test;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;

import lombok.SneakyThrows;

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

        source.map(event -> Tuple2.of(event.getUser(), 1)).returns(Types.TUPLE(Types.STRING, Types.INT))
            .keyBy(tuple -> tuple.f0).window(TumblingEventTimeWindows.of(Time.seconds(10)))
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

    @SneakyThrows
    @Test
    public void test() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Integer> source = env.fromElements(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        WindowedStream<Integer, Integer, TimeWindow> windowedStream = source.assignTimestampsAndWatermarks(WatermarkStrategy.<Integer>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<Integer>() {
                            @Override
                            public long extractTimestamp(Integer element, long recordTimestamp) {
                                return element;
                            }
                        }))
                .keyBy(i -> i % 2)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)));


        windowedStream
                .reduce(new ReduceFunction<Integer>() {
                    @Override
                    public Integer reduce(Integer value1, Integer value2) throws Exception {
                        System.out.println("执行reduce");
                        System.out.println(value1 + " " + value2);
                        return value1 + value2;
                    }
                }, new ProcessWindowFunction<Integer, Object, Integer, TimeWindow>() {
                    @Override
                    public void process(Integer key, ProcessWindowFunction<Integer, Object, Integer, TimeWindow>.Context context, Iterable<Integer> elements, Collector<Object> out) throws Exception {
                        System.out.println(key);
                        TimeWindow window = context.window();
                        System.out.println(window.getStart()); // 返回窗口开始时间的毫秒数
                        System.out.println(window.getEnd()); // 返回窗口结束时间的毫秒数
                        System.out.println(window.maxTimestamp()); // 返回窗口内最大的时间戳, 就是window.getEnd() - 1

                        long processingTime = context.currentProcessingTime(); // 返回当前系统时间的毫秒数
                        System.out.println(processingTime);

                        System.out.println(context.currentWatermark()); // 返回当前watermark的毫秒数

                        if (elements.iterator().hasNext()) {
                            Integer next = elements.iterator().next();
                            System.out.println(next);
                            out.collect(next);
                        }
                    }
                })
                .print();

        env.execute();

        Thread.sleep(1000 * 6000);

    }
}
