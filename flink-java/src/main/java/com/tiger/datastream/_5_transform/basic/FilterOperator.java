package com.tiger.datastream._5_transform.basic;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

public class FilterOperator {

    @Test
    public void filter() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Integer> source = env.fromElements(1, 2, 3, 9);

        source.map(num -> num++)
                .filter(num -> num > 6) // 返回true的保留
                .print();

        env.execute();
    }
}
