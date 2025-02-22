package com.tiger.datastream._10_streams_transform;

import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * 合并两条流, 要求流的元素类型是一样的
 */
public class _01_Union {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream1 = env.socketTextStream("hadoop102", 7777).map(data -> {
            String[] field = data.split(",");
            return new Event(field[0].trim(), field[1].trim(), Long.valueOf(field[2].trim()));
        }).assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
            .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                @Override
                public long extractTimestamp(Event element, long recordTimestamp) {
                    return element.getTimestamp();
                }
            }));
        stream1.print("stream1");


        SingleOutputStreamOperator<Event> stream2 = env.socketTextStream("hadoop103", 7777).map(data -> {
            String[] field = data.split(",");
            return new Event(field[0].trim(), field[1].trim(), Long.valueOf(field[2].trim()));
        }).assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
            .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                @Override
                public long extractTimestamp(Event element, long recordTimestamp) {
                    return element.getTimestamp();
                }
            }));
        stream2.print("stream2");


        // 合并两条流
        stream1.union(stream2).process(new ProcessFunction<Event, String>() {
            @Override
            public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
                out.collect(" 水 位 线 ： " + ctx.timerService().currentWatermark());
            }
        }).print();
        env.execute();
    }
}
