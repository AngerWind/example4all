package com.tiger.hive.udf;

import org.apache.hadoop.hive.ql.exec.TaskExecutionException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 对explode函数的源码进行讲解
 */
public class GenericUDTFExplodeExample extends GenericUDTF {

    private transient ObjectInspector inputOI = null;
    private transient final Object[] forwardListObj = new Object[1];
    private transient final Object[] forwardMapObj = new Object[2];

    /**
     * 每次调用udtf函数都会调用这个函数
     * 这个函数的入参表示调用udtf函数传进来的入参的类型, 因为调用udtf函数可以传入多个入参, 所以是一个数组, 主要用于建议当前udtf函数调用是否合法
     * 这个函数返回值表示调用udtf函数返回的行的类型和列名, 类型是StructObjectInspector, 这个struct结构的顶级元素表示返回行的各个列。返回值必须使用工厂方法创建
     */
    @Override
    public StructObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
        // 对传入explode函数的参数个数进行校验
        if (args.length != 1) {
            throw new UDFArgumentException("explode() takes only one argument");
        }

        // 创建两个List用于explode函数返回出去的行的列的名称和类型
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();

        // 对传入explode函数的类型进行校验
        // 因为explode只能传入一个参数, 所以只需要去args[0]
        switch (args[0].getCategory()) {
            case LIST:
                // 保存explode入参的类型
                inputOI = args[0];
                // 添加一个返回的列, 名称为col, 这个列明可以随便取, 因为可以使用as覆盖
                fieldNames.add("col");
                // 添加一个返回的列, 类型为List类型中元素的类型
                fieldOIs.add(((ListObjectInspector)inputOI).getListElementObjectInspector());
                break;
            case MAP:
                inputOI = args[0];
                // 添加两个列的名称
                fieldNames.add("key");
                fieldNames.add("value");
                // 添加一个返回的列, 类型为Map类型中key的元素的类型
                fieldOIs.add(((MapObjectInspector)inputOI).getMapKeyObjectInspector());
                // 添加一个返回的列, 类型为map类型中value的元素的类型
                fieldOIs.add(((MapObjectInspector)inputOI).getMapValueObjectInspector());
                break;
            default:
                // 如果传入的类型既不是list也不是map, 报错.
                throw new UDFArgumentException("explode() takes an array or a map as a parameter");
        }

        // 这里的返回值StructObjectInspector表示explode这个函数返回出去的行的类型, 需要传入两个list
        // 第一个参数: List<String> 表示返回行的列的名称, 可以随便取
        // 第二个参数: List<ObjectInspector> 表示返回行的列的类型
        // 通过工厂方法创建返回值
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames,
                fieldOIs);
    }

    /**
     * 这个函数用于udtf的计算, 传入的函数是调用udtf时传入的入参的值, 是一个数组
     * 在process函数中必须调用forward函数将结果写出, 一次写出将产生一行, 可以多次调用
     * 调用forward时应该传入Object[]数组, 表示多个列的值
     *
     * 这个方法处理一行数据就会调用一次
     */
    @Override
    public void process(Object[] o) throws HiveException {
        // 通过getCategory判断传入的参数的类型
        switch (inputOI.getCategory()) {
            case LIST:
                // 强转为ListObjectInspector
                ListObjectInspector listOI = (ListObjectInspector)inputOI;
                // 将object类型转换为具体的类型最好使用Inspector中提供的方法
                List<?> list = listOI.getList(o[0]);
                if (list == null) {
                    return;
                }
                for (Object r : list) {
                    forwardListObj[0] = r;
                    forward(forwardListObj);
                }
                break;
            case MAP:
                // 强转为MapObjectInspector
                MapObjectInspector mapOI = (MapObjectInspector)inputOI;
                // 将object类型转换为具体的类型最好使用Inspector中提供的方法
                Map<?,?> map = mapOI.getMap(o[0]);
                if (map == null) {
                    return;
                }
                for (Map.Entry<?,?> r : map.entrySet()) {
                    forwardMapObj[0] = r.getKey();
                    forwardMapObj[1] = r.getValue();
                    forward(forwardMapObj);
                }
                break;
            default:
                throw new TaskExecutionException("explode() can only operate on an array or a map");
        }
    }

    @Override
    public String toString() {
        return "explode";
    }
    @Override
    public void close() throws HiveException {
    }
}
