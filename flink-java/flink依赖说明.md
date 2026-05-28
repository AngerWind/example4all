## flink关于scala版本的说明

在flink1.15.0之前, flink的所有依赖都是带有2个scala版本信息的

因为scala2.12和scala2.11生成的scala字节码是不兼容的, 所以有两个scala版本

比如`flink-streaming-java`这个包来说, 他有两个maven坐标

~~~xml
<dependency>
	<groupId>org.apache.flink</groupId>
	<artifactId>flink-streaming-java_2.12</artifactId>
	<version>1.14.16</version>
</dependency>
~~~

和

~~~xml
<dependency>
	<groupId>org.apache.flink</groupId>
	<artifactId>flink-streaming-java_2.11</artifactId>
	<version>1.14.16</version>
</dependency>
~~~

而在flink1.15.0的时候, 全部的依赖包都已经去除掉了scala的版本信息, 可以使用`flink-streaming-java`来作为依赖包了

~~~xml
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-streaming-java</artifactId>
    <version>1.15.0</version>
</dependency>
~~~



## flink各个依赖包的说明

1. 如果你想要使用flink的datastream api的话, 那么需要导入

   ~~~xml
   <dependency>
       <groupId>org.apache.flink</groupId>
       <artifactId>flink-streaming-java</artifactId>
       <version>1.20.0</version>
   </dependency>
   ~~~

2. flink-clients

   `flink-clients` 提供了 Flink 客户端 API 和命令行工具支持

   如果你想要通过代码来提交flink作业到Standalone, yarn, kubernetes, 那么需要导入这个jar包

   如果你想要通过代码来提交到yarn, kubernets上面, 那么还需要导入对应的`flink-yarn`和`flink-kubernetes`

3. flink-runtime-web

   `flink-runtime-web` 是 Flink 的一个模块，用于提供 **Web UI 的后端支持和 HTTP REST 接口实现**，主要用于监控、查询和管理正在运行的 Flink 作业和集群状态。

   如果你想要在**yarn, k8s, local模式**下启动的flink集群能够有web界面, 那么需要导入这个jar

   如果你通过命令行启动一个Standalone Session的flink集群, 那么他会自动使用这个依赖

4. 如果你想要使用flink sql的话, 那么需要导入如下的依赖

   ~~~xml
   
   <!-- DataStream api -->
   <dependency>
       <groupId>org.apache.flink</groupId>
       <artifactId>flink-streaming-java</artifactId>
       <version>1.20.0</version>
   </dependency>
   
           <!-- 编写table api所需要的jar -->
   	    <dependency>
               <groupId>org.apache.flink</groupId>
               <artifactId>flink-table-api-java</artifactId>
               <version>${flink.version}</version>
           </dependency>
           <!-- 真正执行sql/table作业的逻辑 -->
   		<dependency>
               <groupId>org.apache.flink</groupId>
               <artifactId>flink-table-runtime</artifactId>
               <version>${flink.version}</version>
           </dependency>
           <!-- 桥接DataStream API 和 Table API -->
   		<dependency>
               <groupId>org.apache.flink</groupId>
               <artifactId>flink-table-api-java-bridge</artifactId>
               <version>${flink.version}</version>
           </dependency>
   
           <!-- flink 自定义的数据格式想要做序列化, 还需要这个包-->
           <dependency>
               <groupId>org.apache.flink</groupId>
               <artifactId>flink-table-common</artifactId>
               <version>${flink.version}</version>
           </dependency>
   ~~~

   在以下的场景下, 你可以将scope设置为provider

   1. 你开发的是 Flink 插件 / UDF / jar 作业，运行时这些依赖会被 Flink 提供

   2. 你的 jar 会提交到 Flink 集群执行，而集群 classpath 已经包含了这些 jar

   3. 你的代码是使用Flink SQL Gateway来执行sql文件

   否则的话, 最好将依赖设置为compile

5. flink-table-planner-loader

   不知道干什么的

   

## flink和flinkcdc的connector依赖说明

1. `flink-connector-mysql-cdc`和`flink-sql-connector-mysql-cdc`和`flink-cdc-pipeline-connector-mysql`的区别

   在flink cdc中, 如果你要使用yaml来创建一个mysql cdc的任务, 那么你需要将`flink-cdc-pipeline-connector-mysql`放在`${FLINK_CDC_HOME/lib}`下

   

   如果你想在flink sql client中创建mysql cdc表, 那么需要将`flink-sql-connector-mysql-cdc`放在`${FLINK_HOME}/lib`下

   

   如果你想在代码的flink sql中创建一个mysql cdc表, 那么使用`flink-sql-connector-mysql-cdc`或者``flink-connector-mysql-cdc`都是可以的, 实际上`flink-sql-connector-mysql-cdc`就是`flink-connector-mysql-cdc`的一个fat包

2. `flink-connector-jdbc`和`flink-connector-jdbc-mysql`的区别

   实际上flink-connector-jdbc是一个大的项目, 负责整个jdbc模块的连接, flink-connector-jdbc-mysql是他的模块, 专门用于处理mysql的

   同时flink-connector-jdbc在打包的时候, 又是一个fat包, 他会将所有的子模块和子模块的依赖都打包进去, 所以我们在使用的时候, 只需要使用`flink-connector-jdbc`这个包就好了





## Flink 命令行提交任务

- yarn session

  ~~~shell
  ./flink run -t yarn-session -yid application_1671607810626_0001 -c com.lanson.flinkjava.code.chapter3.FlinkAppWithMultiJob /root/FlinkJavaCode-1.0-SNAPSHOT-jar-with-dependencies.jar
  
  ./flink run -t yarn-session -m hadoop103:8081 -c com.lanson.flinkjava.code.chapter3.FlinkAppWithMultiJob /root/FlinkJavaCode-1.0-SNAPSHOT-jar-with-dependencies.jar
  ~~~

- yarn per job(1.15废弃)

  ~~~shell
  ./flink run -t yarn-per-job -yid application_1671607810626_0001 -c com.lanson.flinkjava.code.chapter3.FlinkAppWithMultiJob /root/FlinkJavaCode-1.0-SNAPSHOT-jar-with-dependencies.jar
  ~~~

- yarn application

  ~~~shell
  ./flink run-applica -t yarn-application -yid application_1671607810626_0001 -c com.lanson.flinkjava.code.chapter3.FlinkAppWithMultiJob /root/FlinkJavaCode-1.0-SNAPSHOT-jar-with-dependencies.jar
  ~~~

  