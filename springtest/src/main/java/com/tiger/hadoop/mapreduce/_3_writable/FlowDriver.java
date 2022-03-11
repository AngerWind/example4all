package com.tiger.hadoop.mapreduce._3_writable;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeysPublic;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRConfig;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Random;

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

    @Override
    public int run(String[] args) throws Exception {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger("root").setLevel(Level.INFO);

        // 添加额外的配置文件
        this.configuration.addResource("hadoop/config/mapred-site.xml");
        this.configuration.addResource("hadoop/config/yarn-site.xml");
        this.configuration.addResource("hadoop/config/hdfs-site.xml");
        this.configuration.addResource("hadoop/config/core-site.xml");


        // 设置环形缓冲区大小20MB
        this.configuration.set(MRJobConfig.IO_SORT_MB, "20");
        // 设置使用yarn
        this.configuration.set(MRConfig.FRAMEWORK_NAME, MRConfig.YARN_FRAMEWORK_NAME);
        // 设置hdfs地址
        this.configuration.set(CommonConfigurationKeysPublic.FS_DEFAULT_NAME_KEY, "hdfs://hadoop:8082");
        // 设置resource manager地址
        this.configuration.set(YarnConfiguration.RM_ADDRESS, "hadoop:8032");
        // 设置hdfs的用户
        System.setProperty("HADOOP_USER_NAME", "tiger");
        // windows提交到linux上需要设置用户可以跨平台提交，否则提交成功但是执行失败
        this.configuration.setBoolean("mapreduce.app-submission.cross-platform", true);

        // 设置HADOOP_MAPRED_HOME, 否则跨平台可能找不到主类MRAppMaster
        this.configuration.set("yarn.app.mapreduce.am.env", "HADOOP_MAPRED_HOME=${HADOOP_HOME}");
        this.configuration.set("mapreduce.map.env", "HADOOP_MAPRED_HOME=${HADOOP_HOME}");
        this.configuration.set("mapreduce.reduce.env", "HADOOP_MAPRED_HOME=${HADOOP_HOME}");

        // 设置分布式缓存文件
        this.configuration.set(MRJobConfig.FILES_FOR_SHARED_CACHE, "");
        // 设置分布式缓存jar, 添加到classpath
        this.configuration.set(MRJobConfig.FILES_FOR_CLASSPATH_AND_SHARED_CACHE, "");
        // 设置分布式缓存压缩包
        this.configuration.set(MRJobConfig.ARCHIVES_FOR_SHARED_CACHE, "");


//        this.configuration.set("io.serializations", String.format("%s,%s", JavaSerialization.class, WritableSerialization.class));
        Job job = Job.getInstance(this.configuration);

        job.setCombinerClass(FlowReducer.class);
        job.setCacheFiles(new URI[]{new URI("")});

        job.setMapOutputValueClass(FlowBean.class);
        job.setMapOutputKeyClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);

//        job.setJarByClass(FlowDriver.class);
        job.setJar("C:\\Users\\Tiger.Shen\\Desktop\\example4all\\springtest\\target\\springtest-0.0.1-SNAPSHOT.jar");

//        FileInputFormat.setInputPaths(job, new Path("C:\\Users\\Tiger.Shen\\Desktop\\aa.txt"));
//        FileOutputFormat.setOutputPath(job, new Path("C:\\Users\\Administrator\\Desktop\\output"));

        FileInputFormat.setInputPaths(job, new Path("/aa.txt"));
        FileOutputFormat.setOutputPath(job, new Path("/output2/" + new Random().nextInt()));

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
