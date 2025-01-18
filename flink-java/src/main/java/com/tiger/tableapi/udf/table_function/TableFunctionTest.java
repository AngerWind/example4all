package com.tiger.tableapi.udf.table_function;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/16
 * @description
 */

import com.tiger.tableapi.udf.table_function.TableFunctionExample.MyPosExplode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

import static org.apache.flink.table.api.Expressions.$;

/**
 * 表函数, 也叫UDTF
 * 即输入一行数据的一个或者多个字段, 然后输出一行或者多行数据, 类似hive中的炸裂函数
 *
 * 在炸裂之后, 再使用lateral table 将炸裂后的侧视图与原表进行连接
 */
public class TableFunctionTest {

    @Test
    public void test(){
        // 通过流式环境创建表环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(streamEnv);

        DataStream<String> stream = streamEnv.fromElements("12, 34, 56", "78, 90");

        tableEnv.createTemporaryView("event", stream, $("str"));

        tableEnv.createTemporarySystemFunction("my_posexplode", MyPosExplode.class);

        tableEnv.executeSql("select * from event").print();

        // 使用交叉连接 来将原表 和炸裂后的侧视图进行连接
        tableEnv.executeSql("SELECT * FROM event, " +
                "lateral table(my_posexplode(str)) as my_table_alias(index, val)").print();

        // 使用left join 来将原表 和炸裂后的侧视图进行连接, 效果和上面一样
        tableEnv.executeSql("select * from event " +
                "left join lateral table(my_posexplode(str)) as my_table_alias(index, val) on true").print();

    }
}
