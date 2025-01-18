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
public class _03_TemporalJoin {

    /**
     * 使用temporal joins时, 右表被任务是一个时态表, 即数据不停的流向右表, 每个时间点右表的数据都不相同
     * 有点像hbase中的表, 在每个时间点表中数据都不同, 也可以认为是一个多版本的表
     *
     * 想要创建一张时态表, 可以从upsert-kafka中读取数据, 或者使用CDC从右表
     */

    /**
     * 基于事件事件的temporal join
     *
     * 基于事件事件的temporal join, 就是对于每一行左表中的一行数据, 获取其事件时间
     * 然后将右表(通常是维度表)拨回到左表数据对应的事件时间的那个点位上, 然后进行join
     *
     * 常常用在需要对历史数据进行join的时候使用
     *
     *
     * 语法如下:
     * SELECT [column_list]
     * FROM table1 [AS <alias1>]
     * [LEFT] JOIN table2 FOR SYSTEM_TIME AS OF table1.{ proctime | rowtime } [AS <alias2>]
     * ON table1.column-name1 = table2.column-name1
     *
     * 假设我们有一个订单表，每个订单都有不同货币的价格。
     * 为了正确地将该表统一为单一货币（如美元）,每个订单都需要与下单时相应的汇率相关联。
     * -- Create a table of orders. This is a standard
     * -- append-only dynamic table.
     * CREATE TABLE orders (
     *     order_id    STRING,
     *     price       DECIMAL(32,2),
     *     currency    STRING,
     *     order_time  TIMESTAMP(3),
     *     WATERMARK FOR order_time AS order_time - INTERVAL '15' SECOND
     * ) WITH ( ... );
     *      *
     * -- Define a versioned table of currency rates.
     * -- This could be from a change-data-capture
     * -- such as Debezium, a compacted Kafka topic, or any other
     * -- way of defining a versioned table.
     * CREATE TABLE currency_rates (
     *     currency STRING,
     *     conversion_rate DECIMAL(32, 2),
     *     update_time TIMESTAMP(3) METADATA FROM `values.source.timestamp` VIRTUAL,
     *     WATERMARK FOR update_time AS update_time - INTERVAL '15' SECOND,
     *     PRIMARY KEY(currency) NOT ENFORCED
     * ) WITH (
     *     'connector' = 'kafka',
     *     'value.format' = 'debezium-json',
     * ;
     *      *
     * SELECT
     *      order_id,
     *      price,
     *      orders.currency,
     *      conversion_rate,
     *      order_time
     * FROM orders
     * LEFT JOIN currency_rates FOR SYSTEM_TIME AS OF orders.order_time
     * ON orders.currency = currency_rates.currency;
     */

    /**
     * 基于处理时间的temporal join
     *
     * 基于处理时间的temporal join, 就是左表每来一条数据, 都会找到右表此时的最新版本
     * 然后进行join
     *
     * 还是上面的案例, 只是我们不使用下单时的汇率来关联, 而是使用处理时的汇率(即最新版本)来进行关联
     * SELECT
     *     o.order_id,
     *     o.order_time,
     *     r.rate
     * FROM
     *     Orders o
     * JOIN
     *     Lateral Table (Rates(o.order_time)) AS r
     * ON
     *     o.currency = r.currency;
     */
    @Test
    public void test() {
        // 创建流式环境和表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        streamEnv.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);
    }
}
