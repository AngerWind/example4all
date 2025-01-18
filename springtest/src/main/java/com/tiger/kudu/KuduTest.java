package com.tiger.kudu;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title KuduTest
 * @date 2021/8/12 18:03
 * @description
 */
@Slf4j
public class KuduTest {

    /**
     * 声明全局变量 KuduClient 后期通过它来操作 kudu 表
     */
    private KuduClient kuduClient;
    private String tableName = "tbl";
    private KuduSession session;

    @Before
    public void init() {
        // 初始化操作
        String kuduMaster = "cdh01.wld.com:7051";
        KuduClient.KuduClientBuilder kuduClientBuilder = new KuduClient.KuduClientBuilder(kuduMaster);
        kuduClientBuilder.defaultOperationTimeoutMs(1800000);
        kuduClientBuilder.defaultAdminOperationTimeoutMs(1800000);

        kuduClient = kuduClientBuilder.build();
        // 创建写session,kudu必须通过session写入
        session = kuduClient.newSession();

        log.info("服务器地址#{}:客户端#{} 初始化成功...", kuduMaster, kuduClient);
    }

    @After
    @SneakyThrows
    public void close() {
        session.close();
        kuduClient.close();
    }

    @SneakyThrows
    @Test
    public void test1() {
        // 查看所有的表
        List<String> tablesList = kuduClient.getTablesList().getTablesList();
        System.out.println(tablesList);
    }

    @Test
    @SneakyThrows
    public void createTable() {
        if (!kuduClient.tableExists(tableName)) {
            /**
             * 指定表的schema
             */
            ArrayList<ColumnSchema> schemaColumns = new ArrayList<>();
            // 添加id列, 并指定主键
            schemaColumns.add(new ColumnSchema.ColumnSchemaBuilder("id", Type.INT32).key(true).build());
            schemaColumns.add(new ColumnSchema.ColumnSchemaBuilder("name", Type.STRING).build());
            schemaColumns.add(new ColumnSchema.ColumnSchemaBuilder("age", Type.INT32).build());
            schemaColumns.add(new ColumnSchema.ColumnSchemaBuilder("sex", Type.INT32).build());
            Schema schema = new Schema(schemaColumns);

            /**
             * 指定表的其他属性
             */
            CreateTableOptions tableOptions = new CreateTableOptions();

            // 指定表的分区方式和分区列
            ArrayList<String> partitionList = new ArrayList<>();
            partitionList.add("id");
            tableOptions.addHashPartitions(partitionList, 6);

            // 指定tablet的个数, 默认为3个
            tableOptions.setNumReplicas(3);

            // 创建表
            kuduClient.createTable(tableName, schema, tableOptions);

        }
    }

    @Test
    @SneakyThrows
    public void insert() {
        // 打开表

        KuduTable kuduTable = kuduClient.openTable("impala::kudu_analysis.sophia_test1014");
        List<ColumnSchema> columns = kuduTable.getSchema().getColumns();

        // 采取Flush方式 手动刷新
        session.setFlushMode(SessionConfiguration.FlushMode.MANUAL_FLUSH);
        session.setMutationBufferSpace(3000);
        for (int j = 0; j < 1; j++) {
            Insert insert = kuduTable.newInsert();
            for (ColumnSchema column : columns) {
                Type type = column.getType();
                if (type == Type.INT64) {
                    insert.getRow().addLong(column.getName(), 1L);
                } else if (type == Type.INT32) {
                    insert.getRow().addInt(column.getName(), 1);
                } else if (type == Type.STRING) {
                    insert.getRow().addString(column.getName(), "hello world");
                } else if (type == Type.UNIXTIME_MICROS) {
                    insert.getRow().addTimestamp(column.getName(), new Timestamp(System.currentTimeMillis()));
                }
            }
            session.apply(insert); // 插入
        }
        session.flush(); // 刷写缓存
    }

    /**
     * 查询表的数据结果
     */
    @Test
    public void queryData() throws KuduException {
        // 构建一个查询的扫描器
        KuduScanner.KuduScannerBuilder kuduScannerBuilder =
            kuduClient.newScannerBuilder(kuduClient.openTable(tableName));
        ArrayList<String> columnsList = new ArrayList<String>();
        columnsList.add("id");
        columnsList.add("name");
        columnsList.add("age");
        columnsList.add("sex");
        kuduScannerBuilder.setProjectedColumnNames(columnsList);
        // 返回结果集
        KuduScanner kuduScanner = kuduScannerBuilder.build();
        // 遍历
        while (kuduScanner.hasMoreRows()) {
            RowResultIterator rowResults = kuduScanner.nextRows();
            while (rowResults.hasNext()) {
                RowResult row = rowResults.next();
                int id = row.getInt("id");
                String name = row.getString("name");
                int age = row.getInt("age");
                int sex = row.getInt("sex");
                System.out.println(">>>>>>>>>>  id=" + id + " name=" + name + " age=" + age + "sex = " + sex);
            }
        }
    }

