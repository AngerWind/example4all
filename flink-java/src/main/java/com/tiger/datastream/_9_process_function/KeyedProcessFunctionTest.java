package com.tiger.datastream._9_process_function;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Duration;

public class KeyedProcessFunctionTest {

    /**
     * 测试处理时间的定时器
     */
    @Test
    public void processTimeTimer() throws Exception {
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

        source.keyBy(Event::getUser).process(new KeyedProcessFunction<String, Event, String>() {
            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
            }

            @Override
            public void close() throws Exception {
                super.close();
            }

            @Override
            public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
                long processingTime = ctx.timerService().currentProcessingTime();
                out.collect(ctx.getCurrentKey() + "数据到达, 到达时间: " + new Timestamp(processingTime));

                // 注册一个处理时间10秒后的定时器
                ctx.timerService().registerProcessingTimeTimer(processingTime + 10 * 1000);
            }

            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                out.collect(ctx.getCurrentKey() + "定时器触发, 触发时间: " + new Timestamp(timestamp));
            }
        }).print();

        env.execute();
    }

    /**
     * 测试事件时间的定时器
     */
    @Test
    public void eventTimeTimer() throws Exception {
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

        source.keyBy(Event::getUser).process(new KeyedProcessFunction<String, Event, String>() {
            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
            }

            @Override
            public void close() throws Exception {
                super.close();
            }

            @Override
            public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
                // timestamp和currentProcessingTime都是处理时间, 但是获取的时间不一样, 所以会有所不同
                Long timestamp = ctx.timestamp();
                long currentProcessingTime = ctx.timerService().currentProcessingTime();

                // 这里获取的watermark其实是上一个数据生成的watermark, 因为watermark是在数据后面的
                // 当前这个数据生成的watermark要下一次调用processElement的时候才能获取到
                long currentWatermark = ctx.timerService().currentWatermark();

                out.collect(ctx.getCurrentKey() + "数据到达"
                        + ", currentTimestamp: " + new Timestamp(timestamp)
                        + ", currentProcessingTime: " + new Timestamp(currentProcessingTime)
                        + ", watermark: " + new Timestamp(currentWatermark));

                // 注册一个事件时间10秒后的定时器
                ctx.timerService().registerEventTimeTimer(currentProcessingTime + 10 * 1000);
            }

            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                out.collect(ctx.getCurrentKey() + "定时器触发, 触发时间: " + new Timestamp(timestamp));
            }
        }).print();

        env.execute();
    }
}
