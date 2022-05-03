package com.tiger.datastream._5_transform.basic;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.util.Arrays;

public class FlatMap {

    @Test
    public void flatMap() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = env.fromElements("a b c", "xx ss gg");

        source.flatMap((String str, Collector<String> collector) -> {
            Arrays.stream(str.split(" ")).forEach(collector::collect);
        }).returns(new TypeHint<String>() {}).print();

        env.execute();
    }
}
