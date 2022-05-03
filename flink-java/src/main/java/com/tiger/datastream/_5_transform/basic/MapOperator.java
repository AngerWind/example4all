package com.tiger.datastream._5_transform.basic;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

public class MapOperator {

    @Test
    public void map() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Integer> source = env.fromElements(1, 2, 3);

        source.map(num -> {
            return num + 1;
        }).returns(new TypeHint<Integer>() {}).print();

        env.execute();
    }
}
