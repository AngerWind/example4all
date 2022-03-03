package com.tiger.hadoop.mapreduce._3_partitioner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 这里的两个泛型表示Map的输出KV的类型
 * 因为分区是在Map之后的
 */
public class PhonePartitioner extends Partitioner<Text, LongWritable> {

    /**
     * 对于每一条Map输出的KV进行分区, 分区数从0开始
     * @param text  类型是Map输出的K的类型
     * @param longWritable 类型是Map输入的V的类型
     * @param numPartitions Job中设置的总分区数, 返回的KV的分区需要小于分区数
     * @return
     */
    @Override
    public int getPartition(Text text, LongWritable longWritable, int numPartitions) {

        String phoneHead = text.toString().substring(0, 3);
        if ("136".equals(phoneHead)){
            return 0;
        } else if("137".equals(phoneHead)){
            return 1;
        } else{
            return 2;
        }
        // 如果job中指定的分区数为2, 那么将会报错, 因为有的数据没有对应的分区
        // 如果job中指定的分区数为4, 那么会浪费资源, 因为有的Reduce读取不到数据
    }
}
