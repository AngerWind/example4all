package com.tiger.wc;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author Tiger.Shen
 * @date 2020/12/20 18:47
 */
public class StreamWordCount {

    public static void main(String[] args) throws Exception {
        // 1. 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2. 从文件中读取数据
        // String inputPath = "D:\\example4all\\flink-java\\src\\main\\resources\\hello.txt";
        // DataStreamSource<String> dataStreamSource = env.readTextFile(inputPath);

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        Integer port = parameterTool.getInt("port");

        DataStreamSource<String> dataStreamSource = env.socketTextStream(host, port);

        // 基于数据流进行转换计算
        DataStream<Tuple2<String, Integer>> resultStream = dataStreamSource
                .flatMap(new WordCount.MyFlatMap())
                .keyBy(0)
                .sum(1);

        resultStream.print();
        // 执行任务
        env.execute();
    }
}
