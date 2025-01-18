package com.tiger.tableapi.udf;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/16
 * @description
 */
public class RegistFunctionTest {

    /**
     * 注册udf, 有两种方式
     * 1.  createTemporarySystemFunction注册全局函数, 函数在全局可用
     * 2.  createTemporaryFunction注册udf到指定的catalog和database下, 在调用的时候需要使用全路径名
     *         tableEnv.createTemporaryFunction("my_catalog.my_database.MyUDF", MyUDF.class);
     *
     *         在调用的时候, 需要指定全路径名
     *         SELECT my_catalog.my_database.MyUDF(column_name)
     *
     *         或者切换到对应的database下
     *         USE CATALOG my_catalog;
     *         USE my_database;
     *         SELECT MyUDF(column_name) AS processed_columnFROM my_table;
     */
}
