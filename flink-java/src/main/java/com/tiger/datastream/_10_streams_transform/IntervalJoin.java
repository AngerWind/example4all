package com.tiger.datastream._10_streams_transform;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import com.tiger.pojo.Event;
import org.junit.Test;

public class IntervalJoin {

    /**
     * 间隔连接示例 订单与用户的浏览记录之间做间隔连接 查看用户下单之前一段时间的浏览记录
     */
    @Test
    public void test() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 用户订单流
        SingleOutputStreamOperator<Tuple3<String, String, Long>> orderStream = env
            .fromElements(Tuple3.of("Mary", "order-1", 5000L), Tuple3.of("Alice", "order-2", 5000L),
                Tuple3.of("Bob", "order-3", 20000L), Tuple3.of("Alice", "order-4", 20000L),
                Tuple3.of("Cary", "order-5", 51000L))
            .assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forMonotonousTimestamps()
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> element, long recordTimestamp) {
                        return element.f2;
                    }
                }));

        // 用户浏览记录流
        SingleOutputStreamOperator<Event> clickStream = env
            .fromElements(new Event("Bob", "./cart", 2000L), new Event("Alice", "./prod?id=100", 3000L),
                new Event("Alice", "./prod?id=200", 3500L), new Event("Bob", "./prod?id=2", 2500L),
                new Event("Alice", "./prod?id=300", 36000L), new Event("Bob", "./home", 30000L),
                new Event("Bob", "./prod?id=1", 23000L), new Event("Bob", "./prod?id=3", 33000L))
            .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps()
                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                    }
                }));

        // 做间隔连接, 输出下订单之前一段时间的用户浏览记录
        orderStream.keyBy(data -> data.f0).intervalJoin(clickStream.keyBy(Event::getUser))
            // 设置间隔, 下单前5秒钟
            .between(Time.seconds(-5), Time.seconds(0))
            // 默认是上面的窗口左右都是闭区间, 现在设置左边开区间
            .lowerBoundExclusive()
            // 现在设置右边开区间
            .upperBoundExclusive()
            // 下面做处理
            .process(new ProcessJoinFunction<Tuple3<String, String, Long>, Event, String>() {
                @Override
                public void processElement(Tuple3<String, String, Long> left, Event right, Context ctx,
                    Collector<String> out) throws Exception {
                    out.collect(right + " => " + left);
                }
            }).print();
        env.execute();
    }
}
