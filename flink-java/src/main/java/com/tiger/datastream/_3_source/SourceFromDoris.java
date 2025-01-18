package com.tiger.datastream._3_source;

import java.util.Properties;

import org.apache.doris.flink.cfg.DorisStreamOptions;
import org.apache.doris.flink.datastream.DorisSourceFunction;
import org.apache.doris.flink.deserialization.SimpleListDeserializationSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/18
 * @description
 */
public class SourceFromDoris {

    @Test
    public void test() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.put("fenodes","hadoop1:8030");
        properties.put("username","test");
        properties.put("password","test");
        properties.put("table.identifier","test_db.table1");

        env.addSource(new DorisSourceFunction(new DorisStreamOptions(properties),
                new SimpleListDeserializationSchema()))
        .print();
    }
}
