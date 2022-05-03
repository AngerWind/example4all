package com.tiger.datastream._7_watermark;

import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import java.time.Duration;

/**
 * flink内嵌的watermark生成策略
 */
public class InnerWatermarkStrategy {

    /**
     * 处理顺序的数据流, 即数据流中的时间戳单调递增
     */
    @Test
    public void forMonotonousTimestamps() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 设置watermark发送的周期, 单位毫秒
        env.getConfig().setAutoWatermarkInterval(200);

        DataStreamSource<Event> stream = env.fromElements(new Event("Mary", "./home", 1000L),
            new Event("Bob", "./cart", 2000L), new Event("Alice", "./prod?id=100", 3000L),
            new Event("Alice", "./prod?id=200", 3500L), new Event("Bob", "./prod?id=2", 2500L),
            new Event("Alice", "./prod?id=300", 3600L), new Event("Bob", "./home", 3000L),
            new Event("Bob", "./prod?id=1", 2300L), new Event("Bob", "./prod?id=3", 3300L));

        // forMonotonousTimestamps针对有序的数据流
        stream.assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps()
            .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                @Override
                public long extractTimestamp(Event element, long recordTimestamp) {
                    return element.getTimestamp();
                }
            })).print();

        env.execute();

    }

    /**
     * 处理乱序的数据流, 即数据流中的时间戳是乱序的
     */
    @Test
    public void forBoundedOutOfOrderness() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 设置watermark发送的周期, 单位毫秒
        env.getConfig().setAutoWatermarkInterval(200);

        DataStreamSource<Event> stream = env.fromElements(new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L), new Event("Alice", "./prod?id=100", 3000L),
                new Event("Alice", "./prod?id=200", 3500L), new Event("Bob", "./prod?id=2", 2500L),
                new Event("Alice", "./prod?id=300", 3600L), new Event("Bob", "./home", 3000L),
                new Event("Bob", "./prod?id=1", 2300L), new Event("Bob", "./prod?id=3", 3300L));

        // forBoundedOutOfOrderness针对乱序的数据流, 传入数据流的最大乱序程度
        stream.assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                    }
                })).print();

        env.execute();

    }
}
