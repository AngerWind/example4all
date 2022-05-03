package com.tiger.datastream._5_transform.aggregation;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import com.tiger.pojo.Event;

public class KeyByOperator {

    @Test
    public void keyBy() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Event> stream =
            env.fromElements(new Event("Mary", "./home", 1000L), new Event("Bob", "./cart", 2000L));

        // 使用 Lambda 表达式
        KeyedStream<Event, String> keyedStream = stream.keyBy(Event::getUser);

        // 使用匿名类实现 KeySelector
        KeyedStream<Event, String> keyedStream1 = stream.keyBy(new KeySelector<Event, String>() {
            @Override
            public String getKey(Event e) throws Exception {
                return e.getUser();
            }
        });
        env.execute();
    }
}
