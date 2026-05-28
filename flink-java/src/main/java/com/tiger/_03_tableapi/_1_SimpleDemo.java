package com.tiger._03_tableapi;

import static org.apache.flink.table.api.Expressions.$;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

import com.tiger._02_datastream._3_source.custom.MultiParallelSource;
import com.tiger.pojo.Event;

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

        // !!!! 调用Table.toString()方法会自动的将table注册的env中!!!!
        // 对table中的数据进行查询, 转换成另外一张表
        Table allTable = tableEnv.sqlQuery("select user, url, `timestamp` from " + eventTable);

        Table maryStream = eventTable.select($("user"), $("url"), $("timestamp").plus(1))
                .where($("user").isEqual("Mary"));

        // table转换成dataStream进行输出
        tableEnv.toDataStream(allTable).print();
        tableEnv.toDataStream(maryStream).print();

        // 因为这里将 table 转换为了 dataStream, 所以要调用execute方法来执行
        // 如果你仅仅是使用了table api, 而没有转换为dataStream, 那么就不需要调用execute方法来执行
        streamEnv.execute();
    }

    public static void main(String[] args) {
        // Set up the execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .withBuiltInCatalogName("default_catalog") // 指定默认使用的catalog, 一个catalog下面可以有多个数据库
                .withBuiltInDatabaseName("default_database") // 指定默认使用的database
                .inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);

        // 定义流数据
        List<Tuple4<String, String, String, Integer>> itemData =
            Arrays.asList(Tuple4.of("ITEM001", "Electronic", "2017-11-11 10:01:01", 70),
                Tuple4.of("ITEM002", "Electronic", "2017-11-11 10:02:00", 50),
                Tuple4.of("ITEM003", "Electronic", "2017-11-11 10:03:02", 30),
                Tuple4.of("ITEM004", "Electronic", "2017-11-11 10:03:03", 60));

        // 创建一个流
        DataStream<Tuple4<String, String, String, Integer>> itemDataStream = env.fromCollection(itemData);
        SingleOutputStreamOperator<Tuple4<String, String, String, Integer>> timestampsAndWatermarks =
            itemDataStream.assignTimestampsAndWatermarks(WatermarkStrategy
                .<Tuple4<String, String, String, Integer>>forBoundedOutOfOrderness(Duration.ofSeconds(5)) // 最大允许的延迟时间
                .withTimestampAssigner((event, timestamp) -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(event.f2, formatter);
                    return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }));

        // 根据流来创建一个表, 可以选择流中的几个字段来创建表
        Table itemTable = tableEnv.fromDataStream(timestampsAndWatermarks,
            $("item_id"),
            $("item_type"),
            $("listing_time"),
            $("price"),
            $("et").rowtime());

        // 注册临时表
        tableEnv.createTemporaryView("ItemTable", itemTable);

        // 生成一个查询
        Table table = tableEnv.sqlQuery("SELECT " + "item_id," + "item_type, " + "listing_time, " + "price, \n"
            + "       MAX(price) OVER (\n" + "        PARTITION BY item_type\n" + "        ORDER BY et\n"
            + "        ROWS BETWEEN 2 preceding AND CURRENT ROW) AS maxPrice\n" + "FROM ItemTable\n" + "\n");


        /**
         * Table对象实际上是对select操作的封装, 底层并不会去执行这个操作
         * 他的作用有三个:
         *     1. 将Table注册到tableEnv中, 这样就是创建一个env中的表, 类似 create table as select ...
         *     2. 从Table对象转换为另外一个Table对象, 相当于封装了一层逻辑, 底层还是不会执行查询
         *     3. 调用Table的toDataStream/toChangelogStream方法, 转换为流
         *     4. 调用Table的execute方法, 真正的执行查询, 获取TableResult, 然后可以调用TableResult.print()方法来打印结果
         */
        // 执行的执行select查询
        TableResult tableResult = table.execute();

        // 打印结果到控制台上
        tableResult.print();

    }
}
