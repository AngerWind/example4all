package com.tiger.datastream._5_transform.partition;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

public class Partitioning {

    @Test
    public void shuffle() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> source = environment.addSource(new MultiParallelSource()).setParallelism(1);

        source.shuffle().print().setParallelism(4);

        environment.execute();
    }

    @Test
    public void rebalance() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> source = environment.addSource(new MultiParallelSource()).setParallelism(1);

        source.rebalance().print().setParallelism(4);

        environment.execute();
    }

    @Test
    public void rescale() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> source = environment.addSource(new MultiParallelSource()).setParallelism(2);

        source.rescale().print().setParallelism(4);

        environment.execute();
    }

    @Test
    public void broadcast() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> source = environment.addSource(new MultiParallelSource()).setParallelism(2);

        source.broadcast().print().setParallelism(4);

        environment.execute();
    }

    @Test
    public void global() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> source = environment.addSource(new MultiParallelSource()).setParallelism(2);

        source.global().print().setParallelism(4);

        environment.execute();
    }

    @Test
    public void custom() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> source = environment.addSource(new MultiParallelSource()).setParallelism(2);

        // 通过KeySelector生成一个key, 然后通过Partitioner对key进行分区
        source.partitionCustom(new Partitioner<String>() {
            @Override
            public int partition(String key, int numPartitions) {
                return key.hashCode() % numPartitions;
            }
        }, new KeySelector<Event, String>() {
            @Override
            public String getKey(Event value) throws Exception {
                return value.getUser();
            }
        }).print().setParallelism(4);

        environment.execute();
    }
}
