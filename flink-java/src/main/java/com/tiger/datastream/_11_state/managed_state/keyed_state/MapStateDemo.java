package com.tiger.datastream._11_state.managed_state.keyed_state;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.sql.Timestamp;

public class MapStateDemo {

    /**
     * 模拟一个滚动窗口。我们要计算的是每一个 url 在每一个窗口中的出现次数。
     */
    @Test
    public void mapState() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream =
            env.addSource(new MultiParallelSource()).assignTimestampsAndWatermarks(WatermarkStrategy
                .<Event>forMonotonousTimestamps().withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                    }
                }));
        // 统计每 10s 窗口内，每个 url 的 count
        stream.keyBy(Event::getUrl).process(new FakeWindowResult(10000L)).print();
        env.execute();
    }

    public static class FakeWindowResult extends KeyedProcessFunction<String, Event, String> {
        // 定义属性，窗口长度
        private Long windowSize;

        public FakeWindowResult(Long windowSize) {
            this.windowSize = windowSize;
        }

        // 声明状态，用 map 保存各个窗口的count值（窗口 start， count）
        MapState<Long, Long> windowPvMapState;

        @Override
        public void open(Configuration parameters) throws Exception {
            windowPvMapState = getRuntimeContext()
                .getMapState(new MapStateDescriptor<Long, Long>("window-pv", Long.class, Long.class));
        }

        @Override
        public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
            // 每来一条数据，就根据时间戳判断属于哪个窗口
            Long windowStart = (value.getTimestamp() / windowSize) * windowSize;
            long windowEnd = windowStart + windowSize;

            // 注册 end -1 的定时器，窗口触发计算,
            // 多次在一个时间点注册定时器, onTimer方法只会调用一次
            ctx.timerService().registerEventTimeTimer(windowEnd - 1);

            // 更新状态中的count值, 进行增量聚合
            if (windowPvMapState.contains(windowStart)) {
                windowPvMapState.put(windowStart, windowPvMapState.get(windowStart) + 1);
            } else {
                windowPvMapState.put(windowStart, 1L);
            }
        }

        // 定时器触发，直接输出统计的 pv 结果
        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            long windowEnd = timestamp + 1;
            Long windowStart = windowEnd - windowSize;
            Long pv = windowPvMapState.get(windowStart);
            out.collect("url: " + ctx.getCurrentKey() + " 访问量: " + pv + " 窗 口 ： " + new Timestamp(windowStart) + " ~ "
                + new Timestamp(windowEnd));
            // 模拟窗口的销毁，清除 map 中的 key
            windowPvMapState.remove(windowStart);
        }
    }
}
