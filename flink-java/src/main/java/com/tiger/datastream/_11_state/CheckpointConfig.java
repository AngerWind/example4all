package com.tiger.datastream._11_state;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/12
 * @description
 */
public class CheckpointConfig {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);

        /*
         * 2. 设置检查点相关配置
         */
        // 2.1 开启检查点, 并设置两个检查点开启时间间隔为5000毫秒
        // 检查点使用精准一次, 即底层会进行分界线对齐, 在等待分界线对齐的时候, 会将数据缓存起来, 不进行处理
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        // 2.2 设置检查点的超时时间为60秒
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        // 2.3 设置当前任务被取消后, 是否保留检查点的数据
        // RETAIN_ON_CANCELLATION: 保留检查点数据, 需要手动删除
        // DELETE_ON_CANCELLATION: 删除检查点数据
        // NO_EXTERNALIZED_CHECKPOINTS: 外部的检查点被禁用
        env.getCheckpointConfig().enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        // 2.4 设置上一个检查点结束时间和下一个检查点开启时间的最小间隔
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(2000);

        // 2.6 设置检查点的最大并发数
        // 设置为1, 即表示如果检查点没有完毕, 不能再启动新的检查点
        // 如果设置了setMinPauseBetweenCheckpoints, 那么这个参数就无效了
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);


        /*
         * 3. 设置Flink任务执行失败时, 重启的策略
         */
        // 不执行重启
        // env.setRestartStrategy(RestartStrategies.noRestart());
        // 重启3次, 每次间隔30秒
        // env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, Time.seconds(30)));
        // 30天内运行重启3次, 超过3次就不重启了
        env.setRestartStrategy(RestartStrategies.failureRateRestart(3, Time.days(30), Time.seconds(30)));



        /*
         * 4. 设置状态后端相关
         */
        // 使用EmbeddedRocksDBStateBackend, 他会将算子状态都保存在内嵌的RocksDB中
        // env.setStateBackend(new EmbeddedRocksDBStateBackend());
        // 设置状态后端为HashMapStateBackend, 他会将算子状态都保存在TaskManger的堆内存中
        env.setStateBackend(new HashMapStateBackend());

        /*
         * 设置检查点的保存策略
         */
        // 保存到JobManager的堆内存中, 一般用于测试使用, 可靠性不高
        // env.getCheckpointConfig().setCheckpointStorage(new JobManagerCheckpointStorage());
        // 保存到文件系统中, 并指定保存到hdfs上
        // env.getCheckpointConfig().setCheckpointStorage(new FileSystemCheckpointStorage("hdfs://hadoop102:8020/gmall/checkpoint"));
        // 上面代码的简写
        env.getCheckpointConfig().setCheckpointStorage("hdfs://hadoop102:8020/gmall/checkpoint");

        /*
         * 设置hdfs相关
         */
        // 设置操作hdfs的用户, 如果不设置的话, 默认为当前windows的用户
        // 如果是在linux上的话, 默认是提交任务的用户
        System.setProperty("HADOOP_USER_NAME", "atguigu");
    }
}
