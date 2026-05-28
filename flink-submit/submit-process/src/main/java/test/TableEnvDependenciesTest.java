package test;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.execution.DefaultExecutorServiceLoader;
import org.apache.flink.core.execution.PipelineExecutorServiceLoader;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.apache.flink.streaming.api.environment.RemoteStreamEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.ResultKind;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.Column;
import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;
import org.apache.flink.types.RowKind;
import org.apache.flink.util.CloseableIterator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableEnvDependenciesTest {

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

        try (
            RemoteStreamEnvironment env = new RemoteStreamEnvironment(executorServiceLoader, host, port, config,
                jarFiles, globalClasspaths, savepointRestoreSettings);) {

            // 4. 创建 TableEnvironment
            EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .build();

            StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);

            // 5. SQL：可以是 Kafka + MySQL 这样的真实场景
            tableEnv.executeSql("CREATE TABLE MyUserTable (\n" +
                "  id BIGINT,\n" +
                "  name STRING,\n" +
                "  age INT,\n" +
                "  status BOOLEAN,\n" +
                "  PRIMARY KEY (id) NOT ENFORCED\n" +
                ") WITH (\n" +
                "   'connector' = 'jdbc',\n" +
                "   'url' = 'jdbc:mysql://localhost:3306/mydatabase',\n" +
                "   'table-name' = 'users'\n" +
                ");");
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
            tableEnv.executeSql("INSERT INTO sink_table\n" +
                "SELECT user_id, amount, ts\n" +
                "FROM source_table;\n");
            tableEnv.executeSql("INSERT INTO sink_table2\n" +
                "SELECT user_id, amount, ts\n" +
                "FROM source_table;\n");




            // executeSql如果里面是select, 也会提交给集群,
            // sink是CollectSinkFunction, 他会将生成的数据保存缓冲区中,当缓冲区已满时，作业会被反压（backpressure）
            TableResult tableResult1 = tableEnv.executeSql("select * from source_table");
            ResultKind resultKind = tableResult1.getResultKind();
            if (ResultKind.SUCCESS_WITH_CONTENT.equals(resultKind)) {
                ResolvedSchema resolvedSchema = tableResult1.getResolvedSchema();
                ArrayList<String> columnNames = new ArrayList<>();
                List<List<String>> result = new ArrayList<>();
                for (int i = 0; i < resolvedSchema.getColumnCount(); i++) {
                    Optional<Column> optionalColumn = resolvedSchema.getColumn(i);
                    if (optionalColumn.isPresent()) {
                        Column column = optionalColumn.get();
                        String columnName = column.getName();
                        columnNames.add(columnName);

                        DataType dataType = column.getDataType();
                        boolean persisted = column.isPersisted();
                        String summaryString = column.asSummaryString();
                        Optional<String> comment = column.getComment();
                        Optional<String> optionalExtras = column.explainExtras();
                        boolean physical = column.isPhysical();
                    }
                }

                // collect会触发作业的执行
                CloseableIterator<Row> iterator = tableResult1.collect();
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    RowKind kind = row.getKind();
                    if (RowKind.INSERT.equals(kind)) {

                    } else if (RowKind.UPDATE_AFTER.equals(kind)) {

                    } else if (RowKind.UPDATE_BEFORE.equals(kind)) {

                    } else if (RowKind.DELETE.equals(kind)) {

                    }
                    List<String> rowData = new ArrayList<>();
                    for (int i = 0; i < row.getArity(); i++) {
                        Object fieldAs = row.getFieldAs(i); // 这个方法可以使用泛型
                        String stringValue = fieldAs.toString();
                        rowData.add(stringValue);
                    }
                    result.add(rowData);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
