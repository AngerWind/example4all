package com.tiger.hadoop.mapreduce._2_writable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author Shen
 * @version v1.0
 * @Title FlowDriver
 * @date 2022/2/26 17:39
 * @description
 */
public class FlowDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration();
        configuration.set(MRJobConfig.IO_SORT_MB, "5");
        Job job = Job.getInstance(configuration);


        job.setMapOutputValueClass(FlowBean.class);
        job.setMapOutputKeyClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);

        job.setJarByClass(FlowDriver.class);

        job.setNumReduceTasks(3);

        FileInputFormat.setInputPaths(job, new Path("C:\\Users\\Administrator\\Desktop\\qq.txt"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Users\\Administrator\\Desktop\\output"));

        job.waitForCompletion(true);
    }
}
