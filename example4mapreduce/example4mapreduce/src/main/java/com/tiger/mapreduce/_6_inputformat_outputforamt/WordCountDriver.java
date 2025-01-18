package com.tiger.mapreduce._6_inputformat_outputforamt;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * @author Shen
 * @version v1.0
 * @Title WordCount
 * @date 2022/2/24 23:02
 * @description
 */

/**
 * 所有的InputFormat都要实现InputFormat接口
 * InputFormat主要有以下几类:
 *      DBInputFormat: 能够从db中读取数据的format
 *          DataDrivenDBInputFormat: 一个 InputFormat，用于从 SQL 表中读取输入数据。操作类似于 DBInputFormat，
 *                                      但它不是使用 LIMIT 和 OFFSET 来划分拆分，
 *                                      而是尝试生成 WHERE 子句，将数据分成大致相等的分片
 *              OracleDataDrivenDBInputFormat: 从Oracle 数据库读取输入数据。
 *      FileInputFormat: 从文件中读取读取内容的format, 是一个基类
 *          CombineFileInputFormat: 能够将一个文件夹下的所有文件当做一个文件, 来进行分片, 是一个基类
 *          FixedLengthInputFormat: 根据固定长度来读取数据, 是FileInputFormat<LongWritable, BytesWritable>类型
 *          TextInputFormat: 按行读取文本文件的format
 *              CombineTextInputFormat: 能够读取多个文本文件的format
 *              KeyValueTextInputFormat: 按行读取文本文件, 并将行内容通过`\t`进行分割, 转换为key和value,
 *                                         并将key和value转换为Text类型
 *          SequenceFileInputFormat: 能够读取sequence文件的format
 *              CombineSequenceFileInputFormat: 能够读取多个Sequence文件的format
 *              SequenceFileAsTextInputFormat: 会自动的将读取出来的key和value转换为string类型
 *              SequenceFileAsBinaryInputFormat: 会自动的将读取出来的key和value转换为byte[]类型
 */
public class WordCountDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        // 1. 获取job
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        // 2. 设置jar包路径
        job.setJarByClass(WordCountDriver.class);

        // 3. 关联mapper和reducer
        job.setMapperClass(DoNothingMapper.class);
        job.setReducerClass(DoNothingReducer.class);

        // 4. 设置mapper的输出的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 5. 设置最终的输出的kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 设置输入和输出的文件格式
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // 设置TextInputFormat和TextOutputFormat的读写路径
        FileInputFormat.setInputPaths(job, new Path("/input/aa.txt"));
        FileOutputFormat.setOutputPath(job, new Path("/output"));

        // 提交到hadoop集群上跑的时候是hdfs的路径
        // hadoop -jar 当前包名 当前类全类名 args...


        // 7. 提交job
        // 结果在输出路径的part-r-00000里面, r表示Reduce的输出文件
        // 执行有三种类型:
        //      当做普通jar包执行main方法, 输入输出在本地, 运行在当前机器
        //      提交到hadoop上, 输入输出在hdfs上, 任务运行在当前机器
        //      提交到hadoop上, 输入输出在hdfs上, 任务运行在yarn上
        boolean result = job.waitForCompletion(true);

    }

}
