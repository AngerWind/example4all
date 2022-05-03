package com.tiger.datastream._5_transform.aggregation;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import com.tiger.datastream._3_source.custom.SingleParallelSource;
import com.tiger.pojo.Event;

public class ReduceOperator {

    /**
     * 需要统计event中user出现次数最多的user
     */
    public void reduce() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 这里的 SingleParallelSource()使用了之前自定义数据源小节中的 ClickSource()
        env.addSource(new SingleParallelSource())
            // 将 Event 数据类型转换成元组类型, 每次出现记为1
            .map(new MapFunction<Event, Tuple2<String, Long>>() {
                @Override
                public Tuple2<String, Long> map(Event e) throws Exception {
                    return Tuple2.of(e.getUser(), 1L);
                }
            })
            // 使用用户名来进行分流
            .keyBy(r -> r.f0)
            // 每一组统计每个用户的总出现次数
            .reduce(new ReduceFunction<Tuple2<String, Long>>() {
                @Override
                public Tuple2<String, Long> reduce(Tuple2<String, Long> value1, Tuple2<String, Long> value2)
                    throws Exception {
                    // 每到一条数据，用户 pv 的统计值加 1
                    return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                }
            })
                .keyBy(r -> true) // 为每一条数据分配同一个 key，将聚合结果发送到一条流中 去
            .reduce(new ReduceFunction<Tuple2<String, Long>>() {
                @Override
                public Tuple2<String, Long> reduce(Tuple2<String, Long> value1, Tuple2<String, Long> value2)
                    throws Exception {
                    // 将累加器更新为当前最大的 pv 统计值，然后向下游发送累加器的值
                    return value1.f1 > value2.f1 ? value1 : value2;
                }
            }).print();
        env.execute();
    }
}
