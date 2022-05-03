package com.tiger.datastream._9_process_function;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.time.Duration;

public class ProcessFunctionTest {

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

        source.process(new ProcessFunction<Event, String>() {
            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
            }

            @Override
            public void close() throws Exception {
                super.close();
            }

            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                super.onTimer(timestamp, ctx, out);
            }

            @Override
            public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
                if (value.getUser().equals("Mary")) {
                    out.collect(value.getUser());
                } else if (value.getUser().equals("Bob")) {
                    out.collect(value.getUser());
                    out.collect(value.getUser());
                }
                out.collect(value.toString());

                // 获取当前的处理时间
                System.out.println("timestamp: " + ctx.timestamp());
                System.out.println("watermark: " + ctx.timerService().currentWatermark());

                // 在普通的dataStream不能注册定时器, 下面的语句报错
                // ctx.timerService().registerEventTimeTimer(50000);
            }
        }).print();

        env.execute();
    }
}
