package com.tiger.tableapi;

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

import com.tiger.datastream._3_source.custom.MultiParallelSource;
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

        Table maryStream =
            eventTable.select($("user"), $("url"), $("timestamp").plus(1)).where($("user").isEqual("Mary"));

        // table转换成dataStream进行输出
        tableEnv.toDataStream(allTable).print();
        tableEnv.toDataStream(maryStream).print();

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

        // Define the item data
        List<Tuple4<String, String, String, Integer>> itemData =
            Arrays.asList(
                    Tuple4.of("ITEM001", "Electronic", "2017-11-11 10:01:01", 70),
                    Tuple4.of("ITEM002", "Electronic", "2017-11-11 10:02:00", 50),
                    Tuple4.of("ITEM003", "Electronic", "2017-11-11 10:03:02", 30),
                    Tuple4.of("ITEM004", "Electronic", "2017-11-11 10:03:03", 60)
            );

        // Create a DataStream from the item data
        DataStream<Tuple4<String, String, String, Integer>> itemDataStream = env.fromCollection(itemData);
        SingleOutputStreamOperator<Tuple4<String, String, String, Integer>> timestampsAndWatermarks = itemDataStream.assignTimestampsAndWatermarks(
                WatermarkStrategy.<Tuple4<String, String, String, Integer>>forBoundedOutOfOrderness(Duration.ofSeconds(5)) // 最大允许的延迟时间
                        .withTimestampAssigner((event, timestamp) -> {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime dateTime = LocalDateTime.parse(event.f2, formatter);
                            return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                        }));

        // Create a Table from the DataStream
        Table itemTable =
            tableEnv.fromDataStream(timestampsAndWatermarks, $("item_id"), $("item_type"), $("listing_time"), $("price"), $("et").rowtime());

        // Register the Table under a name
        tableEnv.createTemporaryView("ItemTable", itemTable);

        // Execute a query on the Table
        TableResult result = tableEnv
            .sqlQuery("SELECT " + "item_id," + "item_type, " + "listing_time, " + "price, \n"
                + "       MAX(price) OVER (\n" + "        PARTITION BY item_type\n" + "        ORDER BY et\n"
                    + "        ROWS BETWEEN 2 preceding AND CURRENT ROW) AS maxPrice\n" + "FROM ItemTable\n" + "\n")
            .execute();

        // Print the query results
        result.print();

    }
}
