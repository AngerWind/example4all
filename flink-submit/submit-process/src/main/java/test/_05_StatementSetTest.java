package test;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.execution.DefaultExecutorServiceLoader;
import org.apache.flink.core.execution.PipelineExecutorServiceLoader;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.apache.flink.streaming.api.environment.RemoteStreamEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamStatementSet;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;


import java.net.URL;


public class _05_StatementSetTest {


    public static void main(String[] args) throws Exception {

        PipelineExecutorServiceLoader executorServiceLoader = new DefaultExecutorServiceLoader(); // 默认就是他

        // 用于指定 RestOptions.ADDRESS 和 JobManagerOptions.ADDRESS
        String host = "172.31.81.41";
        // 用于指定 RestOptions.PORT 和 JobManagerOptions.PORT
        int port = 8081;

        Configuration config = new Configuration();

        // 默认为null, 用于指定 PipelineOptions.JARS
        String[] jarFiles = new String[] {};

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

            // 4. 创建 TableEnvironment
            EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .build();

            StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);

            // 5. SQL：可以是 Kafka + MySQL 这样的真实场景
            tableEnv.executeSql("CREATE TABLE source_table (\n" +
                "    user_id STRING,\n" +
                "    amount DOUBLE,\n" +
                "    ts TIMESTAMP(3),\n" +
                "    WATERMARK FOR ts AS ts - INTERVAL '5' SECOND\n" +
                ") WITH (\n" +
                "    'connector' = 'datagen',\n" +
                "    'rows-per-second' = '1',\n" +
                "    'fields.user_id.length' = '10',\n" +
                "    'fields.amount.min' = '1',\n" +
                "    'fields.amount.max' = '100'\n" +
                ");\n");
            TableResult tableResult = tableEnv.executeSql("CREATE TABLE sink_table (\n" +
                "    user_id STRING,\n" +
                "    amount DOUBLE,\n" +
                "    ts TIMESTAMP(3)\n" +
                ") WITH (\n" +
                "    'connector' = 'print'\n" +
                ");\n");
            TableResult tableResult2 = tableEnv.executeSql("CREATE TABLE sink_table2 (\n" +
                "    user_id STRING,\n" +
                "    amount DOUBLE,\n" +
                "    ts TIMESTAMP(3)\n" +
                ") WITH (\n" +
                "    'connector' = 'print'\n" +
                ");\n");

            // 每次提交都会生成一个独立的作业, 他们之间不相互影响
            // 所以有时候会重复读取数据, 为了让他们在同一个job中, 可以使用StatementSet
            StreamStatementSet statementSet = tableEnv.createStatementSet();
            statementSet.addInsertSql("INSERT INTO sink_table\n" +
                "SELECT user_id, amount, ts\n" +
                "FROM source_table;\n");
            statementSet.addInsertSql("INSERT INTO sink_table2\n" +
                "SELECT user_id, amount, ts\n" +
                "FROM source_table;\n");
            statementSet.execute();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
