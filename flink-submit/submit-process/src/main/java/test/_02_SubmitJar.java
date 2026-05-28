package test;

import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.JobStatus;
import org.apache.flink.client.ClientUtils;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.client.program.rest.RestClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.execution.DefaultExecutorServiceLoader;
import org.apache.flink.runtime.client.JobStatusMessage;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.apache.flink.runtime.messages.webmonitor.JobDetails;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class _02_SubmitJar {

    public static void main(String[] args) throws Exception {

        // 通过rest api提交jar, 也就是调用接口的方式
        // 通过web ui来提交jar, 实际上也是这种方式
        Configuration config = new Configuration();
        config.setString("rest.address", "172.31.81.41");
        config.setInteger("rest.port", 8081);

        // 创建集群客户端
        try (RestClusterClient<String> client = new RestClusterClient<>(config, "default")) {

            String entryClass = "com.tiger.job.SimpleFlinkJob"; // main方法所在的类
            File jarFile = new File("J:\\desktop-shortcut\\example\\example4all\\flink-submit\\submit-job\\target\\submit-job-1.0-SNAPSHOT.jar"); // 要提交的jar的路径


            List<URL> userClassPaths = new ArrayList<>();

            String[] args1 = new String[]{}; // 传递给main方法中传入的参数
            SavepointRestoreSettings savepointRestoreSettings = SavepointRestoreSettings.none(); // 配置是否从某个 savepoint 或 checkpoint 恢复。
            Configuration configuration = new Configuration(); // 需要传递的其他配置,  比如并行度, 类加载参数, flink-conf中的配置也可以在这里定义, 比如taskmanager.memory.process.size = 1728m


            /*
                在创建的过程中, 会通过反射来调用jar包中的main方法, 并且使用的是FlinkUserCodeClassLoaders$SafetyNetWrapperClassLoader来加载class
                这个类是一个包装器, 真正的ClassLoader是他内部的ChildFirstClassLoader
                他首先会尝试从jarFile和userClassPaths中加载class
                如果还是找不到, 他会使用parent来加载class, 即AppClassLoader

                一般来说, 我们在将flink作业打包的时候, 有一些flink依赖会设置为provided, 因为flink集群上面已经有了
                这就导致了我们在创建PackagedProgram的时候, 因为没有没有这些包, 报ClassNotFound的异常
                有两种办法:
                    1. 在启动当前程序的时候, 将flink的lib下所有的jar指定到 java -classpath 中, 这样即使ChildFirstClassLoader加载不到, parent AppClassLoader也能加载到
                    2. 将flink的lib下所有的jar, 添加到userClassPaths中

                实际上你通过flink run来提交flink job的时候, 底层执行的就是方式1, 将flink的lib下的jar都指定在java -classpath中, 这样就不会导致解析失败了
                这里我们使用的是方式2, 所有的flink lib下的jar包都已经复制到了resources/flink-lib
         */
            PackagedProgram packagedProgram = PackagedProgram.newBuilder()
                .setJarFile(jarFile)
                .setUserClassPaths(userClassPaths)
                .setArguments(args1)
                .setEntryPointClassName(entryClass)
                .setSavepointRestoreSettings(savepointRestoreSettings)
                .setConfiguration(configuration)
                .build();

            JobGraph jobGraph = PackagedProgramUtils.createJobGraph(
                packagedProgram,
                new Configuration(), // 构建 JobGraph 过程中的配置, 影响作业结构和提交行为
                1, // 默认并行度
                false // 是否在JobGraph 创建期间抑制 stdout/stderr
            );

            // ClientUtils.executeProgram(
            //     new DefaultExecutorServiceLoader(), configuration, packagedProgram, false, false);

            // 提交 jar
            CompletableFuture<JobID> future = client.submitJob(jobGraph);
            JobID jobId = future.get();
            System.out.println("成功提交作业，JobID: " + jobId);

            String webInterfaceURL = client.getWebInterfaceURL();

            System.out.println("作业已提交成功！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void list() throws Exception {
        // 通过rest api提交jar, 也就是调用接口的方式
        // 通过web ui来提交jar, 实际上也是这种方式
        Configuration config = new Configuration();
        config.setString("rest.address", "172.31.81.41");
        config.setInteger("rest.port", 8081);

        // 创建集群客户端
        try (RestClusterClient<String> client = new RestClusterClient<>(config, "default")) {
            client.listJobs().thenAccept(jobs -> {
                for (JobStatusMessage jobDetails : jobs) {
                    JobID jobId = jobDetails.getJobId();
                    String jobName = jobDetails.getJobName();
                    JobStatus jobState = jobDetails.getJobState();
                    long startTime = jobDetails.getStartTime();
                }
            });

            String webInterfaceURL = client.getWebInterfaceURL();

            String clusterId = client.getClusterId();
            Configuration flinkConfiguration = client.getFlinkConfiguration();

            client.cancel(new JobID()).thenAccept(ack -> {

            });

            // client.cancelWithSavepoint()
            // client.disposeSavepoint()
            client.getJobDetails(new JobID()).thenAccept(jobDetailsInfo -> {

            });

            client.getJobStatus(new JobID()).thenAccept(jobStatus -> {
            });
        }
    }

}
