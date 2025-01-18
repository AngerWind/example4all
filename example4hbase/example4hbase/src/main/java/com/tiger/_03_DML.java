package com.tiger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.apache.commons.compress.utils.Lists;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.AsyncConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.ColumnValueFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
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
public class _03_DML {

    private final String namespaceName = "namespace_1";
    private final String tableName = "table_1";
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

    /**
     * 插入数据
     */
    @Test
    public void putCell() throws IOException {
        String rowKey = "rowKey1";
        String columnFamily = "columnFamily1";

        // 1. 获取 table
        Table table = connection.getTable(TableName.valueOf(namespaceName, tableName));

        // 2. 创建put对象, 必须指定rowKey
        Put put = new Put(Bytes.toBytes(rowKey));

        // 可选的, 设置tiemstamp, 即版本号
        put.setTimestamp(System.currentTimeMillis());

        // 设置这条数据的ttl, 过了ttl那么数据会被过期
        put.setTTL(1000 * 60 * 60 * 24 * 7) ;


        // 3. 添加列信息
        //    !!! 可以多次调用这个方法, 依次添加多个列的数据
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("column_1"), Bytes.toBytes("value_1"));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("column_2"), Bytes.toBytes("value_2"));

        // 4. put进去
        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭 table
        table.close();
    }

    @Test
    public void batchPutCells() throws IOException {
        String rowKey = "rowKey1";
        String columnFamily = "columnFamily1";

        // 1. 获取 table
        Table table = connection.getTable(TableName.valueOf(namespaceName, tableName));

        ArrayList<Put> list = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            // 2. 创建put对象, 必须指定rowKey
            Put put = new Put(Bytes.toBytes(rowKey + i));

            // 3. 添加列信息
            //    !!! 可以多次调用这个方法, 依次添加多个列的数据
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("column_1"), Bytes.toBytes("value_1"));
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("column_2"), Bytes.toBytes("value_2"));

            list.add(put);
        }
        // 4. put进去
        try {
            // table.batch(list, new Object[list.size()]);

            // 底层调用的就是batch方法
            table.put(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭 table
        table.close();
    }

    @Test
    public void getCells() throws IOException {
        String rowKey = "rowKey1";
        String columnFamily = "columnFamily1";
        String columnName = "column_1";
        // 获取 table
        Table table = connection.getTable(TableName.valueOf(namespaceName, tableName));
        // 创建 get 对象
        Get get = new Get(Bytes.toBytes(rowKey));
        // 如果直接调用 get 方法读取数据 此时读一整行数据
        // 如果想读取某一列的数据 需要添加对应的参数, 读取多个列可以多次调用这个方法
        get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));

        // 默认情况下, 如果什么也不设置会读取最新的一个版本

        // 设置读取这个rowkey的所有版本
        get.readAllVersions();

        // 设置只读取最新的3个版本
        // get.readVersions(3);

        // 也可以通过timestamp来指定读取特定的版本
        // get.setTimestamp(System.currentTimeMillis());

        // 设置扫描版本的范围, 可以和readVersions方法一起使用
        // 表示读取一定范围的版本, 并且只读取3个最新的版本
        get.setTimeRange(0, System.currentTimeMillis());

        try {
            // 读取数据 得到 result 对象
            Result result = table.get(get);
            // 处理数据
            Cell[] cells = result.rawCells();
            // 测试方法: 直接把读取的数据打印到控制台
            // 如果是实际开发 需要再额外写方法 对应处理数据
            for (Cell cell : cells) {
                // cell 存储数据比较底层
                String value = new String(CellUtil.cloneValue(cell));
                System.out.println(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭 table
        table.close();
    }

    public void batchGetCells() throws IOException {
        String rowKey = "rowKey1";
        String columnFamily = "columnFamily1";
        String columnName = "column_1";
        // 获取 table
        Table table = connection.getTable(TableName.valueOf(namespaceName, tableName));

        ArrayList<Get> list = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            // 创建 get 对象
            Get get = new Get(Bytes.toBytes(rowKey + i));
            // 如果直接调用 get 方法读取数据 此时读一整行数据
            // 如果想读取某一列的数据 需要添加对应的参数, 读取多个列可以多次调用这个方法
            get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));

            // 默认情况下, 如果什么也不设置会读取最新的一个版本

            // 设置读取这个rowkey的所有版本
            get.readAllVersions();
            // 设置只读取最新的3个版本
            // get.readVersions(3);
            // 也可以通过timestamp来指定读取特定的版本
            // get.setTimestamp(System.currentTimeMillis());
            // 设置只读取这一段范围的版本
            get.setTimeRange(0, System.currentTimeMillis());

            list.add(get);
        }

        try {
            // 读取数据 得到 result 对象
            Result[] result = table.get(list);
            for (Result r : result) {
                // 处理数据
                Cell[] cells = r.rawCells();
                // 测试方法: 直接把读取的数据打印到控制台
                // 如果是实际开发 需要再额外写方法 对应处理数据
                for (Cell cell : cells) {
                    // cell 存储数据比较底层
                    String value = new String(CellUtil.cloneValue(cell));
                    System.out.println(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭 table
        table.close();
    }

    /**
     * 扫描数据
     *
     * @param startRow 开始的 row 包含的
     * @param stopRow 结束的 row 不包含
     */
    public void scanRows(String startRow, String stopRow) throws IOException {
        Table table = connection.getTable(TableName.valueOf(namespaceName, tableName));
        // 2. 创建 scan 对象
        Scan scan = new Scan();
        // 设置扫描的起始和节数, 设置为左闭右闭, 默认情况下就是左闭右开
        // 如果不设置的话就是全表扫描
        scan.withStartRow(Bytes.toBytes(startRow), true);
        scan.withStopRow(Bytes.toBytes(stopRow), true);

        // 设置扫描结果的最大大小，以字节为单位。这限制了扫描操作返回的总数据量。
        // 这个值通常用来防止客户端因接收过多数据而耗尽内存。
        scan.setMaxResultSize(100);
        // 设置扫描结果的最大行数。此参数限制了扫描操作返回的最大行数，防止一次扫描返回太多行。
        scan.setLimit(200);
        // 设置每次 RPC 调用中返回的最大单元格数（Cell）
        // 这可以提高大批量扫描的性能，因为它减少了每次 RPC 调用中传输的数据量。
        scan.setBatch(50);
        // 启用异步预取。这样可以在扫描时异步预取数据，提高扫描的性能。
        scan.setAsyncPrefetch(true);
        // 设置按需加载列族。启用此选项时，只有在扫描时访问的列族会被加载，这可以减少不必要的数据加载，提升性能。
        scan.setLoadColumnFamiliesOnDemand(true);
        // 允许返回部分结果。启用此选项时，如果一个 RPC 调用无法返回所有数据，
        // 它会返回部分结果，这对于大列值或者大行数据的扫描很有用。
        scan.setAllowPartialResults(true);


        // 设置扫描操作的时间戳。这会使扫描只返回与指定时间戳匹配的单元格版本。
        scan.setTimestamp(System.currentTimeMillis());
        // 设置扫描操作的时间范围。此方法将扫描的时间范围设置为从时间戳 0 到当前时间戳的范围内的所有数据。
        scan.setTimeRange(0, System.currentTimeMillis());
        // 设置扫描操作返回的最大版本数。例如，这里设置为 3 表示每个rowkey最多返回 3 个版本的数据。
        scan.readVersions(3);

        try {
            // 读取多行数据 获得 scanner
            ResultScanner scanner = table.getScanner(scan);
            // ResultScanner 来记录多行数据 result 的数组
            // result 来记录一行数据 cell 数组
            // 每一个cell就是一行的一列数据
            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.print(
                        new String(CellUtil.cloneRow(cell)) + "-" + new String(CellUtil.cloneFamily(cell)) + "-"
                            + new String(CellUtil.cloneQualifier(cell)) + "-" + new String(CellUtil.cloneValue(cell))
                            + "\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 关闭 table
        table.close();
    }

    /**
     * 带过滤的扫描
     */
    public void filterScan(String startRow, String stopRow, String columnFamily, String columnName, String value)
        throws IOException {
        // 获取 table
        Table table = connection.getTable(TableName.valueOf(namespaceName, tableName));
        // 创建 scan 对象
        Scan scan = new Scan();

        // 设置扫描的起始和节数, 设置为左闭右开, 也可以设置为左闭右闭
        // 如果不设置的话就是全表扫描
        scan.withStartRow(Bytes.toBytes(startRow), true);
        scan.withStopRow(Bytes.toBytes(stopRow), false);

        // 创建过滤器
        // 结果只保留当前列的数据, 比如比较name = '张三', 那么过滤完之后就只保留rowkey和name这一列的数据
        ColumnValueFilter columnValueFilter = new ColumnValueFilter(
            // 列族名称
            Bytes.toBytes(columnFamily),
            // 列名
            Bytes.toBytes(columnName),
            // 比较关系, 可以<, >, >=, <=, !=, ==
            CompareOperator.EQUAL,
            // 值
            Bytes.toBytes(value));

        // 结果保留整行数据, 比如比较name = '张三', 那么过滤完之后会保留当前行的所有数据
        // 同时如果一行没有name这个字段, 那么也会保留
        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(
            // 列族名称
            Bytes.toBytes(columnFamily),
            // 列名
            Bytes.toBytes(columnName),
            // 比较关系
            CompareOperator.EQUAL,
            // 值
            Bytes.toBytes(value));

        // 创建一个过滤器列表
        FilterList filterList = new FilterList(Operator.MUST_PASS_ALL, singleColumnValueFilter);

        scan.setFilter(filterList);
        try {
            // 读取多行数据 获得 scanner
            ResultScanner scanner = table.getScanner(scan);
            // ResultScanner 来记录多行数据 result 的数组
            // result 来记录一行数据 cell 数组
            // 每一个cell就是一行的一列数据
            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.print(
                        new String(CellUtil.cloneRow(cell)) + "-" + new String(CellUtil.cloneFamily(cell)) + "-"
                            + new String(CellUtil.cloneQualifier(cell)) + "-" + new String(CellUtil.cloneValue(cell))
                            + "\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 关闭 table
        table.close();
    }

    @Test
    public void deleteCell() throws IOException {

        String rowKey = "rowKey1";
        String columnFamily = "columnFamily1";
        String columnName = "column_1";
        // 1.获取 table
        Table table = connection.getTable(TableName.valueOf(namespaceName, tableName));
        // 2.创建 Delete 对象
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        // 删除一行的指定字段的最新版本数据, 重量级方法
        delete.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));

        // 删除一行对应字段的所有版本数据
        delete.addColumns(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));

        // 删除一行数据指定的列族
        // delete.addFamily(Bytes.toBytes(family));

        // 执行删除
        table.delete(delete);

        // 关闭资源
        table.close();
    }

    @Test
    public void batchDeleteCell() throws IOException {

        String rowKey = "rowKey1";
        String columnFamily = "columnFamily1";
        String columnName = "column_1";
        // 1.获取 table
        Table table = connection.getTable(TableName.valueOf(namespaceName, tableName));

        List<Delete> deleteList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            // 2.创建 Delete 对象
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            // 删除一行的指定字段的最新版本数据, 重量级方法
            delete.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));

            // 删除一行对应字段的所有版本数据
            delete.addColumns(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));

            // 删除一行数据指定的列族
            // delete.addFamily(Bytes.toBytes(family));

            deleteList.add(delete);
        }

        // 执行删除
        table.delete(deleteList);

        // 关闭资源
        table.close();
    }
}


