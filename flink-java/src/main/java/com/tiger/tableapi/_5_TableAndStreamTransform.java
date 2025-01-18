package com.tiger.tableapi;

import com.tiger.datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.junit.Test;

import static org.apache.flink.table.api.Expressions.$;

/**
 * 表与流之间的转换
 */
public class _5_TableAndStreamTransform {

    @Test
    public void streamToTable() throws Exception {

        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);
        DataStreamSource<Event> streamSource = streamEnv.addSource(new MultiParallelSource());


        // stream转换为table对象,  按照Event类的结构进行转换
        Table event1 = tableEnv.fromDataStream(streamSource);

        // 将table对象注册为表
        tableEnv.createTemporaryView("event1", event1);

        // stream转换为table对象, 转换的过程中, 可以对Event中的字段重命名, 也可以调整字段的位置, 还可以只选取部分字段
        Table event2 = tableEnv.fromDataStream(streamSource, $("timestamp").as("ts"),
                $("user").as("user_name"));


        // stream直接转换为表, 并讲字段进行重命名
        tableEnv.createTemporaryView("event4", streamSource, $("timestamp").as("ts"),
                $("user").as("user_name"));


        // 有时候传过来的流是没有结构的, 那么我们可以直接传入一个Express对象, 来给这个字段命名
        DataStream<String> stream = streamEnv.fromElements("12, 34, 56", "78, 90");
        tableEnv.createTemporaryView("event", stream, $("str"));
    }

    @Test
    public void tableToStream() throws Exception {

        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);
        tableEnv.executeSql("create table `event` (`user` STRING, `url` string, `timestamp` bigint)"
                + "with ('connector' = 'filesystem', 'path' = 'input/click.txt', 'format' = 'csv')");

        // 从表转换为table对象
        Table eventTable = tableEnv.from("event");
        // 将table对象转换为stream
        DataStream<Row> eventStream = tableEnv.toDataStream(eventTable);
        eventStream.print();

        Table countTable = tableEnv.sqlQuery("select `user`, count(*) from event group by `event`");

        // 下面的语句将会报错, 因为group by输出的数据是要更新的, 但是输出到控制台无法更新
        // tableEnv.toDataStream(countTable).print();

        // 下面将group by输出的数据转换成更新日志流, 即将countTable表的更新日志进行输出
        // 输出里面
        tableEnv.toChangelogStream(countTable).print();
    }
}
