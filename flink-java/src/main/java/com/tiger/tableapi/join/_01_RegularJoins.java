package com.tiger.tableapi.join;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

import com.tiger.tableapi.join.bean.Dept;
import com.tiger.tableapi.join.bean.Emp;

import java.time.Duration;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/14
 * @description
 */
public class _01_RegularJoins {

    /**
     * flink支持inner join和outer join
     * !!!!! 但是需要特别注意的是, join会导致flink会保存两条流中的数据到状态中, 慢慢的内存就会爆炸
     *
     * 所以一般情况下, 我们会设置状态的ttl, 通过如下api
     * tableEnv.getConfig().setIdleStateRetention(Duration.ofSeconds(60));
     * 上面我们设置空闲的状态为60s
     *
     * 同时还有两种不同的ttl更新策略
     * 1. OnCreateAndWrite: 在创建和更新状态的时候, 会更新ttl
     * 2. OnReadAndWrite: 在对状态读写的时候, 会更新ttl
     *
     * 我们在使用join的时候, 会根据join方式的不同而对左右两个表使用不同ttl更新策略
     *                                   左表                    右表
     *              内连接         OnCreateAndWrite        OnCreateAndWrite
     *              左外连接       OnReadAndWrite          OnCreateAndWrite
     *              右外连接       OnCreateAndWrite        OnReadAndWrite
     *              全外连接       OnReadAndWrite          OnReadAndWrite
     *
     * 还有一点需要注意的是, 因为flink中的表是动态表, 所以我们在执行select的时候, 实际上是一个持续查询的过程
     * 所有select产生的流是一个回撤流
     *
     * 所以我们在执行select的时候, 数据的先来后到也会影响输出的结果
     * 比如我们的left join, 此时先来一条emp数据: 1, 张三, 10, 1000
     * 此时会输出一个: +I, 1, 张三, 10, 1000, null, null, null
     * 之后来一条dept数据, 10, dev, 1000
     * 那么会同时产生两条数据: -D: 1, 张三, 10, 1000, null, null, null 和 +I, 1, 张三, 10, 1000, 10, dev, 1000
     * 其中-D表示回撤上面一条消息
     *
     * 而如果我们调转数据到来的顺序, 先来一条dept数据: 10, dev, 1000
     * 此时不会有任何输出产生
     * 这个时候再来一条emp数据: 1, 张三, 10, 1000
     * 此时会输出+I, 1, 张三, 10, 1000, 10, dev, 1000
     */
    @Test
    public void join() {
        // 创建流式环境和表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        streamEnv.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);

        // 设置空闲的状态的ttl
        tableEnv.getConfig().setIdleStateRetention(Duration.ofSeconds(60));

        SingleOutputStreamOperator<Emp> empDS = streamEnv
                .socketTextStream("hadoop102", 8888)
                .map(new MapFunction<String, Emp>() {
                    @Override
                    public Emp map(String lineStr) throws Exception {
                        String[] fieldArr = lineStr.split(",");
                        return new Emp(Integer.valueOf(fieldArr[0]), fieldArr[1], Integer.valueOf(fieldArr[2]), Long.valueOf(fieldArr[3]));
                    }
                });
        tableEnv.createTemporaryView("emp",empDS);

        SingleOutputStreamOperator<Dept> deptDS = streamEnv
                .socketTextStream("hadoop102", 8889)
                .map(new MapFunction<String, Dept>() {
                    @Override
                    public Dept map(String lineStr) throws Exception {
                        String[] fieldArr = lineStr.split(",");
                        return new Dept(Integer.valueOf(fieldArr[0]), fieldArr[1], Long.valueOf(fieldArr[2]));
                    }
                });
        tableEnv.createTemporaryView("dept",deptDS);

        Table table1 = tableEnv.sqlQuery("select * from emp left join dept on emp.deptno = dept.deptno");
        Table table2 = tableEnv.sqlQuery("select * from emp right join dept on emp.deptno = dept.deptno");
        Table table3 = tableEnv.sqlQuery("select * from emp join dept on emp.deptno = dept.deptno");

        table1.execute().print();
        table2.execute().print();
        table3.execute().print();
    }
}
