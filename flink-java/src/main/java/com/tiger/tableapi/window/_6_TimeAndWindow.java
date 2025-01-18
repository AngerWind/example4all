package com.tiger.tableapi.window;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;

public class _6_TimeAndWindow {

    /**
     * 直接在定义表的时候定义时间属性字段和watermark的生成策略
     */
    @Test
    public void test() throws Exception {
        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);

        // 直接在定义表的时候, 指定当前事件时间的生成策略和watermark的生成策略
        // watermark必须使用一个timestamp(3)数据类型的字段!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // 这里to_timestamp需要传入一个yyyy-MM-dd HH:mm:ss的字符串, 所以需要先调用from_unixtime将bigint转换成string,
        // 而from_unixtime需要传入的是秒, 所以需要根据直接的需求判断如何将bigint转换成秒级别的精度
        tableEnv.executeSql("create table `event` (`user` STRING, `url` string, `timestamp` bigint,"
            + "event_time as to_timestamp(from_unixtime(`timestamp` / 1000)),"
            + "watermark for event_time as event_time - interval '5' second )"
            + "with ('connector' = 'filesystem', 'path' = 'input/click.txt', 'format' = 'csv')");

        // 使用处理时间
        // 这里获取消息被处理时的时间, 然后将其赋值给process_time上面, 形成一个新的字段
        tableEnv.executeSql("create table `event1` ( `user` STRING, `url` string, `timestamp` bigint, process_time as proctime())"
                + "with ('connector' = 'filesystem', 'path' = 'input/click.txt', 'format' = 'csv')");

    }

    /**
     * 在流转换成表的时候指定时间属性
     */
    @Test
    public void test1() throws Exception {
        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);


        // 这里必须定义时间戳的提取方式和watermark的生成策略
        SingleOutputStreamOperator<Event> streamSource = streamEnv.addSource(new MultiParallelSource())
            .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                    }
                }));
        // 这里的rowtime的意思是, 提取消息自身携带的时间戳, 将其赋值给event_time上面, 如果原来的消息结构上已经有event_time了, 那么会覆盖掉
        // 至于数据的时间戳提取和watermark的生成还是需要在上面定义好
        Table table = tableEnv.fromDataStream(streamSource, $("user"), $("url"), $("timestamp"),
                    $("event_time").rowtime());

        // 这里的proctime就是 获取消息被处理时的时间, 然后将其赋值给process_time上面
        Table table1 = tableEnv.fromDataStream(streamSource, $("user"), $("url"), $("timestamp"),
                $("process_time").proctime());

    }
}
