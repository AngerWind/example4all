package com.tiger.wordcount;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
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
public class UnboundedStreamWordCount {

    public static void main(String[] args) throws Exception {
        // 1. 创建流式执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 从命令行参数中获取指定的hostname和port
        // 该工具可以解析命令行中的--key value类型的参数
        // 所以要在命令行参数中指定--host localhost --port 7777
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        Integer port = parameterTool.getInt("port");

        // 2. 从网络端口中读取文本, 需要通过linux的nc -lk 7777启动这个socket服务器
        DataStreamSource<String> textFile = env.socketTextStream(host, port);

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
