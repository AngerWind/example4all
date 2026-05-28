package com.tiger;

import lombok.SneakyThrows;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.execution.DefaultExecutorServiceLoader;
import org.apache.flink.core.execution.JobClient;
import org.apache.flink.core.execution.PipelineExecutorServiceLoader;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.RemoteStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;


public class Job3 {

    /**
     * 测试带有依赖的sql, 能不能通过StreamTableEnvironment, 来提交到远程的集群中
     *     1. 只要你将所有需要的依赖, 打包为一个fat包, 并在jarFiles中指定, 就可以直接提交到远程集群上
     *     2. 如果你没有打包为一个fat包, 而是一个普通的不带依赖的包, 那么你就需要再jarFiles中指定不带依赖的包, 以及在classpaths中指定所有的依赖
     */
    @SneakyThrows
    public static void main(String[] args) throws MalformedURLException {

        PipelineExecutorServiceLoader executorServiceLoader = new DefaultExecutorServiceLoader(); // 默认就是他

        // 用于指定 RestOptions.ADDRESS 和 JobManagerOptions.ADDRESS
        String host = "172.31.81.41";
        // 用于指定 RestOptions.PORT 和 JobManagerOptions.PORT
        int port = 8081;

        Configuration config = new Configuration();

        // // 默认为null, 用于指定 PipelineOptions.JARS
        // String[] jarFiles = new String[] {
        //     "J:\\desktop-shortcut\\example\\example4all\\flink-submit\\submit-sql-job\\target\\submit-sql-job-1.0-SNAPSHOT-shade.jar"
        // };
        // // 默认为null, 用于指定 PipelineOptions.CLASSPATHS
        // URL[] globalClasspaths = new URL[] {
        //
        // };

        // 提交不带依赖的jar包, 并在classpath中指定所有的依赖
        String jarFile = "J:\\desktop-shortcut\\example\\example4all\\flink-submit\\submit-sql-job\\target\\submit-sql-job-1.0-SNAPSHOT.jar";
        String classpathStr = "E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-java\\1.17.2\\flink-java-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\commons\\commons-math3\\3.6.1\\commons-math3-3.6.1.jar;E:\\apache-maven\\apache-maven-repository\\com\\twitter\\chill-java\\0.7.6\\chill-java-0.7.6.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-streaming-java\\1.17.2\\flink-streaming-java-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-file-sink-common\\1.17.2\\flink-file-sink-common-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\slf4j\\slf4j-api\\1.7.36\\slf4j-api-1.7.36.jar;E:\\apache-maven\\apache-maven-repository\\com\\google\\code\\findbugs\\jsr305\\1.3.9\\jsr305-1.3.9.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-shaded-force-shading\\16.1\\flink-shaded-force-shading-16.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-shaded-asm-9\\9.3-16.1\\flink-shaded-asm-9-9.3-16.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-connector-base\\1.17.2\\flink-connector-base-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-clients\\1.17.2\\flink-clients-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-core\\1.17.2\\flink-core-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-annotations\\1.17.2\\flink-annotations-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-metrics-core\\1.17.2\\flink-metrics-core-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-shaded-jackson\\2.13.4-16.1\\flink-shaded-jackson-2.13.4-16.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\commons\\commons-text\\1.10.0\\commons-text-1.10.0.jar;E:\\apache-maven\\apache-maven-repository\\com\\esotericsoftware\\kryo\\kryo\\2.24.0\\kryo-2.24.0.jar;E:\\apache-maven\\apache-maven-repository\\com\\esotericsoftware\\minlog\\minlog\\1.2\\minlog-1.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\objenesis\\objenesis\\2.1\\objenesis-2.1.jar;E:\\apache-maven\\apache-maven-repository\\commons-collections\\commons-collections\\3.2.2\\commons-collections-3.2.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\commons\\commons-compress\\1.21\\commons-compress-1.21.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-runtime\\1.17.2\\flink-runtime-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-rpc-core\\1.17.2\\flink-rpc-core-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-rpc-akka-loader\\1.17.2\\flink-rpc-akka-loader-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-queryable-state-client-java\\1.17.2\\flink-queryable-state-client-java-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-hadoop-fs\\1.17.2\\flink-hadoop-fs-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\commons-io\\commons-io\\2.11.0\\commons-io-2.11.0.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-shaded-netty\\4.1.82.Final-16.1\\flink-shaded-netty-4.1.82.Final-16.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-shaded-zookeeper-3\\3.7.1-16.1\\flink-shaded-zookeeper-3-3.7.1-16.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\javassist\\javassist\\3.24.0-GA\\javassist-3.24.0-GA.jar;E:\\apache-maven\\apache-maven-repository\\org\\xerial\\snappy\\snappy-java\\1.1.10.4\\snappy-java-1.1.10.4.jar;E:\\apache-maven\\apache-maven-repository\\org\\lz4\\lz4-java\\1.8.0\\lz4-java-1.8.0.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-optimizer\\1.17.2\\flink-optimizer-1.17.2.jar;E:\\apache-maven\\apache-maven-repository\\commons-cli\\commons-cli\\1.5.0\\commons-cli-1.5.0.jar;E:\\apache-maven\\apache-maven-repository\\com\\ververica\\flink-connector-mysql-cdc\\2.4.1\\flink-connector-mysql-cdc-2.4.1.jar;E:\\apache-maven\\apache-maven-repository\\com\\ververica\\flink-connector-debezium\\2.4.1\\flink-connector-debezium-2.4.1.jar;E:\\apache-maven\\apache-maven-repository\\io\\debezium\\debezium-api\\1.9.7.Final\\debezium-api-1.9.7.Final.jar;E:\\apache-maven\\apache-maven-repository\\io\\debezium\\debezium-embedded\\1.9.7.Final\\debezium-embedded-1.9.7.Final.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\kafka\\connect-api\\3.2.0\\connect-api-3.2.0.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\kafka\\kafka-clients\\3.2.0\\kafka-clients-3.2.0.jar;E:\\apache-maven\\apache-maven-repository\\javax\\ws\\rs\\javax.ws.rs-api\\2.1.1\\javax.ws.rs-api-2.1.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\kafka\\connect-runtime\\3.2.0\\connect-runtime-3.2.0.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\kafka\\connect-transforms\\3.2.0\\connect-transforms-3.2.0.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\kafka\\kafka-tools\\3.2.0\\kafka-tools-3.2.0.jar;E:\\apache-maven\\apache-maven-repository\\net\\sourceforge\\argparse4j\\argparse4j\\0.7.0\\argparse4j-0.7.0.jar;E:\\apache-maven\\apache-maven-repository\\ch\\qos\\reload4j\\reload4j\\1.2.19\\reload4j-1.2.19.jar;E:\\apache-maven\\apache-maven-repository\\org\\bitbucket\\b_c\\jose4j\\0.7.9\\jose4j-0.7.9.jar;E:\\apache-maven\\apache-maven-repository\\com\\fasterxml\\jackson\\core\\jackson-annotations\\2.12.6\\jackson-annotations-2.12.6.jar;E:\\apache-maven\\apache-maven-repository\\com\\fasterxml\\jackson\\jaxrs\\jackson-jaxrs-json-provider\\2.12.6\\jackson-jaxrs-json-provider-2.12.6.jar;E:\\apache-maven\\apache-maven-repository\\com\\fasterxml\\jackson\\jaxrs\\jackson-jaxrs-base\\2.12.6\\jackson-jaxrs-base-2.12.6.jar;E:\\apache-maven\\apache-maven-repository\\com\\fasterxml\\jackson\\module\\jackson-module-jaxb-annotations\\2.12.6\\jackson-module-jaxb-annotations-2.12.6.jar;E:\\apache-maven\\apache-maven-repository\\jakarta\\xml\\bind\\jakarta.xml.bind-api\\2.3.2\\jakarta.xml.bind-api-2.3.2.jar;E:\\apache-maven\\apache-maven-repository\\jakarta\\activation\\jakarta.activation-api\\1.2.1\\jakarta.activation-api-1.2.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\glassfish\\jersey\\containers\\jersey-container-servlet\\2.34\\jersey-container-servlet-2.34.jar;E:\\apache-maven\\apache-maven-repository\\org\\glassfish\\jersey\\containers\\jersey-container-servlet-core\\2.34\\jersey-container-servlet-core-2.34.jar;E:\\apache-maven\\apache-maven-repository\\org\\glassfish\\hk2\\external\\jakarta.inject\\2.6.1\\jakarta.inject-2.6.1.jar;E:\\apache-maven\\apache-maven-repository\\jakarta\\ws\\rs\\jakarta.ws.rs-api\\2.1.6\\jakarta.ws.rs-api-2.1.6.jar;E:\\apache-maven\\apache-maven-repository\\org\\glassfish\\jersey\\inject\\jersey-hk2\\2.34\\jersey-hk2-2.34.jar;E:\\apache-maven\\apache-maven-repository\\org\\glassfish\\hk2\\hk2-locator\\2.6.1\\hk2-locator-2.6.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\glassfish\\hk2\\external\\aopalliance-repackaged\\2.6.1\\aopalliance-repackaged-2.6.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\glassfish\\hk2\\hk2-api\\2.6.1\\hk2-api-2.6.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\glassfish\\hk2\\hk2-utils\\2.6.1\\hk2-utils-2.6.1.jar;E:\\apache-maven\\apache-maven-repository\\javax\\xml\\bind\\jaxb-api\\2.3.0\\jaxb-api-2.3.0.jar;E:\\apache-maven\\apache-maven-repository\\javax\\activation\\activation\\1.1.1\\activation-1.1.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\eclipse\\jetty\\jetty-server\\9.4.44.v20210927\\jetty-server-9.4.44.v20210927.jar;E:\\apache-maven\\apache-maven-repository\\javax\\servlet\\javax.servlet-api\\3.1.0\\javax.servlet-api-3.1.0.jar;E:\\apache-maven\\apache-maven-repository\\org\\eclipse\\jetty\\jetty-http\\9.4.44.v20210927\\jetty-http-9.4.44.v20210927.jar;E:\\apache-maven\\apache-maven-repository\\org\\eclipse\\jetty\\jetty-io\\9.4.44.v20210927\\jetty-io-9.4.44.v20210927.jar;E:\\apache-maven\\apache-maven-repository\\org\\eclipse\\jetty\\jetty-servlet\\9.4.44.v20210927\\jetty-servlet-9.4.44.v20210927.jar;E:\\apache-maven\\apache-maven-repository\\org\\eclipse\\jetty\\jetty-security\\9.4.44.v20210927\\jetty-security-9.4.44.v20210927.jar;E:\\apache-maven\\apache-maven-repository\\org\\eclipse\\jetty\\jetty-util-ajax\\9.4.44.v20210927\\jetty-util-ajax-9.4.44.v20210927.jar;E:\\apache-maven\\apache-maven-repository\\org\\eclipse\\jetty\\jetty-servlets\\9.4.44.v20210927\\jetty-servlets-9.4.44.v20210927.jar;E:\\apache-maven\\apache-maven-repository\\org\\eclipse\\jetty\\jetty-continuation\\9.4.44.v20210927\\jetty-continuation-9.4.44.v20210927.jar;E:\\apache-maven\\apache-maven-repository\\org\\eclipse\\jetty\\jetty-util\\9.4.44.v20210927\\jetty-util-9.4.44.v20210927.jar;E:\\apache-maven\\apache-maven-repository\\org\\eclipse\\jetty\\jetty-client\\9.4.44.v20210927\\jetty-client-9.4.44.v20210927.jar;E:\\apache-maven\\apache-maven-repository\\org\\reflections\\reflections\\0.9.12\\reflections-0.9.12.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\maven\\maven-artifact\\3.8.4\\maven-artifact-3.8.4.jar;E:\\apache-maven\\apache-maven-repository\\org\\codehaus\\plexus\\plexus-utils\\3.3.0\\plexus-utils-3.3.0.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\kafka\\connect-json\\3.2.0\\connect-json-3.2.0.jar;E:\\apache-maven\\apache-maven-repository\\com\\fasterxml\\jackson\\datatype\\jackson-datatype-jdk8\\2.12.6\\jackson-datatype-jdk8-2.12.6.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\kafka\\connect-file\\3.2.0\\connect-file-3.2.0.jar;E:\\apache-maven\\apache-maven-repository\\io\\debezium\\debezium-connector-mysql\\1.9.7.Final\\debezium-connector-mysql-1.9.7.Final.jar;E:\\apache-maven\\apache-maven-repository\\io\\debezium\\debezium-core\\1.9.7.Final\\debezium-core-1.9.7.Final.jar;E:\\apache-maven\\apache-maven-repository\\com\\fasterxml\\jackson\\core\\jackson-core\\2.13.2\\jackson-core-2.13.2.jar;E:\\apache-maven\\apache-maven-repository\\com\\fasterxml\\jackson\\core\\jackson-databind\\2.13.2.2\\jackson-databind-2.13.2.2.jar;E:\\apache-maven\\apache-maven-repository\\com\\fasterxml\\jackson\\datatype\\jackson-datatype-jsr310\\2.13.2\\jackson-datatype-jsr310-2.13.2.jar;E:\\apache-maven\\apache-maven-repository\\com\\google\\guava\\guava\\30.1.1-jre\\guava-30.1.1-jre.jar;E:\\apache-maven\\apache-maven-repository\\com\\google\\guava\\failureaccess\\1.0.1\\failureaccess-1.0.1.jar;E:\\apache-maven\\apache-maven-repository\\com\\google\\guava\\listenablefuture\\9999.0-empty-to-avoid-conflict-with-guava\\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;E:\\apache-maven\\apache-maven-repository\\io\\debezium\\debezium-ddl-parser\\1.9.7.Final\\debezium-ddl-parser-1.9.7.Final.jar;E:\\apache-maven\\apache-maven-repository\\org\\antlr\\antlr4-runtime\\4.8\\antlr4-runtime-4.8.jar;E:\\apache-maven\\apache-maven-repository\\com\\zendesk\\mysql-binlog-connector-java\\0.27.2\\mysql-binlog-connector-java-0.27.2.jar;E:\\apache-maven\\apache-maven-repository\\com\\github\\luben\\zstd-jni\\1.5.0-2\\zstd-jni-1.5.0-2.jar;E:\\apache-maven\\apache-maven-repository\\com\\esri\\geometry\\esri-geometry-api\\2.2.0\\esri-geometry-api-2.2.0.jar;E:\\apache-maven\\apache-maven-repository\\com\\zaxxer\\HikariCP\\4.0.3\\HikariCP-4.0.3.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\commons\\commons-lang3\\3.7\\commons-lang3-3.7.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-shaded-guava\\30.1.1-jre-16.1\\flink-shaded-guava-30.1.1-jre-16.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\awaitility\\awaitility\\4.0.1\\awaitility-4.0.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\hamcrest\\hamcrest\\2.1\\hamcrest-2.1.jar;E:\\apache-maven\\apache-maven-repository\\org\\apache\\flink\\flink-connector-jdbc\\3.1.0-1.17\\flink-connector-jdbc-3.1.0-1.17.jar;E:\\apache-maven\\apache-maven-repository\\com\\mysql\\mysql-connector-j\\8.0.33\\mysql-connector-j-8.0.33.jar;E:\\apache-maven\\apache-maven-repository\\com\\google\\protobuf\\protobuf-java\\3.21.9\\protobuf-java-3.21.9.jar;E:\\apache-maven\\apache-maven-repository\\org\\projectlombok\\lombok\\1.18.22\\lombok-1.18.22.jar";
        String[] split = classpathStr.split(";");
        // ArrayList<String> strings = new ArrayList<>(Arrays.asList(split));
        // strings.addAll(Arrays.asList(jarFile));
        // String[] jarFiles = strings.toArray(new String[0]);


        URL[] globalClasspaths = Arrays.stream(split).map(str -> {
            try {
                return new File(str).toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }).toArray(URL[]::new);
        String[] jarFiles = new String[]{
            jarFile
        };

        // 默认就是他
        // 会通过 savepointRestoreSettings.allowNonRestoredState() 来设置
        // SavepointConfigOptions.SAVEPOINT_IGNORE_UNCLAIMED_STATE
        // 会通过 savepointRestoreSettings.getRestoreMode() 来设置 SavepointConfigOptions.RESTORE_MODE
        // 会通过 savepointRestoreSettings.getRestorePath() 来设置 SavepointConfigOptions.SAVEPOINT_PATH
        SavepointRestoreSettings savepointRestoreSettings = SavepointRestoreSettings.none();

        try (
            RemoteStreamEnvironment env = new RemoteStreamEnvironment(executorServiceLoader, host, port, config,
                jarFiles, globalClasspaths, savepointRestoreSettings);
            // LocalStreamEnvironment env = new LocalStreamEnvironment();
        ) {

            // 4. 创建 TableEnvironment
            EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .inStreamingMode()
                .build();

            StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);


            tableEnv.executeSql("CREATE TABLE account_source (\n" +
                "                         id INT,\n" +
                "                         name STRING,\n" +
                "                         balance INT,\n" +
                "                         PRIMARY KEY (id) NOT ENFORCED\n" +
                ") WITH (\n" +
                "    'connector' = 'mysql-cdc',\n" +
                "    'hostname' = '172.31.80.1',\n" +
                "    'port' = '3306',\n" +
                "    'username' = 'root',\n" +
                "    'password' = '871403165',\n" +
                "    'database-name' = 'test',\n" +
                "    'table-name' = 'account', \n" +
                "    'scan.startup.mode' = 'latest-offset' \n" +
                ");");
            tableEnv.executeSql("CREATE TABLE account_sink (\n" +
                "                              id INT,\n" +
                "                              name STRING,\n" +
                "                              balance INT,\n" +
                "                              PRIMARY KEY (id) NOT ENFORCED\n" +
                ") WITH (\n" +
                "    'connector' = 'jdbc',\n" +
                "    'url' = 'jdbc:mysql://172.31.80.1:3306/test',\n" +
                "    'table-name' = 'account1',\n" +
                "    'username' = 'root',\n" +
                "    'password' = '871403165'\n" +
                ");\n");


            TableResult result = tableEnv.executeSql("insert into account_sink select * from account_source;");
            Optional<JobClient> optionalClient = result.getJobClient();
            if (optionalClient.isPresent()) {
                JobClient jobClient = optionalClient.get();
                JobID jobID = jobClient.getJobID();
                System.out.println(jobID);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
