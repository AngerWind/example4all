package com.tiger.tableapi.source_sink;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.junit.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/15
 * @description
 */
public class _04_Upsert_Kafka {


    @Test
    public void test() {
        TableEnvironment tableEnvironment = TableEnvironment.create(EnvironmentSettings.newInstance().build());

        /**
         * 在使用upsert_kafka的时候, 他会将kafka中的消息认为是一个changelog
         * 如果他读到的是一个value非空的消息, 那么他会认为这是一个upsert操作, key作为主键
         * 如果他读到的是一个value为空的消息, 那么他认为这是一个delete操作, key为主键
         *
         * 在将流写入到kafka的时候, 如果是一个回撤流, 那么必须使用upsert-kafka
         * 对于delete操作, 他会生成一个value为空的消息, 然后写入到kafka中
         *
         * 在讲回撤流写入到kafka中时, 必须指定主键, 用于kafka中的key
         *
         * key.format和value.format用于指定在读取消息的时候, 如何将消息转换为表数据, 以及在写入消息的时候, 如何将表数据序列化
         */
        tableEnvironment.executeSql("CREATE TABLE pageviews_per_region\n" +
                "(\n" +
                "    user_region STRING,\n" +
                "    pv BIGINT,\n" +
                "    uv BIGINT,\n" +
                "    PRIMARY KEY (user_region) NOT ENFORCED\n" +
                ")\n" +
                "WITH (" +
                " 'connector' = 'upsert-kafka',\n" +
                "    'topic' = 'pageviews_per_region',\n" +
                "    'properties.bootstrap.servers' = 'hadoop102',\n" +
                "    'key.format' = 'avro',\n" +
                "    'value.format' = 'avro'" +
                ");");
    }

}
