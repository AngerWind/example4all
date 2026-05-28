package com.tiger;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class Job1 {

    /**
     * 这个测试用例主要用来带有依赖的sql, 打包之后能不能通过webui, 或者RestClusterClient提交
     * 答案是可以的, 只要把当前项目打包为一个fat包, 提交到webui, 或者通过RestClusterClient指定jar包路径, 即可执行成功
     */
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
            .withBuiltInCatalogName("default_catalog") // 指定默认使用的catalog, 一个catalog下面可以有多个数据库
            .withBuiltInDatabaseName("default_database") // 指定默认使用的database
            .inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);

        tableEnv.executeSql("CREATE TABLE account_source (\n" +
            "                         id INT,\n" +
            "                         name STRING,\n" +
            "                         balance INT,\n" +
            "                         PRIMARY KEY (id) NOT ENFORCED\n" +
            ") WITH (\n" +
            "    'connector' = 'mysql-cdc',\n" +
            "    'hostname' = '172.31.80.1',\n" +
            "    'port' = '3306',\n" +
            "    'username' = 'root',\n" +
            "    'password' = '871403165',\n" +
            "    'database-name' = 'test',\n" +
            "    'table-name' = 'account', \n" +
            "    'scan.startup.mode' = 'latest-offset' \n" +
            ");");
        tableEnv.executeSql("CREATE TABLE account_sink (\n" +
            "                              id INT,\n" +
            "                              name STRING,\n" +
            "                              balance INT,\n" +
            "                              PRIMARY KEY (id) NOT ENFORCED\n" +
            ") WITH (\n" +
            "    'connector' = 'jdbc',\n" +
            "    'url' = 'jdbc:mysql://172.31.80.1:3306/test',\n" +
            "    'table-name' = 'account1',\n" +
            "    'username' = 'root',\n" +
            "    'password' = '871403165'\n" +
            ");\n");


        TableResult result = tableEnv.executeSql("insert into account_sink select * from account_source;");
    }
}
