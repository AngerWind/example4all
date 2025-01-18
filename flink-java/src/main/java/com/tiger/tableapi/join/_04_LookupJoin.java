package com.tiger.tableapi.join;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/15
 * @description
 */
public class _04_LookupJoin {

    /**
     * LookupJoin在执行的时候, 左表每来一条数据, 他都会到外部系统中查询与之对应的数据
     *
     * LookupJoin和基于处理时间的TemporalJoin类似, 都是对左表的数据, 获取右表中最新的数据, 然后与他join
     * 不同的点在于, TemporalJoin中的右表实际上是一条流, 然后再内存中维护对应这个流的表的最新的状态
     * 而LookupJoin 是当左表数据来的时候直接从外部系统中获取与之对应的最新的数据
     *
     * 比如还是订单与汇率的关联
     * 但是此时我们的汇率不再是一条流, 而是直接保存在mysql中
     * 所以当订单表中每来一条数据的时候, 我们都需要去mysql中找到与之对应的汇率, 然后进行join
     *
     * -- Customers is backed by the JDBC connector and can be used for lookup joins
     * CREATE TEMPORARY TABLE Customers (
     *   id INT,
     *   name STRING,
     *   country STRING,
     *   zip STRING
     * ) WITH (
     *   'connector' = 'jdbc',
     *   'url' = 'jdbc:mysql://mysqlhost:3306/customerdb',
     *   'table-name' = 'customers'
     * );
     *
     * -- enrich each order with customer information
     * SELECT o.order_id, o.total, c.country, c.zip
     * FROM Orders AS o
     *   JOIN Customers FOR SYSTEM_TIME AS OF o.proc_time AS c
     *     ON o.customer_id = c.id;
     */
}
