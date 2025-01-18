package com.tiger.datastream;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/12
 * @description
 */
public class PalliTest {


    /**
     * 默认情况下, 如果不指定算子的并行度, 那么算子会使用默认的并行度, 而不是继承上一个算子的并行度
     */
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);

        DataStreamSource<Integer> d1 = env.fromElements(1, 2, 3, 4, 5);
        System.out.println(d1.getParallelism());
        SingleOutputStreamOperator<Integer> d2 = d1.map(x -> x +1 ).setParallelism(2);
        System.out.println(d2.getParallelism());

        SingleOutputStreamOperator<Integer> d3 = d2.map(x -> x + 1);
        System.out.println(d3.getParallelism());

        SingleOutputStreamOperator<Integer> d4 = d3.map(x -> x + 1).setParallelism(6);
        System.out.println(d4.getParallelism());

        SingleOutputStreamOperator<Integer> d5 = d4.map(x -> x + 1);
        System.out.println(d5.getParallelism());

        SingleOutputStreamOperator<Integer> d6 = d5.map(x -> x + 1);
        System.out.println(d6.getParallelism());

        d4.print();

        env.execute();
    }
}
