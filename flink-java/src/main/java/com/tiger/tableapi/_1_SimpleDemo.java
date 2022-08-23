package com.tiger.tableapi;

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

public class _1_SimpleDemo {

    @Test
    public void test() throws Exception {

        // 创建流式环境和表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        streamEnv.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);

        // 从流式环境中创建流
        SingleOutputStreamOperator<Event> eventStream = streamEnv.addSource(new MultiParallelSource())
            .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.getTimestamp();
                    }
                }));

        // dataStream转换成table
        Table eventTable = tableEnv.fromDataStream(eventStream);

        // 对table中的数据进行查询, 转换成另外一张表
        Table allTable = tableEnv.sqlQuery("select user, url, `timestamp` from " + eventTable);
        Table maryStream = eventTable.select($("user"), $("url"), $("timestamp").plus(1))
                .where($("user").isEqual("Mary"));

        // table转换成dataStream进行输出
        tableEnv.toDataStream(allTable).print();
        tableEnv.toDataStream(maryStream).print();

        streamEnv.execute();
    }
}
