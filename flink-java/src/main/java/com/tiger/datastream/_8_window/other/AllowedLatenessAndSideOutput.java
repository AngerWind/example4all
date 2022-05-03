package com.tiger.datastream._8_window.other;

import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Duration;

public class AllowedLatenessAndSideOutput {

    @Test
    public void allowedLatenessAndSideOutput() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.getConfig().setAutoWatermarkInterval(100);

        // 指定socket的hostname和port
        // 可以在linux上使用nc -lk 1111在端口1111上面启动一个socket, 然后往这个source中发数据
        // 数据集在项目根目录下面的click.txt
        DataStreamSource<String> socketTextStream = env.socketTextStream("localhost", 1111);
        SingleOutputStreamOperator<Event> stream = socketTextStream.map(str -> {
            String[] split = str.split(", ");
            return new Event(split[0].trim(), split[1].trim(), Long.valueOf(split[2].trim()));
        })
                // 水位线延迟两秒
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                    }
                }));

        // 输出方便观察
        stream.print();

        // 创建一个侧输出流标记
        OutputTag<Event> lateness = new OutputTag<Event>("lateness"){};

        SingleOutputStreamOperator<String> result = stream.keyBy(Event::getUrl)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                // 延迟一分钟关闭窗口
                .allowedLateness(Time.minutes(1))
                // 迟到数据放入侧输出流
                .sideOutputLateData(lateness)
                .aggregate(new AggregateFunction<Event, Long, Long>() {

                    @Override
                    public Long createAccumulator() {
                        return 0L;
                    }

                    @Override
                    public Long add(Event value, Long accumulator) {
                        return ++accumulator;
                    }

                    @Override
                    public Long getResult(Long accumulator) {
                        return accumulator;
                    }

                    /**
                     * 只有会话窗口有merge操作, 这里不写也无所谓
                     */
                    @Override
                    public Long merge(Long a, Long b) {
                        return null;
                    }
                }, new ProcessWindowFunction<Long, String, String, TimeWindow>() {
                    /**
                     * 这里的输入elements存放着上一步getResult的返回值
                     */
                    @Override
                    public void process(String key, Context context, Iterable<Long> input, Collector<String> out)
                            throws Exception {

                        // 获取url的个数
                        Long urlCount = input.iterator().next();

                        // 获取当前窗口的相关信息
                        long windowEnd = context.window().getEnd();
                        long windowStart = context.window().getStart();

                        // 输出数据
                        out.collect(String.format("窗口[%s, %s),  url:%s, url个数:%d", new Timestamp(windowStart),
                                new Timestamp(windowEnd), key, urlCount));
                    }
                });
        result.print("result");

        DataStream<Event> latenessStream = result.getSideOutput(lateness);
        // 侧输出流打印
        latenessStream.print("late");

        env.execute();

    }
}
