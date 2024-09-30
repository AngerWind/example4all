package com.tiger.datastream._10_streams_transform;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.junit.Test;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;

public class _04_Split {

    /**
     * 通过侧输出流来进行分流
     */
    @Test
    public void splitStreamByOutput() throws Exception {

        OutputTag<Tuple3<String, String, Long>> MaryTag = new OutputTag<Tuple3<String, String, Long>>("Mary") {};
        OutputTag<Tuple3<String, String, Long>> BobTag = new OutputTag<Tuple3<String, String, Long>>("Bob") {};

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream = env.addSource(new MultiParallelSource());
        SingleOutputStreamOperator<Event> processedStream = stream.process(new ProcessFunction<Event, Event>() {
            @Override
            public void processElement(Event value, Context ctx, Collector<Event> out) throws Exception {
                if ("Mary".equals(value.getUser())) {
                    ctx.output(MaryTag, new Tuple3<>(value.getUser(), value.getUrl(), value.getTimestamp()));
                } else if ("Bob".equals(value.getUser())) {
                    ctx.output(BobTag, new Tuple3<>(value.getUser(), value.getUrl(), value.getTimestamp()));
                } else {
                    out.collect(value);
                }
            }
        });
        processedStream.getSideOutput(MaryTag).print("Mary");
        processedStream.getSideOutput(BobTag).print("Bob");
        processedStream.print("else");
        env.execute();

    }

    /**
     * 通过filter来做分流, 该方法可以做, 但是不可取
     */
    @Test
    public void splitStreamByFilter() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream = env.addSource(new MultiParallelSource());
        // 筛选 Mary 的浏览行为放入 MaryStream 流中
        DataStream<Event> MaryStream = stream.filter(new FilterFunction<Event>() {
            @Override
            public boolean filter(Event value) throws Exception {
                return value.getUser().equals("Mary");
            }
        });
        // 筛选 Bob 的购买行为放入 BobStream 流中
        DataStream<Event> BobStream = stream.filter(new FilterFunction<Event>() {
            @Override
            public boolean filter(Event value) throws Exception {
                return value.getUser().equals("Bob");
            }
        });
        // 筛选其他人的浏览行为放入 elseStream 流中
        DataStream<Event> elseStream = stream.filter(new FilterFunction<Event>() {
            @Override
            public boolean filter(Event value) throws Exception {
                return !value.getUser().equals("Mary") && !value.getUser().equals("Bob");
            }
        });
        MaryStream.print("Mary pv");
        BobStream.print("Bob pv");
        elseStream.print("else pv");
        env.execute();
    }

}
