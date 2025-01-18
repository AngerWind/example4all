package com.tiger.mapreduce._6_inputformat_outputforamt;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 四个泛型 KEYIN,VALUEIN,KEYOUT,VALUEOUT
 * KEYIN, VALUEIN表示输入字段, 需要与mapper的输出字段相同
 * KEYOUT, VALUEOUT表示输出字段
 */
public class DoNothingReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    // 任务启动时调用一次
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {}

    // values表示key相同的一组值的集合
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        for (IntWritable v : values) {
            count++; // 统计key出现的次数
        }
        context.write(key, new IntWritable(count));
    }

    // 任务结束时finally调用一次
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {}
}
