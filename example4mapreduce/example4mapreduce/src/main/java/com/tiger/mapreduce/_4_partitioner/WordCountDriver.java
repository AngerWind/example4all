package com.tiger.mapreduce._4_partitioner;

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

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        job.setJarByClass(WordCountDriver.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.setInputPaths(job, new Path("C:\\Users\\Administrator\\Desktop\\aa.txt"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Users\\Administrator\\Desktop\\output"));

        /**
         * 设置分区个数, 分区个数对应ReduceTask的个数
         * 设置自定义的分区类, 对MapTask输出的KV进行分区
         * 分区数需要大于1, 否则不会使用自定义的分区类进行分区
         */
        job.setNumReduceTasks(4);
        job.setPartitionerClass(PhonePartitioner.class);


        boolean result = job.waitForCompletion(true);

    }

}
