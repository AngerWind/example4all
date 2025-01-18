package com.tiger;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.apache.hadoop.hbase.MemoryCompactionPolicy;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.AsyncConnection;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/1
 * @description
 */
public class _02_DDL {

    private final String namespace = "namespace_1";
    private final String table = "table_1";
    /**
     * 对hbase的操作是通过Connection中的Admin和Table属性来操作hbase的
     *
     * Connection是线程安全的, 并且是重量级的 Admin和Table是是轻量级的, 但是是线程不安全的
     *
     * 所以每个应用只创建一个连接, 然后通过Connection来创建Admin和Table 每个线程都使用单独创建的Admin和Table
     */

    // 同步操作hbase的连接
    Connection connection;
    // 异步操作hbase的连接
    AsyncConnection asyncConnection;

    @BeforeEach
    public void before() throws IOException, ExecutionException, InterruptedException {
        // 直接读取hbase-site.xml中的配置
        connection = ConnectionFactory.createConnection();
        CompletableFuture<AsyncConnection> asyncConnectionFutere = ConnectionFactory.createAsyncConnection();
        asyncConnection = asyncConnectionFutere.get();
    }

    @AfterEach
    public void after() throws IOException {
        connection.close();
        asyncConnection.close();
    }

    @Test
    public void createNamespace() throws IOException {
        // admin是轻量的, 线程不安全的, 每个线程都可以创建admin
        Admin admin = connection.getAdmin();
        NamespaceDescriptor descriptor = NamespaceDescriptor.create(namespace).addConfiguration("key1", "value1") // 给命名空间设置别的属性
            .addConfiguration("key2", "value2").build();
        admin.createNamespace(descriptor);
        // 关闭admin
        admin.close();
    }

    @Test
    public void deleteNamespace() throws IOException {
        // admin是轻量的, 线程不安全的, 每个线程都可以创建admin
        Admin admin = connection.getAdmin();
        // 只有空的namespace才可以删除
        admin.deleteNamespace(namespace);
        // 关闭admin
        admin.close();
    }

    @Test
    public void tableExists() throws IOException {
        Admin admin = connection.getAdmin();
        boolean exists = admin.tableExists(TableName.valueOf(namespace, table));
        System.out.println(exists);

        admin.close();
    }

    @Test
    public void createTable() throws IOException {
        Admin admin = connection.getAdmin();

        List<ColumnFamilyDescriptor> list = Lists.newArrayList();
        for (int i = 0; i < 2; i++) {
            // 创建列族
            ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder
                .newBuilder(Bytes.toBytes("columnFamily" + i))
                .setCompressionType(Algorithm.GZ) // 设置压缩算法
                .setBlocksize(1024 * 1024) // 设置块大小
                .setBloomFilterType(BloomType.ROWCOL) // 设置布隆过滤器
                .setMaxVersions(5) // 设置每个列族中每个rowkey能够保存的最大版本
                .setMinVersions(3) // 设置每个列族中每个rowkey能够保存的最小版本
                .setInMemoryCompaction(MemoryCompactionPolicy.ADAPTIVE) // 设置in memory compaction的策略
                .setTimeToLive(1000) // 数据在该列族中的存活时间为 1000 秒。超过这个时间的数据将被自动删除
                .build();

            list.add(columnFamilyDescriptor);
        }

        admin.createTable(TableDescriptorBuilder.newBuilder(TableName.valueOf(table)).setColumnFamilies(list) // 设置列族
            .setMaxFileSize(1024 * 1024 * 1024) // 设置一个region的最大大小, 达到了这个大小就要进行分裂
            .setMemStoreFlushSize(1024 * 1024 * 1024) // 设置memstore的大小
            // 设置region的自动拆分策略, 在实际运用过程中不需要指定, 用默认的就好了
            .setRegionSplitPolicyClassName("org.apache.hadoop.hbase.regionserver.ConstantSizeRegionSplitPolicy")
            .setRegionReplication(1) // 设置副本数量
            .build());

        admin.close();
    }

    @Test
    public void alterTable() throws IOException {
        Admin admin = connection.getAdmin();

        // 判断表格是否存在
        if (!admin.tableExists(TableName.valueOf(namespace, table))) {
            System.out.println("表格不存在无法修改");
            return;
        }
        try {
            // 获取之前的表格描述
            TableDescriptor descriptor = admin.getDescriptor(TableName.valueOf(namespace, table));
            // 根据旧的descripter创建一个新的table descriptor
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(descriptor);
            // 获取对应的column family descriptor
            ColumnFamilyDescriptor columnFamily1 = descriptor.getColumnFamily(Bytes.toBytes("columnFamily1"));
            // 使用旧的创建新的
            ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder =
                ColumnFamilyDescriptorBuilder.newBuilder(columnFamily1);

            // 修改对应的版本
            columnFamilyDescriptorBuilder.setMaxVersions(5);

            // 此处修改的时候 如果填写的新创建 那么别的参数会初始化
            tableDescriptorBuilder.modifyColumnFamily(columnFamilyDescriptorBuilder.build());
            admin.modifyTable(tableDescriptorBuilder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 关闭 admin
        admin.close();
    }

    /**
     * 删除表格
     */
    @Test
    public boolean disableTable() throws IOException {
        // 获取 admin
        Admin admin = connection.getAdmin();
        try {
            TableName tableName1 = TableName.valueOf(namespace, table);
            // table必须处于启用状态, 才可以禁用
            if (admin.isTableEnabled(tableName1)) {
                admin.disableTable(tableName1);
            }
            admin.deleteTable(tableName1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 4. 关闭 admin
        admin.close();
        return true;
    }
}
