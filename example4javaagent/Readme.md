### javaagent的由来
JVM的设计人员很聪明，在JVM设计之初，他们就考虑了虚拟机状态监控、DEBUG、线程和内存分析等功能。

在JDK1.5之前，JVM规范定义了JVMPI（**Java Virtual Machine Profiler Interface**）语义，JVMPI提供了一批JVM分析接口。
VMPI 可以监控就JVM发生的各种事件，比如，JVM创建、关闭、Java类被加载、创建对象或GC回收等37种事件。

除了JVMPI ，JVM规范还定义了JVMDI（**Java Virtual Machine Debug Interface**）语义，JVMDI提供了一批JVM调试接口。

JDK 1.5及之后的版本将JVMPI和JVMDI合二为一，形成了一套JVM语义——JVMTI（**JVMTM Tool Interface**），包括JVM 分析接口和JVM调试接口。

JVMTI 是开发和监控工具使用的编程接口，它提供了一种方法，用于检查状态和控制在Java 虚拟机（VM）中运行的应用程序的执行。

简单来说，JVMTI是一套JVM制定的规范，在不同的JVM中实现方式也不一定都一样。JVMTI提供的是Native API调用方式（即大家更熟知的JNI调用方式）。

JVMTI接口用C/C++语言来暴露Native API，并最终以动态链路库的形式被JVM加载并运行。

在Java Agent 问世之前，苦逼的开发人员只能通过JVMTI的Native API调用方式完成代码的动态侵入，非常的繁琐、不友好，而且门槛有点高。

因此，在JDK1.5版本以后，JVM提供了Java Agent技术来解决上述痛点。

在JDK 1.5之后，JVM提供了探针接口（Instrumentation接口），便于开发人员基于Instrumentation接口编写Java Agent。

但是，Instrumentation接口底层依然依赖JVMTI语义的Native API，相当于给用户封装了一下，降低了使用成本。

在JDK 1.6及之后的版本，JVM又提供了Attach接口，便于开发人员使用Attach接口实现Java Agent。和Instrumentation接口一样，Attach接口底层也依赖JVMTI语义的Native API。

### 基于 Instrumentation 接口和premain()方法实现Java Agent
agent项目结构如下:
│ pom.xml
├─src
│  └─main
│      └─java
│          └─com
│              └─tiger
│                      IntrumentationApiDemo.java
1.  在pom.xml
通过JVM参数「-javaagent:*.jar」启动应用。

应用在启动时，会优先加载Java Agent，并执行premain()方法，这时部分的类都还没有被加载。

此时，可以实现对新加载的类进行字节码修改，但如果premain()方法执行失败或者抛出异常，则JVM会被终止，这太蛋疼了。