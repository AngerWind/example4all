package com.tiger.hadoop.mapreduce._3_spilt;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
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

        /**
         * {@link org.apache.hadoop.mapreduce.InputFormat#getSplits(JobContext)}用于对指定的文件进行分片
         * 分片个数决定了MapTask的个数
         * 不同的InputFormat对文件的分片方式不同, 个数也不同
         *
         * {{@link org.apache.hadoop.mapreduce.InputFormat#createRecordReader(InputSplit, TaskAttemptContext)}用于读取文件内容作为Map的输入
         * 所以这个方法决定了MapTask的输入KV的类型, 同时也决定了输入KV的内容
         * InputFormat的子类不仅有对hdfs文件内容的读取, 也有对DB内容的读取
         */
        // 指定InputFormat类, 默认是使用TextInputFormat对文件进行分片和读取
        job.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.setInputPaths(job, new Path("C:\\Users\\Administrator\\Desktop\\aa.txt"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Users\\Administrator\\Desktop\\output"));


        boolean result = job.waitForCompletion(true);

    }

}
