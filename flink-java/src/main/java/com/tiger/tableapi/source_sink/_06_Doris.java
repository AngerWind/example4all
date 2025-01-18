package com.tiger.tableapi.source_sink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/17
 * @description
 */
public class _06_Doris {

    @Test
    public void writeToDoris() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        tableEnv.executeSql("CREATE TABLE flink_doris (\n" +
                "    siteid INT,\n" +
                "    citycode SMALLINT,\n" +
                "    username STRING,\n" +
                "    pv BIGINT\n" +
                " ) \n" +
                " WITH (\n" +
                "    'connector' = 'doris',\n" +
                "    'fenodes' = 'hadoop1:8030',\n" +
                "    'table.identifier' = 'test_db.table1',\n" +
                "    'username' = 'test',\n" +
                "    'password' = 'test'\n" +
                ")\n");
// 读取数据
// tableEnv.executeSql("select * from flink_doris").print();
// 写入数据
        tableEnv.executeSql("insert into flink_doris(siteid,username,pv) values(22,'wuyanzu',3)");
    }
}
