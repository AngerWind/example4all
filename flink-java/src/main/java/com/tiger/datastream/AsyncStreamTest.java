package com.tiger.datastream;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.streaming.util.retryable.AsyncRetryStrategies.ExponentialBackoffDelayRetryStrategy;
import org.apache.flink.streaming.util.retryable.AsyncRetryStrategies.ExponentialBackoffDelayRetryStrategyBuilder;
import org.apache.flink.streaming.util.retryable.AsyncRetryStrategies.FixedDelayRetryStrategy;
import org.apache.flink.streaming.util.retryable.AsyncRetryStrategies.FixedDelayRetryStrategyBuilder;
import org.apache.flink.streaming.util.retryable.RetryPredicates;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/19
 * @description
 */
public class AsyncStreamTest {

    /**
     * 当flink中的算子需要与外部系统进行交互的时候, 比如从mysql, redis, hbase中查询数据
     * <p>
     * 比如在map算子中与redis进行交互, 那么多个数据在处理的时候其实是同步进行的
     * 即比如等前一个数据查询完redis之后, 才会处理下一个数据
     * 在许多情况下，等待查询redis的时间占据了函数运行的大部分时间
     * <p>
     * 所以在flink中提供了异步处理消息的功能, 可以同时处理多个数据
     * 这样flink就不必等待一个消息与外部系统交互完成之后, 再去处理下一个消息
     * 而是可以同时处理多个消息, 然后这些消息同时与外部系统进行交互
     * <p>
     * 需要注意的是, 外部系统必须要提供异步的客户端, 这样才能够进行异步处理, 否则多个消息与外部系统交互的时候还是要同步排队
     * 如果外部系统没有提供异步的客户端, 可以通过创建多个客户端并使用线程池处理同步调用的方法，将同步客户端转换为有限并发的客户端
     */
    @Test
    public void test() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<String> source = env.fromElements(1, 2, 3, 4, 5).map(String::valueOf);
        /**
         * 将元素作为key, 从redis中查询value
         * 需要使用redis的异步客户端
         */
        RichAsyncFunction<String, JSONObject> asyncFunction = new RichAsyncFunction<String, JSONObject>() {

            /** hbase和redis的异步客户端 */
            private AsyncConnection hbaseAsyncConn;
            private StatefulRedisConnection<String,String> redisAsyncConn;

            @Override
            public void open(Configuration parameters) throws Exception {
                org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
                conf.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104");

                try {
                    hbaseAsyncConn = ConnectionFactory.createAsyncConnection(conf).get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                redisAsyncConn = RedisClient.create("redis://hadoop102:6379/0").connect();
            }

            @Override
            public void close() throws Exception {
                if(hbaseAsyncConn != null && !hbaseAsyncConn.isClosed()){
                    try {
                        hbaseAsyncConn.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if(redisAsyncConn != null && redisAsyncConn.isOpen()){
                    redisAsyncConn.close();
                }
            }

            /**
             * 每来一个元素, 都会调用asyncInvoke的方法
             * !!! 注意, 不是使用多线程调用的, 而是使用一个线程来调用
             */
            @Override
            public void asyncInvoke(String key, final ResultFuture<JSONObject> resultFuture) throws Exception {

                // 设置客户端完成请求后要执行的回调函数
                // 回调函数只是简单地把结果发给 future
                CompletableFuture.supplyAsync(new Supplier<String>() {

                    @Override
                    public String get() {
                        try {
                            // 使用redis异步客户端查询数据
                            RedisAsyncCommands<String, String> asyncCommands = redisAsyncConn.async();
                            return asyncCommands.get(key).get();
                        } catch (InterruptedException | ExecutionException e) {
                            // 显示地处理异常。
                            return null;
                        }
                    }
                }).thenApply((String redisResult) -> {
                    try {
                        AsyncTable<AdvancedScanResultConsumer> table = hbaseAsyncConn.getTable(TableName.valueOf("table_name"));
                        Get get = new Get(Bytes.toBytes(key));
                        Result result = table.get(get).get();
                        List<Cell> cells = result.listCells();
                        if (cells != null && !cells.isEmpty()) {
                            JSONObject jsonObj = new JSONObject();
                            for (Cell cell : cells) {
                                String columnName = Bytes.toString(CellUtil.cloneQualifier(cell));
                                String columnValue = Bytes.toString(CellUtil.cloneValue(cell));
                                jsonObj.put(columnName, columnValue);
                            }
                            return jsonObj;
                        }
                    } catch (Exception e) {
                        return null;
                    }
                    return null;
                }).thenAccept(jsonObject -> {
                    // 出现异常, 调用这个方法
                    // resultFuture.completeExceptionally();

                    resultFuture.complete(Collections.singleton(jsonObject));
                });
            }
        };


        /**
         * 指数级别延迟的重试策略
         */
        ExponentialBackoffDelayRetryStrategy<JSONObject> retryStrategy = new ExponentialBackoffDelayRetryStrategyBuilder<JSONObject>(3,
                10, 100, 2)
                .ifException(RetryPredicates.HAS_EXCEPTION_PREDICATE)
                .build();


        /**
         * 固定延迟的重试策略
         */
        FixedDelayRetryStrategy<JSONObject> fixedDelayRetryStrategy = new FixedDelayRetryStrategyBuilder<JSONObject>(3, 100L) // maxAttempts=3, fixedDelay=100ms
                .ifException(RetryPredicates.HAS_EXCEPTION_PREDICATE)
                .ifResult(RetryPredicates.EMPTY_RESULT_PREDICATE)
                .build();

        // 对source中的每一个元素, 都调用asyncFunction, 异步查询redis
        // timeout: 如果asyncFunction的调用超过了timeout, 就会认为调用超时
        // capacity: flink能够同时处理的消息数量

        // 启用重试, 同时不保证异步执行之后结果的顺序和消息的顺序一致, 即转换前消息的顺序是abc, 转换后可能就变成了bca的顺序了
        SingleOutputStreamOperator<JSONObject> resultStream =
                AsyncDataStream.unorderedWaitWithRetry(source, asyncFunction, 1000, TimeUnit.MILLISECONDS, 100, fixedDelayRetryStrategy);

        // 不启用重试, 同时不保证异步执行之后结果的顺序和消息的顺序一致, 即转换前消息的顺序是abc, 转换后可能就变成了bca的顺序了
        SingleOutputStreamOperator<JSONObject> resultStream1 = AsyncDataStream.unorderedWait(source, asyncFunction, 1000, TimeUnit.MILLISECONDS, 100);

        // 不启用重试, 同时保证异步执行之后结果的顺序和消息的顺序一致, 即转换前消息的顺序是abc, 转换后还是abc的顺序
        AsyncDataStream.orderedWait(source, asyncFunction, 1000, TimeUnit.MILLISECONDS, 100);

        // 启用重试, 同时保证异步执行之后结果的顺序和消息的顺序一致, 即转换前消息的顺序是abc, 转换后还是abc的顺序
        AsyncDataStream.orderedWaitWithRetry(source, asyncFunction, 1000, TimeUnit.MILLISECONDS, 100, fixedDelayRetryStrategy);

    }
}
