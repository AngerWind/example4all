package com.tiger._02_datastream._3_source;

import com.tiger._02_datastream._3_source.custom.MultiParallelSource;
import com.tiger._02_datastream._3_source.custom.SingleParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

public class SourceFromCustomSource {

    @Test
    public void sourceFromSingleParallelSource() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // SingleParallelSource继承自SourceFunction, 只支持1并行度
        DataStreamSource<Event> source = env.addSource(new SingleParallelSource());

        source.print();

        env.execute();
    }

    @Test
    public void sourceFromMultiParallelSource() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // MultiParallelSource继承自ParallelSourceFunction, 可以有大于1的并行度
        DataStreamSource<Event> source = env.addSource(new MultiParallelSource());

        source.print();

        env.execute();
    }
}
