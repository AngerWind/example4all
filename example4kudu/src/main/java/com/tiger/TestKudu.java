package com.tiger;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tiger.Shen
 * @date 2020/8/30 15:28
 */
public class TestKudu {

    private KuduClient kuduClient;

    private String tableName = "student";

    /**
     * 初始化kuduClient
     */
    @Before
    public void init() {

        //指定 kuduMaster 地址
        String kuduMaster = "node1:7051,node2:7051,node3:7051";

        // 创建kuduClientBuider
        KuduClient.KuduClientBuilder kuduClientBuilder = new
                KuduClient.KuduClientBuilder(kuduMaster);

        // 指定客户端和kudu集群连接的socket超时时间
        kuduClientBuilder.defaultSocketReadTimeoutMs(10000);

        // 创建client
        kuduClient = kuduClientBuilder.build();
    }

    /**
     * 关闭kudu连接
     */
    @After
    public void close() throws KuduException {
        if (kuduClient != null) {
            kuduClient.close();
        }
    }

    @Test
    public void createTable() throws KuduException {

        // 判断表是否存在
        if (!kuduClient.tableExists(tableName)) {
            List<ColumnSchema> columnSchemas = new ArrayList<ColumnSchema>();
            // 添加字段信息，名字为id，类型为int32，主键
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("id", Type.INT32)
                    .key(true).build());
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("name", Type.STRING)
                    .build());
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("age", Type.INT32)
                    .build());
            columnSchemas.add(new ColumnSchema.ColumnSchemaBuilder("sex", Type.INT32)
                    .build());
            Schema schema = new Schema(columnSchemas);

            CreateTableOptions options = new CreateTableOptions();
            // 指定表的分区字段和分区数， 分区号 = id.hashcode % 分区数
            options.addHashPartitions(Arrays.asList("id"), 6);
            // 指定副本数，默认为3
            options.setNumReplicas(3);

            // 创建表
            kuduClient.createTable(tableName, schema, options);
        }
    }

    @Test
    public void insertTable() throws KuduException {
        // 向表加载数据需要一个 kuduSession 对象
        KuduSession kuduSession = kuduClient.newSession();
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);

        // 打开表并获取一个insert对象
        KuduTable kuduTable = kuduClient.openTable(tableName);
        Insert insert = kuduTable.newInsert();

        // 实现执行数据的加载操作
        PartialRow row = insert.getRow();
        row.addInt("id", 1);
        row.addString("name", "zhangsan-");
        row.addInt("age", 20);
        row.addInt("sex", 1);
        kuduSession.apply(insert);

    }

    @Test
    public void query() throws KuduException {
        // 构建一个查询的扫描器
        KuduScanner.KuduScannerBuilder kuduScannerBuilder =
                kuduClient.newScannerBuilder(kuduClient.openTable(tableName));

        // 设置扫描的列
        kuduScannerBuilder.setProjectedColumnNames(Arrays.asList("id", "name", "age", "sex"));
        KuduScanner kuduScanner = kuduScannerBuilder.build();

        //遍历
        while (kuduScanner.hasMoreRows()){
            RowResultIterator rowResults = kuduScanner.nextRows();

            while (rowResults.hasNext()){
                RowResult row = rowResults.next();

                int id = row.getInt("id");
                String name = row.getString("name");
                int age = row.getInt("age");
                int sex = row.getInt("sex");

                System.out.format("id=%d, name=%s, age=%d, sex=%d", id, name, age, sex);
            }
        }
    }

    @Test
    public void updateData() throws KuduException {
        //修改表的数据需要一个 kuduSession 对象
        KuduSession kuduSession = kuduClient.newSession();
        kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);

        
        // 获取打开表的对象，并获取一个upsert对象
        KuduTable kuduTable = kuduClient.openTable(tableName);
        Upsert upsert = kuduTable.newUpsert();

        PartialRow row = upsert.getRow();
        //如果 id 存在就表示修改，不存在就新增
        row.addInt("id",100);
        row.addString("name","zhangsan-100");
        row.addInt("age",100);
        row.addInt("sex",0);
        //最后实现执行数据的修改操作
        kuduSession.apply(upsert);
    }
}
