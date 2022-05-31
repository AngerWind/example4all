package com.tiger.kudu;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        //初始化操作
        String kuduMaster = "cdh01.wld.com:7051";
        KuduClient.KuduClientBuilder kuduClientBuilder = new
                KuduClient.KuduClientBuilder(kuduMaster);
        kuduClientBuilder.defaultOperationTimeoutMs(1800000);
        kuduClientBuilder.defaultAdminOperationTimeoutMs(1800000);

        kuduClient = kuduClientBuilder.build();
        // 创建写session,kudu必须通过session写入
        session = kuduClient.newSession();

        log.info("服务器地址#{}:客户端#{} 初始化成功...", kuduMaster, kuduClient);
    }

    @After
    @SneakyThrows
    public void close(){
        session.close();
        kuduClient.close();
    }

    @SneakyThrows
    @Test
    public void test1(){
        List<String> tablesList = kuduClient.getTablesList().getTablesList();
        System.out.println(tablesList);
    }

    @Test
    @SneakyThrows
    public void insert(){
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
            session.apply(insert);
        }
        session.flush();
    }

    /**
     * 查询表的数据结果
     */
    @Test
    public void queryData() throws KuduException {
        //构建一个查询的扫描器
        KuduScanner.KuduScannerBuilder kuduScannerBuilder =
                kuduClient.newScannerBuilder(kuduClient.openTable(tableName));
        ArrayList<String> columnsList = new ArrayList<String>();
        columnsList.add("id");
        columnsList.add("name");
        columnsList.add("age");
        columnsList.add("sex");
        kuduScannerBuilder.setProjectedColumnNames(columnsList);
        //返回结果集
        KuduScanner kuduScanner = kuduScannerBuilder.build();
        //遍历
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

    public  void test() {

        Logger aaa = LoggerFactory.getLogger("aaa");
        //初始化操作
        KuduClient kuduClient = null;
        try {
            String kuduMaster = "header01.big.db.coltd:7051";

            KuduClient.KuduClientBuilder kuduClientBuilder = new
                    KuduClient.KuduClientBuilder(kuduMaster);
            kuduClientBuilder.defaultOperationTimeoutMs(1800000);

            kuduClient = kuduClientBuilder.build();

            log.info("服务器地址#{}:客户端#{} 初始化成功...", kuduMaster, kuduClient);

            aaa.info("初始化成功");

            List<String> tablesList = null;
            tablesList = kuduClient.getTablesList().getTablesList();
            System.out.println("print table");
            aaa.info("print table");
            System.out.println(tablesList);
            aaa.info("{}", tablesList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (kuduClient != null) {
                try {
                    kuduClient.close();
                } catch (KuduException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
