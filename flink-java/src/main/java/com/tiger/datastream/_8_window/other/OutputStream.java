package com.tiger.datastream._8_window.other;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.junit.Test;

import java.util.Objects;

public class OutputStream {

    @Test
    public void test() {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> stream = env.addSource(new MultiParallelSource());

        OutputTag<String> outputTag = new OutputTag<String>("output") {};
        SingleOutputStreamOperator<String> longStream =
            stream.map(Objects::toString).process(new ProcessFunction<String, String>() {
                @Override
                public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
                    // 输出到主流中
                    out.collect(value);

                    // 输出到侧输出流中
                    ctx.output(outputTag, "side-output: " + value);
                }
            });
    }
}
