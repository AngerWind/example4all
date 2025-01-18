package com.tiger.tableapi.udf.table_function;

import java.math.BigDecimal;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/8/16
 * @description
 */

/**
 * 
 *
 * TableFunction需要一个泛型, 用来表示返回的一行数据的类型
 * 如果你返回的一行数据有多个字段, 那么需要使用Row来表示
 * 如果你返回的一行数据只有一个字段, 那么可以使用Row, 或者直接这个字段的类型来表示
 * 如果你返回的一行数据, 根据入参类型的不同而不同, 那么泛型可以直接是Object
 */
public class TableFunctionExample {

    /**
     * 将字符串按照逗号进行分割, 然后炸裂
     * 这里返回的每一行都有两个字段, 所以要使用Row来表示
     */
    public static class MyPosExplode extends TableFunction<Row> {
        @FunctionHint(input = {@DataTypeHint("STRING")},
                output = @DataTypeHint("ROW<idx INT, s STRING>"))
        public void eval(String args) {
            if (args == null) {
                collect(null);
            } else {
                String[] split = args.split(",");
                for (int i = 0; i < split.length; i++) {
                    collect(Row.of(i, split[i]));
                }
            }
        }
    }

    /**
     * 因为FlattenFunction返回的一行数据只有一个字段, 所以可以直接返回Integer
     * 其实隐式的返回了Row<Integer>
     */
    public static class FlattenFunction extends TableFunction<Integer> {
        public void eval(Integer... args) {
            for (Integer i : args) {
                collect(i);
            }
        }
    }


    /**
     * 因为根据参数的不同, 返回的类型也不同, 所以TableFunction的泛型直接给Object
     */
    public static class DuplicatorFunction extends TableFunction<Object> {
        /**
         * 传入的Int, 返回的一行数据只有一个字段, 就是int
         */
        @FunctionHint(output = @DataTypeHint("INT"))
        public void eval(Integer i) {
            collect(i);
            collect(i);
        }
        /**
         * 传入的DECIMAL, 返回的一行数据只有一个字段, 就是DECIMAL
         */
        @FunctionHint(output = @DataTypeHint("DECIMAL(10, 4)"))
        public void eval(@DataTypeHint("DECIMAL(10, 4)") BigDecimal d) {
            collect(d);
            collect(d);
        }
    }

}
