package com.tiger;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.execution.DefaultExecutorServiceLoader;
import org.apache.flink.core.execution.PipelineExecutorServiceLoader;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.apache.flink.streaming.api.environment.RemoteStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.ResultKind;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.Column;
import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;
import org.apache.flink.types.RowKind;
import org.apache.flink.util.CloseableIterator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Job2 {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
            .withBuiltInCatalogName("default_catalog") // 指定默认使用的catalog, 一个catalog下面可以有多个数据库
            .withBuiltInDatabaseName("default_database") // 指定默认使用的database
            .inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);


        // 5. SQL：可以是 Kafka + MySQL 这样的真实场景
            tableEnv.executeSql("CREATE TABLE source_table (\n" +
                "    user_id STRING,\n" +
                "    amount DOUBLE,\n" +
                "    ts TIMESTAMP(3),\n" +
                "    WATERMARK FOR ts AS ts - INTERVAL '5' SECOND\n" +
                ") WITH (\n" +
                "    'connector' = 'datagen',\n" +
                "    'rows-per-second' = '1',\n" +
                "    'fields.user_id.length' = '10',\n" +
                "    'fields.amount.min' = '1',\n" +
                "    'fields.amount.max' = '100'\n" +
                ");\n");
            TableResult tableResult = tableEnv.executeSql("CREATE TABLE sink_table (\n" +
                "    user_id STRING,\n" +
                "    amount DOUBLE,\n" +
                "    ts TIMESTAMP(3)\n" +
                ") WITH (\n" +
                "    'connector' = 'print'\n" +
                ");\n");
        tableEnv.executeSql("insert into sink_table select * from source_table");


    }
}
