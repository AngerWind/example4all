package com.tiger.mapreduce._1_wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author Shen
 * @version v1.0
 * @Title WordCount
 * @date 2022/2/24 23:02
 * @description
 */
public class WordCountDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        // 1. 获取job
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        // 2. 设置jar包路径
        job.setJarByClass(WordCountDriver.class);

        // 3. 关联mapper和reducer
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // 4. 设置mapper的输出的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 5. 设置最终的输出的kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 6. 设置文件的输入路径和输出路径
        // 输出路径指定的目录需要不存在

        // 在本地跑的时候path是当前机器的路径
        FileInputFormat.setInputPaths(job, new Path("C:\\Users\\Administrator\\Desktop\\aa.txt"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Users\\Administrator\\Desktop\\output"));

        // 设置输入和输出的文件格式
        job.setInputFormatClass(FileInputFormat.class);
        job.setOutputFormatClass(FileOutputFormat.class);

        // 提交到hadoop集群上跑的时候是hdfs的路径
        // hadoop -jar 当前包名 当前类全类名 args...
        // FileInputFormat.setInputPaths(job, new Path("/input/aa.txt"));
        // FileOutputFormat.setOutputPath(job, new Path("/output"));

        // 7. 提交job
        // 结果在输出路径的part-r-00000里面, r表示Reduce的输出文件
        // 执行有三种类型:
        //      当做普通jar包执行main方法, 输入输出在本地, 运行在当前机器
        //      提交到hadoop上, 输入输出在hdfs上, 任务运行在当前机器
        //      提交到hadoop上, 输入输出在hdfs上, 任务运行在yarn上
        boolean result = job.waitForCompletion(true);

    }

}
