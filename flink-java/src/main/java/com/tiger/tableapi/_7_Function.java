package com.tiger.tableapi;

import java.time.ZoneId;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/17
 * @description
 */
public class _7_Function {


    @Test
    public void timeFunction() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .withBuiltInCatalogName("default_catalog") // 指定默认使用的catalog, 一个catalog下面可以有多个数据库
                .withBuiltInDatabaseName("default_database") // 指定默认使用的database
                .inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);
        // 设置时区为东八区
        // SET 'table.local-time-zone' = 'Asia/Shanghai';
        tableEnv.getConfig().setLocalTimeZone(ZoneId.of("Asia/Shanghai"));

        /*
         * unix_timestamp: 返回当前时间的unix时间戳, 单位是秒
         */
        tableEnv.sqlQuery("select unix_timestamp()").execute().print();

        /*
         * unix_timestamp(string datetime [, string format]):
         *   将格式为format的字符串datetime, 根据指定的时区转换为unix时间戳, 单位是秒
         *   比如:
         *     SET 'table.local-time-zone' = 'Asia/Shanghai';
         *     -- 返回1
         *     SELECT UNIX_TIMESTAMP('1970-01-01 08:00:01.001', 'yyyy-MM-dd HH:mm:ss.SSS');
         */
        tableEnv.sqlQuery("SELECT UNIX_TIMESTAMP('1970-01-01 08:00:01.001', 'yyyy-MM-dd HH:mm:ss.SSS')").execute().print();

        /*
         * FROM_UNIXTIME(numeric[, string])
         *    将自'1970-01-01 00:00:00’ UTC 以来的秒数, 转换为当前时区的指定格式的字符串, 默认为 ‘yyyy-MM-dd HH:mm:ss’
         *   比如 SELECT UNIX_TIMESTAMP('1970-01-01 08:00:01.001', 'yyyy-MM-dd HH:mm:ss.SSS')在东八区会返回1
         */
        // 1970-01-01 08:00:01
        tableEnv.sqlQuery("select from_unixtime(1, 'yyyy-MM-dd HH:mm:ss')").execute().print();

        /*
         * TO_TIMESTAMP(string1[, string2])
         *   将格式为 string2（默认为：‘yyyy-MM-dd HH:mm:ss’）的字符串 string1 转换为 timestamp，不带时区。
         */
        tableEnv.sqlQuery("select to_timestamp('1970-01-01 08:00:01', 'yyyy-MM-dd HH:mm:ss')").execute().print();

        /*
         * TO_TIMESTAMP_LTZ(numeric, precision)
         *   将自'1970-01-01 00:00:00’ UTC 以来的秒数或者毫秒转换为 TIMESTAMP_LTZ，有效精度为 0 或 3，
         *   0 代表 TO_TIMESTAMP_LTZ(epochSeconds, 0)， 3 代表 TO_TIMESTAMP_LTZ(epochMilliseconds, 3)。
         */
        tableEnv.sqlQuery("select TO_TIMESTAMP_LTZ(1, 0)").execute().print();
    }
}
