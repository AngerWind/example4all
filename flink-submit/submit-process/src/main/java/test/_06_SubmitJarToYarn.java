package test;

import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.*;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.yarn.YarnClientYarnClusterInformationRetriever;
import org.apache.flink.yarn.YarnClusterClientFactory;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.ApplicationId;


import java.util.concurrent.CompletableFuture;

public class _06_SubmitJarToYarn {

    public static void main(String[] args) throws ClusterRetrieveException {


        final ClusterClientFactory<ApplicationId> clientFactory = new YarnClusterClientFactory();

        // 创建了一个descriptor, 用于跟yarn进行交互
        try (final ClusterDescriptor<ApplicationId> clusterDescriptor =
                 clientFactory.createClusterDescriptor(new Configuration())) {

            // ClusterSpecification 用于指定flink集群的资源配置
            // 从Configuration中来解析出ClusterSpecification
            final ClusterSpecification clusterSpecification =
                clientFactory.getClusterSpecification(new Configuration());

            // 也可以直接构建
            ClusterSpecification.ClusterSpecificationBuilder builder = new ClusterSpecification.ClusterSpecificationBuilder();
            builder.setMasterMemoryMB(1024);
            builder.setTaskManagerMemoryMB(1024);
            builder.setSlotsPerTaskManager(4);
            ClusterSpecification clusterSpecification1 = builder.createClusterSpecification();

            // 构建ApplicationConfiguration, 用于指定应用的主类和参数
            String mainClass = "org.apache.flink.streaming.examples.wordcount.WordCount";
            String[] programArguments = new String[]{"--input", "hdfs://localhost:9000/input.txt", "--output", "hdfs://localhost:9000/output.txt"};
            ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(programArguments, mainClass );


            // 部署flink集群和作业, ApplicationConfiguration可以指定应用的主类和参数
            ClusterClientProvider<ApplicationId> clusterClientProvider = clusterDescriptor.deployApplicationCluster(
                clusterSpecification, applicationConfiguration);

            // 获取部署的flink集群的client, 用于与flink集群进行交互
            ClusterClient<ApplicationId> clusterClient = clusterClientProvider.getClusterClient();

            // 提交作业
            // clusterClient.submitJob()

        } catch (ClusterDeploymentException e) {
            // 无法部署作业
            throw new RuntimeException(e);
        }

    }
}
