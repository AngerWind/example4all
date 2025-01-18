package com.tiger.mapreduce._5_local_run;

import java.net.URI;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeysPublic;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRConfig;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

/**
 * @author Shen
 * @version v1.0
 * @Title FlowDriver
 * @date 2022/2/26 17:39
 * @description
 */
public class FlowDriver implements Tool {

    private Configuration configuration;

    public static void main(String[] args) throws Exception {
        /**
         * 解析command line中的参数
         * @see org.apache.hadoop.util.GenericOptionsParser#processGeneralOptions
         * -jt xxx 设置resource manager的地址
         * -fs xxx 设置namenode地址
         * -conf xxx 添加一个配置文件路径，该配置文件将覆盖以前的配置文件属性
         * -D aaa=bbb 设置Configuration的一个属性值
         * -libjor xxx 设置分布式缓存jar，并添加到classpath，多个jar逗号隔开
         * -files xxx 设置分布式缓存文件，多个逗号隔开
         * -archives xxx 设置分布式压缩包文件， 多个逗号隔开
         * -tokenCacheFile 貌似与认证有关，不知道啥用
         *
         * 所有配置都是后面的覆盖前面的， 所以以上所有配置都会被代码中的硬编码指定的配置覆盖
         */
        ToolRunner.run(new Configuration(), new FlowDriver(), args);
    }

    /**
     * 调试使用类
     */
    @Override
    public int run(String[] args) throws Exception {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger("root").setLevel(Level.INFO);

        // 设置环形缓冲区大小20MB
        this.configuration.set(MRJobConfig.IO_SORT_MB, "2");

        // 设置hdfs的用户
        System.setProperty("HADOOP_USER_NAME", "tiger");

        Job job = Job.getInstance(this.configuration);

        job.setCombinerClass(FlowReducer.class);

        job.setMapOutputValueClass(FlowBean.class);
        job.setMapOutputKeyClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);

        job.setJarByClass(FlowDriver.class);

        FileInputFormat.setInputPaths(job, new Path("C:\\Users\\Tiger.Shen\\Desktop\\aa.txt"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Users\\Administrator\\Desktop\\output" + new Random().nextInt()));

        return job.waitForCompletion(true) ? 1 : 0;
    }

    @Override
    public void setConf(Configuration conf) {
        this.configuration = conf;
    }

    @Override
    public Configuration getConf() {
        return this.configuration;
    }
}
