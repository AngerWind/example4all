package com.tiger._02_datastream._3_source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import java.util.ArrayList;

public class SourceFromCollection {

    @Test
    public void sourceFromCollection() throws Exception {

        // 从多个元素中创建source
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> stringSource = env.fromElements("asdfa", "asdf", "dafa");
        stringSource.print();

        ArrayList<String> strings = new ArrayList<>();
        strings.add("asdfa");
        strings.add("asdf");
        strings.add("dafa");
        // 从集合中读取source
        DataStreamSource<String> listSource = env.fromCollection(strings);
        listSource.print();

        env.execute();
    }
}
