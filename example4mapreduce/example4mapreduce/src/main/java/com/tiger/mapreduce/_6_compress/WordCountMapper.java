package com.tiger.mapreduce._6_compress;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {


    @Override
    protected void setup(Context context) throws IOException, InterruptedException { }


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

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException { }
}
