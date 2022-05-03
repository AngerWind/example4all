package com.tiger.datastream._6_sink;

import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

import com.tiger.pojo.Event;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SinkToJdbc {

    @Test
    public void sinkToJdbc() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Event> stream = env.fromElements(new Event("Mary", "./home", 1000L),
            new Event("Bob", "./cart", 2000L), new Event("Alice", "./prod?id=100", 3000L),
            new Event("Alice", "./prod?id=200", 3500L), new Event("Bob", "./prod?id=2", 2500L),
            new Event("Alice", "./prod?id=300", 3600L), new Event("Bob", "./home", 3000L),
            new Event("Bob", "./prod?id=1", 2300L), new Event("Bob", "./prod?id=3", 3300L));

        /**
         * 第一个参数: 对于每一个输入需要执行的sql
         * 第二个参数: 通过该参数设置PreparedStatement的占位符
         * 第三个参数: 设置一些执行时的参数, 如batch size和重试次数111111
         * 第四个参数: 设置需要连接的mysql的url, driver, user, passwd
         *
         * create table clicks (user varchar(256), url varchar(256) );
         */
        stream.addSink(JdbcSink.sink("INSERT INTO clicks (user, url) VALUES (?, ?)",
            new JdbcStatementBuilder<Event>() {
                @Override
                public void accept(PreparedStatement preparedStatement, Event event) throws SQLException {
                    preparedStatement.setString(1, event.getUser());
                    preparedStatement.setString(2, event.getUrl());
            }},
            JdbcExecutionOptions.builder()
                .withBatchSize(1000)
                .withBatchIntervalMs(200)
                .withMaxRetries(5)
                .build(),
            new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl("jdbc:mysql://localhost:3306/test")
                .withDriverName("com.mysql.cj.jdbc.Driver")
                .withUsername("username")
                .withPassword("password")
                .build()));

        env.execute();
    }
}
