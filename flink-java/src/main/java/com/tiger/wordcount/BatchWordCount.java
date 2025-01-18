package com.tiger.wordcount;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * 批处理word count
 * 这一套使用的DataSet API, 已经不推荐使用了, 可以统一使用DataStream API
 */
public class BatchWordCount {

    public static void main(String[] args) throws Exception {

        // 1. 创建批处理执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 2. 从文本中读取数据, 获得一个数据源
        DataSource<String> textFile = env.readTextFile("input/words.txt");

        // 3. 将每一行数据进行分词, 转换成一个二元组类型
        // 每一行做flatMap操作
        // FlatMapOperator的泛型String表示输入类型, Tuple2<String, Long>表示输出类型
        FlatMapOperator<String, Tuple2<String, Long>> flatMapOperator = textFile.flatMap((String line, Collector<Tuple2<String, Long>> collector) -> {
            // 每一行按照空格拆分
            String[] words = line.split(" ");
            Arrays.stream(words).forEach(word -> {
                // 所有的word转换为一个Tuple2进行输出
                collector.collect(new Tuple2<String, Long>(word, 1L));
            });
        })
                // 由于java的泛型擦除, 所以需要手动指定输出类型的泛型
                .returns(Types.TUPLE(Types.STRING, Types.LONG));

        // 4. 按照第一个字段, 也就是word进行分组
        UnsortedGrouping<Tuple2<String, Long>> unsortedGrouping = flatMapOperator.groupBy(0);

        // 5. 分组之后按照第二个字段进行求和
        AggregateOperator<Tuple2<String, Long>> sum = unsortedGrouping.sum(1);

        // 6. 打印输出
        sum.print();

        // 批处理不需要调用execute方法, 流处理需要
        // env.execute();
    }
}
