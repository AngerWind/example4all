package com.tiger.hadoop.mapreduce._1_wordcount;

/**
 * @author Shen
 * @version v1.0
 * @Title WordCountMapper
 * @date 2022/2/24 23:28
 * @description
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 导入Mapper类使用org.apache.hadoop.mapreduce.Mapper, 是hadoop 2.x和3.x的使用
 * org.apache.hadoop.mapred.Mapper是hadoop1.x的使用
 *
 * 四个泛型为KEYIN, VALUEIN, KEYOUT, VALUEOUT
 * KEYIN, VALUEIN表示输入的kv的类型
 * KEYOUT, VALUEOUT表示输出的kv的类型
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    // 只在开始调用一次
    @Override
    protected void setup(Context context) throws IOException, InterruptedException { }

    // value是输入文件的每一行内容
    // key的当前行首字母的偏移量, 偏移量从0开始, 每一行后面的回车符和换行符算两个
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString(); // 获取每一行内容
        String[] words = line.split(" "); // 空格切分单词
        for (String w : words) {
            if (StringUtils.isNotBlank(w)) {
                context.write(new Text(w), new IntWritable(1)); // 发送到下游
            }
        }
    }

    // finally, 调用一次
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException { }
}
