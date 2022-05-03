package com.tiger.datastream._6_sink;

import com.tiger.datastream._3_source.custom.SingleParallelSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.junit.Test;

import com.tiger.pojo.Event;

public class SinkToRedis {

    @Test
    public void sinkToRedis() throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 1. 添加一个随机生成数据的source
        DataStreamSource<Event> source = env.addSource(new SingleParallelSource());

        // 2. 创建一个到 redis 连接的配置, 并指定Redis需要的一个Mapper
        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("hadoop102").build();
        source.addSink(new RedisSink<Event>(conf, new MyRedisMapper()));
        env.execute();

    }

    public static class MyRedisMapper implements RedisMapper<Event> {

        /**
         * 针对每一个发送到Redis的Event, 获取redis的key, 或者hash的field
         */
        @Override
        public String getKeyFromData(Event e) {
            return e.getUser();
        }

        /**
         * 针对每一个发送到Redis的Event, 获取redis的value
         */
        @Override
        public String getValueFromData(Event e) {
            return e.getUrl();
        }

        /**
         * 定义写入到redis使用的命令
         * 这里将数据写入到hash, 所以使用hset, 并且指定了hash的key为clicks
         */
        @Override
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.HSET, "clicks");
        }
    }
}
