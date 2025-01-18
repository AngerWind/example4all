package com.tiger.tableapi.join;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/15
 * @description
 */
public class _02_IntervalJoin {

    /**
     * interval join 和 streamapi中的interval join类似, 都是对左表中的每一条数据, 都取右表中一个区间中的数据进行join
     * 需要注意的是
     *     1. interval join时, 两条流都必须指定wartermark
     *     2. interval join只支持事件时间
     *     2. 两条流的时间属性必须是递增的
     *     3. 两条流都必须是追加流, 而不能是回撤流
     *
     * create table left (
     *   id string,
     *   ts bigint,
     *   watermark for ts as ts - interval 5 seconds
     * ) with (...)
     *
     * create table right (
     *   left_id string,
     *   ts bigint,
     *   watermark for ts as ts - interval 5 seconds
     * ) with (...)
     *
     *
     * SELECT *
     * FROM left l, right r
     * WHERE l.id = r.left_id
     * AND r.time BETWEEN l.time - INTERVAL '4' HOUR AND l.time
     */
    @Test
    public void test() {
        // 创建流式环境和表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        streamEnv.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);
    }
}
