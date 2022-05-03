package com.tiger.datastream._3_source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import java.util.ArrayList;

public class SourceFromCollection {

    @Test
    public void sourceFromCollection() {

        // 从多个元素中创建source
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> stringSource = env.fromElements("asdfa", "asdf", "dafa");

        // 从集合中读取source
        DataStreamSource<String> listSource = env.fromCollection(new ArrayList<String>());
    }
}
