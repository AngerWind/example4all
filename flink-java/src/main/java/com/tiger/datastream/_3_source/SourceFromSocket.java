package com.tiger.datastream._3_source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import lombok.SneakyThrows;

public class SourceFromSocket {

    @SneakyThrows
    @Test
    public void sourceFromSocket() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 指定socket的hostname和port
        // 可以在linux上使用nc -lk 1111在端口1111上面启动一个socket, 然后往这个source中发数据
        // 数据集在项目根目录下面的click.txt
        DataStreamSource<String> socketTextStream = env.socketTextStream("localhost", 1111);
    }
}
