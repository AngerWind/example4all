package com.tiger.mapreduce._6_compress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
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
        job.setInputFormatClass(TextInputFormat.class);

        /**
         * 总共有3个地方可以开启压缩和解压缩
         * 1. map输入的时候, 进行文件的解压
         * 2. map输出的时候, shuffle文件的压缩
         * 3. reducer输入的时候, shuffle文件的解压
         * 4. reducer输出的时候, 进行文件的压缩
         *
         * 其中map输入的时候, 和reducer输入的时候, 不需要指定解压方式,
         * mapreducer会自动根据输入的文件名后缀来查看是否支持解压
         * 如果想要在输入的时候, 添加解压的方式, 可以使用io.compression.codecs来设置所有codesc, 多个类名使用逗号隔开
         * 或者使用CompressionCodecFactory#setCodecClasses()方法
         *
         * 在map输出的时候, 可以使用MRJobConfig#MAP_OUTPUT_COMPRESS来开启压缩
         * 此时默认会使用DefaultCodec.class来进行压缩
         * 也可以使用MRJobConfig#MAP_OUTPUT_COMPRESS_CODEC来指定要使用的压缩类
         *
         * 在reducer输出的时候, 可以使用FileOutputFormat#COMPRESS来开启压缩
         * 使用FileOutputFormat#COMPRESS_CODEC来指定压缩的方式
         * 使用FileOutputFormat#OUTDIR来指定输出文件的位置
         * 当然也可以使用FileOutputFormat来快速设置上面三个属性
         *
         */
        // 开启map端输出压缩
        configuration.setBoolean(MRJobConfig.MAP_OUTPUT_COMPRESS, true);
        // 设置map端输出压缩方式
        configuration.setClass(MRJobConfig.MAP_OUTPUT_COMPRESS_CODEC, BZip2Codec.class, CompressionCodec.class);

        // 设置reduce端输出压缩开启
        FileOutputFormat.setCompressOutput(job, true);
        // 设置压缩的方式
        FileOutputFormat.setOutputCompressorClass(job, BZip2Codec.class);
	    // FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
	    // FileOutputFormat.setOutputCompressorClass(job, DefaultCodec.class);
        // 设置reducer文件输出的目录
        FileOutputFormat.setOutputPath(job, new Path("output"));

        FileInputFormat.setInputPaths(job, new Path("C:\\Users\\Administrator\\Desktop\\aa.txt"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Users\\Administrator\\Desktop\\output"));


        boolean result = job.waitForCompletion(true);

    }

}
