package com.tiger.tableapi;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

public class _7_WindowAggregation {

    /**
     * 新老版本滚动窗口聚合
     */
    @Test
    public void tumblingWindow() throws Exception {

        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);

        tableEnv.executeSql("create table event ( `user` string,  `url` string,  `timestamp` bigint," +
                "`event_time` as to_timestamp(from_unixtime(`timestamp` / 1000)), watermark for `event_time` as `event_time` ) " +
                "with ( 'connector' = 'filesystem', 'format' = 'csv', 'path' = 'input/click.txt' )");

        // 进行分组窗口聚合, 老版本的窗口聚合方式
        Table oldWay = tableEnv.sqlQuery("select `user`, count(`user`) as cnt, tumble_start(`event_time`, interval '1' hour), \n" +
                "tumble_end(`event_time`, interval '1' hour) from `event`" +
                " group by `user`, tumble(`event_time`, interval '1' hour )");

        // 使用新的方式进行窗口聚合
        Table newWay = tableEnv.sqlQuery("select `user`, count(`user`) as cnt, window_start, window_end from \n" +
                "table(tumble(table `event`, descriptor(`event_time`), interval '1' hour )) \n" +
                "group by `user`, window_start, window_end");


        tableEnv.toDataStream(oldWay).print("oldWay");
        tableEnv.toDataStream(newWay).print("newWay");
        streamEnv.execute();
    }

    /**
     * 滑动窗口聚合
     */
    @Test
    public void slidingWindow() throws Exception {

        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);

        tableEnv.executeSql("create table event ( `user` string,  `url` string,  `timestamp` bigint," +
                "`event_time` as to_timestamp(from_unixtime(`timestamp` / 1000)), watermark for `event_time` as `event_time` ) " +
                "with ( 'connector' = 'filesystem', 'format' = 'csv', 'path' = 'input/click.txt' )");

        tableEnv.executeSql("select * from event").print();


        // 使用新的方式进行窗口聚合, 1小时窗口, 5分组滑动步长
        Table newWay = tableEnv.sqlQuery("select `user`, count(`user`) as cnt, window_start, window_end from \n" +
                "table(hop(table `event`, descriptor(`event_time`), interval '5' second , interval '10' second )) \n" +
                "group by `user`, window_start, window_end");
        tableEnv.toDataStream(newWay).print("newWay");
        streamEnv.execute();
    }

    /**
     * 累计窗口聚合
     */
    @Test
    public void cumulateWindow() throws Exception {

        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);

        tableEnv.executeSql("create table event ( `user` string,  `url` string,  `timestamp` bigint," +
                "`event_time` as to_timestamp(from_unixtime(`timestamp` / 1000)), watermark for `event_time` as `event_time` ) " +
                "with ( 'connector' = 'filesystem', 'format' = 'csv', 'path' = 'input/click.txt' )");


        // 使用新的方式进行窗口聚合, 1小时窗口, 5分组滑动步长
        Table newWay = tableEnv.sqlQuery("select `user`, count(`user`) as cnt, window_start, window_end from \n" +
                "table(cumulate(table `event`, descriptor(`event_time`), interval '5' minute, interval '1' hour )) \n" +
                "group by `user`, window_start, window_end");
        tableEnv.toDataStream(newWay).print("newWay");
        streamEnv.execute();
    }
}
