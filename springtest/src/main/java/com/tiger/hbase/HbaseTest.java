package com.tiger.hbase;

import lombok.SneakyThrows;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Shen
 * @version v1.0
 * @Title Test
 * @date 2022/4/13 23:42
 * @description
 */
public class HbaseTest {

    /**
     * 通过connection获取table进行dml操作
     * 通过connection获取HBaseAdmin进行ddl操作
     * connection是线程安全的
     * table和hbaseAdmin是非线程安全的
     */
    Connection connection;
    HBaseAdmin hbaseAdmin;

    @SneakyThrows
    @Before
    public void init() {
        Configuration configuration = HBaseConfiguration.create();
        connection = ConnectionFactory.createConnection(configuration);
        hbaseAdmin = (HBaseAdmin)connection.getAdmin();
    }

    @SneakyThrows
    @After
    public void close() {
        hbaseAdmin.close();
        connection.close();
    }

    @SneakyThrows
    @Test
    public void createNamespace() {
        NamespaceDescriptor ns = NamespaceDescriptor.create("ns1").addConfiguration("", "").build();
        hbaseAdmin.createNamespace(ns);
    }

    @SneakyThrows
    @Test
    public void createTable() {
        TableName tableName = TableName.valueOf("ns1", "table");
        if(hbaseAdmin.tableExists(tableName)) {
            hbaseAdmin.disableTable(tableName);
            hbaseAdmin.deleteTable(tableName);
        }
        ColumnFamilyDescriptor cf1 = ColumnFamilyDescriptorBuilder.newBuilder("cf1".getBytes()).build();
        ColumnFamilyDescriptor cf2 = ColumnFamilyDescriptorBuilder.newBuilder("cf2".getBytes()).build();
        TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(tableName)
                .setColumnFamily(cf1)
                .setColumnFamily(cf2)
                .build();

        hbaseAdmin.createTable(tableDescriptor);
    }

    @SneakyThrows
    @Test
    public void put() {
        Table table = connection.getTable(TableName.valueOf("ns1", "table"));
        Put put = new Put("rowKey1".getBytes())
                .addColumn("cf1".getBytes(), "name".getBytes(), "zhangs".getBytes())
                .addColumn("cf1".getBytes(), "age".getBytes(), "zhangs".getBytes())
                .addColumn("cf2".getBytes(), "high".getBytes(), "175".getBytes())
                .addColumn("cf2".getBytes(), "weight".getBytes(), "120".getBytes());
        table.put(put);
    }

    @SneakyThrows
    @Test
    public void get() {
        Table table = connection.getTable(TableName.valueOf("ns1", "table"));

        Get get = new Get("rowKey1".getBytes()) // 如果什么都不设置, 就读取整行数据
                .addColumn("cf1".getBytes(), "name".getBytes()) // 指定读取的列
                .addFamily("cf2".getBytes()) // 指定读取的列族
                .readAllVersions(); // 指定读取的版本

        Result result = table.get(get);
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            // 一下都是字节数组
            byte[] familyArray = cell.getFamilyArray();
            byte[] qualifierArray = cell.getQualifierArray();
            long timestamp = cell.getTimestamp();
            byte[] valueArray = cell.getValueArray();
            Cell.Type type = cell.getType();
        }

    }

    @SneakyThrows
    @Test
    public void delete() {
        Table table = connection.getTable(TableName.valueOf("ns1", "table"));
        //
        Delete delete = new Delete("rowKey1".getBytes());


        // table.get(get);
    }
}
