package com.tiger.datastream._5_transform.aggregation;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class AggregationOperator {

    public void aggregateByName() throws Exception {
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Tuple2<String, Integer>> stream = env.fromElements(
                Tuple2.of("a", 1),
                Tuple2.of("a", 3),
                Tuple2.of("b", 3),
                Tuple2.of("b", 4)
        );

        // 通过名称指定要聚合的字段
        stream.keyBy(r -> r.f0).sum("f1").print();
        stream.keyBy(r -> r.f0).max("f1").print();
        stream.keyBy(r -> r.f0).min("f1").print();
        stream.keyBy(r -> r.f0).maxBy("f1").print();
        stream.keyBy(r -> r.f0).minBy("f1").print();
        env.execute();
    }

    public void aggregateByIndex() throws Exception {
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Tuple2<String, Integer>> stream = env.fromElements(
                Tuple2.of("a", 1),
                Tuple2.of("a", 3),
                Tuple2.of("b", 3),
                Tuple2.of("b", 4)
        );

        // 通过索引指定需要聚合的字段
        stream.keyBy(r -> r.f0).sum(1).print();
        stream.keyBy(r -> r.f0).max(1).print();
        stream.keyBy(r -> r.f0).min(1).print();
        stream.keyBy(r -> r.f0).maxBy(1).print();
        stream.keyBy(r -> r.f0).minBy(1).print();

        env.execute();
    }
}
