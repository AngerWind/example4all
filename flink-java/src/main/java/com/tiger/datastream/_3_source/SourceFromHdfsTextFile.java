package com.tiger.datastream._3_source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import lombok.SneakyThrows;

public class SourceFromHdfsTextFile {

    @SneakyThrows
    @Test
    public void sourceFromHdfsTextFile() {

        // 不仅可以从本地读取文件, 还可以从hdfs上面读取文件
        // 但是需要导入hadoop-client maven包
        // 路径使用hdfs://path/to/file.txt
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> fileSource =
                env.readTextFile("hdfs://flinkhadoop:9000/user/wuhulala/input/core-site.xml");

    }
}
