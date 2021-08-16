package com.tiger.kudu;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduScanner;
import org.apache.kudu.client.RowResult;
import org.apache.kudu.client.RowResultIterator;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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


    @Before
    public void init() {
        //初始化操作
        String kuduMaster = "10.2.0.241:7051";
        KuduClient.KuduClientBuilder kuduClientBuilder = new
                KuduClient.KuduClientBuilder(kuduMaster);
        kuduClientBuilder.defaultOperationTimeoutMs(1800000);

        kuduClient = kuduClientBuilder.build();

        log.info("服务器地址#{}:客户端#{} 初始化成功...", kuduMaster, kuduClient);
    }

    @SneakyThrows
    @Test
    public void test1(){
        List<String> tablesList = kuduClient.getTablesList().getTablesList();
        System.out.println(tablesList);
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

    public static void main(String[] args) {

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
