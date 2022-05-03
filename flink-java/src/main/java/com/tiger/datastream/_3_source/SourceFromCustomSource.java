package com.tiger.datastream._3_source;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.datastream._3_source.custom.SingleParallelSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

public class SourceFromCustomSource {

    @Test
    public void sourceFromSingleParallelSource() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // SingleParallelSource继承自SourceFunction, 只支持1并行度
        env.addSource(new SingleParallelSource());
    }

    @Test
    public void sourceFromMultiParallelSource() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // MultiParallelSource继承自ParallelSourceFunction, 可以有大于1的并行度
        env.addSource(new MultiParallelSource());
    }
}
