package com.tiger.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author Tiger.Shen
 * @date 2020/12/20 16:41
 */
// 批处理word count
public class WordCount {

    public static void main(String[] args) throws Exception {
        // 1. 创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 2. 从文件中读取数据
        String inputPath = "D:\\example4all\\flink-java\\src\\main\\resources\\hello.txt";
        DataSource<String> dataSource = env.readTextFile(inputPath);

        // 3. 对数据集进行处理, 按空格分词展开，转换成（word， 1）二元组进行统计
        AggregateOperator<Tuple2<String, Integer>> resultSet = dataSource
                .flatMap(new MyFlatMap())
                .groupBy(0) // 按照第一个位置的word分组
                .sum(1);// 按照第二个文字上的数据求和
        resultSet.print();

    }


    public static class MyFlatMap implements FlatMapFunction<String, Tuple2<String, Integer>> {

        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            String[] words = value.split(" ");
            for (String word : words) {
                out.collect(new Tuple2<String, Integer>(word, 1));
            }
        }
    }

}

