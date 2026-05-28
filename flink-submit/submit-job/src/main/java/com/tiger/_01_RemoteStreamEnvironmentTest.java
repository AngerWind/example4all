package com.tiger;



import com.tiger.pojo.Event;
import com.tiger.source.SingleParallelSource;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.execution.DefaultExecutorServiceLoader;
import org.apache.flink.core.execution.PipelineExecutorServiceLoader;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.RemoteStreamEnvironment;


import java.net.URL;

public class _01_RemoteStreamEnvironmentTest {

    public static void main(String[] args) throws Exception {
        /**
         * 实际上并没有什么RemoteStreamEnvironment和LocalStreamEnvironment的区别, 所有功能都在StreamExecutionEnvironment上面
         * RemoteStreamEnvironment和LocalStreamEnvironment这两个类, 只是为了方便设置不同的Configuration
         * 实际上创建的就是StreamExecutionEnvironment, 只是使用的配置不同罢了
         */

        PipelineExecutorServiceLoader executorServiceLoader = new DefaultExecutorServiceLoader(); // 默认就是他

        // 用于指定 RestOptions.ADDRESS 和 JobManagerOptions.ADDRESS
        String host = "172.31.81.41";
        // 用于指定 RestOptions.PORT 和 JobManagerOptions.PORT
        int port = 8081;

        Configuration config = new Configuration();

        // 默认为null, 用于指定 PipelineOptions.JARS
        String[] jarFiles = new String[] {
            "J:\\desktop-shortcut\\example\\example4all\\flink-submit\\submit-job\\target\\submit-job-1.0-SNAPSHOT.jar"};

        // 默认为null, 用于指定 PipelineOptions.CLASSPATHS
        URL[] globalClasspaths = new URL[] {};

        // 默认就是他
        // 会通过 savepointRestoreSettings.allowNonRestoredState() 来设置
        // SavepointConfigOptions.SAVEPOINT_IGNORE_UNCLAIMED_STATE
        // 会通过 savepointRestoreSettings.getRestoreMode() 来设置 SavepointConfigOptions.RESTORE_MODE
        // 会通过 savepointRestoreSettings.getRestorePath() 来设置 SavepointConfigOptions.SAVEPOINT_PATH
        SavepointRestoreSettings savepointRestoreSettings = SavepointRestoreSettings.none();

        try (RemoteStreamEnvironment env = new RemoteStreamEnvironment(executorServiceLoader, host, port, config,
            jarFiles, globalClasspaths, savepointRestoreSettings);) {
            // 这里你可以写你的作业逻辑：
            env.setParallelism(1);
            DataStreamSource<Event> source = env.addSource(new SingleParallelSource());

            SingleOutputStreamOperator<Event> operator = source.filter(new FilterFunction<Event>() {
                @Override
                public boolean filter(Event event) throws Exception {
                    System.out.println("filter: " + event);
                    return true;
                }
            });

            SingleOutputStreamOperator<String> operator1 = operator.map(new MapFunction<Event, String>() {
                @Override
                public String map(Event event) throws Exception {
                    System.out.println("map: " + event);
                    return event.getUser();
                }
            });

            operator1.print();

            /**
             *
             * execute实际上的作用就是生成JobGraph, 然后提交到指定的flink集群上
             * 但是他只是生成JobGraph, 并不会将使用到的类自动上传到flink集群中
             *
             * 所以我们要在jarFiles和globalClasspaths中指定job使用到的其他的类
             * 包括Source, Sink, Pojo, 匿名的算子类(类似上面的MapFunction, FilterFunction), 以及他们使用到的所有的类
             * 都必须包含在jarFile, 或者classpath中
             *
             * 但是不需要包含flink的jar包, 因为集群上面有flink的jar包
             *
             * 所以我们可以先将我们的项目打包为jar包(flink依赖设置为provided), 这样就有了所有的依赖了,
             * 然后在执行这个程序, 并指定jarFile为打包的jar包,
             * 就可以将当前程序提交到远程的flink集群上了
             *
             * 虽然可以成功, 但是比较奇怪, 因为我们先编写jar包, 然后打包, 然后又在执行中指定打包好的jar包
             * 所以一般情况都是, 我们直接打包好jar包, 然后提交到flink集群上
             * flink集群指定我们的jar到classpath中
             * 通过反射来调用main方法, 然后执行execute方法, 来生成JobGraph, 然后提交JobGraph到指定的flink集群上
             */
            // 方法会堵塞, 直到stream执行完毕, 或者被取消, 终止
            // 关闭当前程序之后, 任务依然会在集群上面执行
            JobExecutionResult result = env.execute("Remote Job Example");


            System.out.println("提交任务");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