    @Test
    public void updateData() throws KuduException {
        // 向表加载数据需要一个 kuduSession对象
        KuduSession kuduSession = kuduClient.newSession();
        // 设置提交数据为自动fLush
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);
        // 打开本次操作的表名
        KuduTable kuduTable = kuduClient.openTable(tableName);

        // 构建了一个update对亲 用于数据的修改
        Update update = kuduTable.newUpdate();
        PartialRow row = update.getRow();
        row.addInt("id", 1);
        row.addString("name", "itcast");
        row.addInt("age", 50);
        row.addInt("sex", 1);

        // 最后实现执行数据的修改颜作换作
        kuduSession.apply(update);
    }

    @Test
    public void upsertData() throws KuduException {
        // 向表加载数据需要一个 kuduSession对象
        KuduSession kuduSession = kuduClient.newSession();
        // 设置提交数据为自动fLush
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);
        // 打开本次操作的表名
        KuduTable kuduTable = kuduClient.openTable(tableName);

        // 构建了一个update对亲 用于数据的修改
        Upsert upsert = kuduTable.newUpsert();
        PartialRow row = upsert.getRow();
        row.addInt("id", 1);
        row.addString("name", "itcast");
        row.addInt("age", 50);
        row.addInt("sex", 1);

        // 最后实现执行数据的修改颜作换作
        kuduSession.apply(upsert);
    }

    @Test
    public void deleteData() throws KuduException {
        // 向表加载数据需要一个 kuduSession对象
        KuduSession kuduSession = kuduClient.newSession();
        // 设置提交数据为自动fLush
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);
        // 打开本次操作的表名
        KuduTable kuduTable = kuduClient.openTable(tableName);

        // 构建了一个update对亲 用于数据的修改
        Delete delete = kuduTable.newDelete();
        PartialRow row = delete.getRow();
        row.addInt("id", 1);
        row.addString("name", "itcast");
        row.addInt("age", 50);
        row.addInt("sex", 1);

        // 最后实现执行数据的修改颜作换作
        kuduSession.apply(delete);
    }

    /**
     * 指定表的分区方式
     * 1. hash分区
     * 2. 范围分区
     * 3. 同时使用hash分区和范围分区
     */
    @Test
    @SneakyThrows
    public void partition() {
        if (!kuduClient.tableExists(tableName)) {
            /**
             * 指定表的schema
             */
            ArrayList<ColumnSchema> schemaColumns = new ArrayList<>();
            // 添加id列, 并指定主键
            schemaColumns.add(new ColumnSchema.ColumnSchemaBuilder("id", Type.INT32).key(true).build());
            schemaColumns.add(new ColumnSchema.ColumnSchemaBuilder("name", Type.STRING).build());
            schemaColumns.add(new ColumnSchema.ColumnSchemaBuilder("age", Type.INT32).build());
            schemaColumns.add(new ColumnSchema.ColumnSchemaBuilder("sex", Type.INT32).build());
            Schema schema = new Schema(schemaColumns);

            CreateTableOptions tableOptions = new CreateTableOptions();

            // hash分区, id作为分区字段, 一共分为6个桶
            // ArrayList<String> partitionList = new ArrayList<>();
            // partitionList.add("id");
            // tableOptions.addHashPartitions(partitionList, 6);

            // 范围分区
            ArrayList<String> partitionList = new ArrayList<>();
            partitionList.add("id");
            // 设置分区字段
            tableOptions.setRangePartitionColumns(partitionList);
            // 设置分区范围 [0, 10) [10, 20) [20, 30) [30, 40) [40, 50)
            int count = 0;
            for (int i = 0; i < 5; i++) {
                //指定range的上界
                PartialRow lower = schema.newPartialRow();
                lower.addInt("id", count);
                count += 10;
                // 指定range的下界
                PartialRow upper = schema.newPartialRow();
                upper.addInt("id", count);
                // 添加一共分区范围
                tableOptions.addRangePartition(lower, upper);
            }

            // 创建表
            kuduClient.createTable(tableName, schema, tableOptions);
        }
    }
}
