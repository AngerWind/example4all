package com.tiger.datastream._7_watermark;

import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import com.tiger.pojo.Event;

public class CustomWatermarkStrategy1 {

    /**
     * 自定义watermark的生成策略
     */
    @Test
    public void test() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 设置watermark发送的周期, 单位毫秒, 默认200
        env.getConfig().setAutoWatermarkInterval(200);

        DataStreamSource<Event> stream = env.fromElements(new Event("Mary", "./home", 1000L),
            new Event("Bob", "./cart", 2000L), new Event("Alice", "./prod?id=100", 3000L),
            new Event("Alice", "./prod?id=200", 3500L), new Event("Bob", "./prod?id=2", 2500L),
            new Event("Alice", "./prod?id=300", 3600L), new Event("Bob", "./home", 3000L),
            new Event("Bob", "./prod?id=1", 2300L), new Event("Bob", "./prod?id=3", 3300L));

        // 设置自定义的水位线
        stream.assignTimestampsAndWatermarks(new CustomWatermarkStrategy()).print();

        env.execute();
    }

    /**
     * WatermarkStrategy这个接口融合了WatermarkGeneratorSupplier和TimestampAssignerSupplier这两个接口
     * TimestampAssignerSupplier接口的作用是通过createTimestampAssigner方法返回一个TimestampAssigner, 该TimestampAssigner指示了如何从数据中提取事件时间
     * WatermarkGeneratorSupplier接口的作用是通过createWatermarkGenerator方法返回一个WatermarkGenerator, 该接口指示了应该在什么时候发送watermark
     */
    public static class CustomWatermarkStrategy implements WatermarkStrategy<Event> {

        /**
         * 返回一个TimestampAssigner, 指示了如何提取数据中的事件时间, 继承自{@link TimestampAssignerSupplier}
         */
        @Override
        public TimestampAssigner<Event> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
            return new SerializableTimestampAssigner<Event>() {
                @Override
                public long extractTimestamp(Event element, long recordTimestamp) {
                    // 从数据中提取时间戳
                    return element.getTimestamp();
                }
            };
        }

        /**
         * 返回一个WatermarkGenerator, 指示了在什么时候发送watermark已经watermark的值
         */
        @Override
        public WatermarkGenerator<Event> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
            return new PeriodicGenerator();
        }
    }

    /**
     * 当前类使用周期性发送watermark的策略
     */
    public static class PeriodicGenerator implements WatermarkGenerator<Event> {
        // watermark的延迟时间
        private Long delayTime = 5000L;
        // 观察到的最大时间戳
        private Long maxTs = Long.MIN_VALUE + delayTime + 1L;

        /**
         * 每来一个数据调用一次, 如果在该方法中使用output发送watermark, 说明是每来一个数据发送一个watermark
         */
        @Override
        public void onEvent(Event event, long eventTimestamp, WatermarkOutput output) {
            // 每来一条数据就调用一次
            // 更新最大时间戳
            maxTs = Math.max(event.getTimestamp(), maxTs);
        }

        /**
         * 每个周期调用一次, watermark的发送周期可以通过env.getConfig().setAutoWatermarkInterval(200)设置
         * 如果在该方法中调用output发送watermark, 说明是每个周期发送一个watermark
         */
        @Override
        public void onPeriodicEmit(WatermarkOutput output) {
            // 发射水位线，默认 200ms 调用一次
            //
            output.emitWatermark(new Watermark(maxTs - delayTime - 1L));
        }
    }

    /**
     * 当前类使用断点式生成watermark, 即每来一个数据生成一个watermark
     */
    public class PunctuatedGenerator implements WatermarkGenerator<Event> {
        @Override
        public void onEvent(Event r, long eventTimestamp, WatermarkOutput output) {
            output.emitWatermark(new Watermark(r.getTimestamp() - 1));
        }
        @Override
        public void onPeriodicEmit(WatermarkOutput output) {
            // 不需要做任何事情，因为我们在 onEvent 方法中发射了水位线
        }
    }
}
