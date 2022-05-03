package com.tiger.wordcount;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * @author Tiger.Shen
 * @date 2020/12/20 18:47
 */
public class BoundedStreamWordCount {

    public static void main(String[] args) throws Exception {
        // 1. 创建流式执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2. 从文件中读取数据
        DataStreamSource<String> textFile = env.readTextFile("input/words.txt");

        // 3. 对文件的每一行进行分词
        SingleOutputStreamOperator<Tuple2<String, Long>> flatMapOperator = textFile.flatMap((String line, Collector<Tuple2<String, Long>> collector) -> {
            String[] words = line.split(" ");
            Arrays.stream(words).forEach(word -> {
                collector.collect(Tuple2.of(word, 1L));
            });
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));

        // 4. 对每个Tuple进行分组, 这里需要的是一个KeySelector的实现
        KeyedStream<Tuple2<String, Long>, String> keyedStream = flatMapOperator.keyBy((Tuple2<String, Long> tuple2) -> tuple2.f0);

        // 5. 分组之后按照第二个字段进行求和
        SingleOutputStreamOperator<Tuple2<String, Long>> sum = keyedStream.sum(1);

        // 6. 打印输出
        sum.print();

        // 7. 执行任务
        env.execute();
    }
}
