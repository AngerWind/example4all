package com.tiger.tableapi.test;

import lombok.SneakyThrows;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.junit.Test;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title MySQL2Kafka
 * @date 2022/5/7 16:19
 * @description
 */
public class MySQL2Kafka {

    @Test
    @SneakyThrows
    public void test(){
        TableEnvironment tableEnvironment = TableEnvironment.create(EnvironmentSettings.newInstance().build());

        tableEnvironment.executeSql(
            "CREATE TABLE `appid` (\n" + "`id` int ,\n" + "`" + "a` string,\n" + "`b` string,\n"
                + "PRIMARY KEY (`id`) not enforced\n"
                + ") with (\n" + "    'connector' = 'jdbc',\n" + "    'url' = 'jdbc:mysql://localhost:3308/tenma_test?useUnicode=true&characterEncoding=utf-8&rewriteBatchedStatements=true&generateSimpleParameterMetadata=true&useSSL=false&autoReconnect=true&failOverReadOnly=false',\n" + "    'table-name' = 'appid',\n"
                + "    'driver' = 'com.mysql.cj.jdbc.Driver',\n" + "    'username' = 'root',\n" + "    'password' = '871403165'\n" + ")");

        tableEnvironment.executeSql("select * from appid limit 100").print();


    }
}
