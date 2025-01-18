package com.tiger.datastream._7_watermark;

import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import java.time.Duration;

/**
 * flink内嵌的watermark生成策略
 */
public class BuildInWatermarkStrategy {

    /**
     * 在某些情况下, 可能不需要使用watermark
     */
    @Test
    public void noWatermarks() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 设置watermark发送的周期, 单位毫秒
        env.getConfig().setAutoWatermarkInterval(200);

        DataStreamSource<Event> stream = env.fromElements(new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L));

        stream.assignTimestampsAndWatermarks(WatermarkStrategy.noWatermarks());

        env.execute();

    }

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
                    /*
                     * 返回的是毫秒数
                     */
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
                        /*
                         * 返回的是毫秒数
                         */
                        return element.getTimestamp();
                    }
                })).print();

        env.execute();

    }

    /**
     * 下面是WatermarkStratege.forMonotonousTimestamps()和WatermarkStratege.forBoundedOutOfOrderness()
     * 的原理
     */
    @Test
    public void buildingWatermarkStratege() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 设置watermark发送的周期, 单位毫秒
        env.getConfig().setAutoWatermarkInterval(200);

        DataStreamSource<Event> stream = env.fromElements(new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L), new Event("Alice", "./prod?id=100", 3000L),
                new Event("Alice", "./prod?id=200", 3500L), new Event("Bob", "./prod?id=2", 2500L),
                new Event("Alice", "./prod?id=300", 3600L), new Event("Bob", "./home", 3000L),
                new Event("Bob", "./prod?id=1", 2300L), new Event("Bob", "./prod?id=3", 3300L));

        stream.assignTimestampsAndWatermarks(new WatermarkStrategy<Event>() {
            @Override
            public WatermarkGenerator<Event> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                // 这个就是WatermarkStrategy.<Event>forMonotonousTimestamps()的原理, 其实对于升序的数据, 那么他的乱序程度就是0
                // return new BoundedOutOfOrdernessWatermarks<Event>(Duration.ofSeconds(0));

                // 这个就是WatermarkStrategy.<Event>forBoundedOutOfOrderness() 的原理
                return  new BoundedOutOfOrdernessWatermarks<Event>(Duration.ofSeconds(5));
            }


            @Override
            public TimestampAssigner<Event> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
                return new TimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                    }
                };
            }
        }).print();

        env.execute();
    }
}
