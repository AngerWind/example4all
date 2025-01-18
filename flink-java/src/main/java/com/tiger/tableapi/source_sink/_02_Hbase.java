package com.tiger.tableapi.source_sink;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.junit.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/15
 * @description
 */
public class _02_Hbase {

    @Test
    public void readFromHbase() {
        TableEnvironment tableEnvironment = TableEnvironment.create(EnvironmentSettings.newInstance().build());
        tableEnvironment.executeSql("CREATE TABLE hTable\n" +
                "(\n" +
                "    rowkey INT,\n" +
                "    family1 ROW <q1 INT >,\n" +
                "    family2 ROW <q2 STRING, q3 BIGINT>,\n" +
                "    family3 ROW <q4 DOUBLE, q5 BOOLEAN, qd6 STRING >,\n" +
                "    PRIMARY KEY (rowkey) NOT ENFORCED\n" +
                ")\n" +
                "WITH (" +
                " \n" +
                "    'connector' = 'hbase-2.2',\n" +
                "    'table-name' = 'namesapce:table-name',\n" +
                "    'zookeeper.quorum' = 'localhost:2181',\n" +
                "    'lookup.async' = 'true', -- 是否启用hbase的异步客户端\n" +
                "    'lookup.cache.max-rows' = '1000', -- 在查询hbase时, 启用缓存(默认关闭), 缓存的最大行数\n" +
                "    'lookup.cache.ttl' = '1 minute', -- 设置每一行数据的ttl\n" +
                "    'lookup.max-retries' = '3' -- 当hbase查询失败时, 重试的次数\n" +
                ");");

        // 从hbase表中查询数据
        tableEnvironment.executeSql("select rowkey, family1.ql, family2.q2  from htable;");

    }

    @Test
    public void writeToHbase() {
        TableEnvironment tableEnvironment = TableEnvironment.create(EnvironmentSettings.newInstance().build());

        /**
         * 这个connector支持中hbase中读写数据, 默认运行在upsert模式下
         *
         * 在创建表的时候, 必须指定主键, 作为hbase的rowkey, 如果没有指定, 将会使用rowkey字段作为默认的主键
         * 在定义列族的时候, 必须使用ROW类型, 然后列名写在列族里面
         *
         * connector有两个可选值: hbase-2.2和hbase-1.4, 分别对应hbase2.2.x和hbase1.4.x
         */
        tableEnvironment.executeSql("CREATE TABLE hTable\n" +
                "(\n" +
                "    rowkey INT,\n" +
                "    family1 ROW <q1 INT >,\n" +
                "    family2 ROW <q2 STRING, q3 BIGINT>,\n" +
                "    family3 ROW <q4 DOUBLE, q5 BOOLEAN, qd6 STRING >,\n" +
                "    PRIMARY KEY (rowkey) NOT ENFORCED\n" +
                ")\n" +
                "WITH (" +
                " 'connector' = 'hbase-2.2',\n" +
                "    'table-name' = 'namesapce:table-name',\n" +
                "    'zookeeper.quorum' = 'localhost:2181'" +
                ");");
        // 向hbase中插入数据, 通过row(value1, value2, ...)的形式来指定插入到列族中的数据
        tableEnvironment.executeSql("INSERT INTO hTable\n" +
                "SELECT rowkey, ROW(f1q1), ROW(f2q2, f2q3), ROW(f3q4, f3q5, f3q6) FROM T;");
    }
}
