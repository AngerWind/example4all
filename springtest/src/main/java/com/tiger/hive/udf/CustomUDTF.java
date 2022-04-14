package com.tiger.hive.udf;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title CustomUDAF
 * @date 2022/3/24 18:21
 * @description
 */
public class CustomUDTF extends GenericUDTF {


    @Override
    public void process(Object[] args) throws HiveException {

    }

    @Override
    public void close() throws HiveException {

    }
}
