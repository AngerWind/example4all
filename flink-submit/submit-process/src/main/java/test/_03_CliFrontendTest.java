package test;

import org.apache.flink.client.cli.CliFrontend;

import java.io.File;
import java.net.MalformedURLException;

public class _03_CliFrontendTest {

    public static void main(String[] args) throws MalformedURLException {
        /*
            exec /opt/module/jdk1.8.0_271/bin/java
            -Dlog.file=/opt/module/flink-1.17.2/log/flink-tiger-client-Wind.log
            -Dlog4j.configuration=file:///J:\desktop-shortcut\example\example4all\flink-submit\submit-process\src\main\resources\flink\conf\log4j-cli.properties
            -Dlog4j.configurationFile=file///:J:\desktop-shortcut\example\example4all\flink-submit\submit-process\src\main\resources\flink\conf\log4j-cli.properties
            -Dlogback.configurationFile=file///:J:\desktop-shortcut\example\example4all\flink-submit\submit-process\src\main\resources\flink\conf\logback.xml
            -classpath /opt/module/flink-1.17.2/lib/flink-cep-1.17.2.jar:/opt/module/flink-1.17.2/lib/flink-connector-files-1.17.2.jar:/opt/module/flink-1.17.2/lib/flink-csv-1.17.2.jar:/opt/module/flink-1.17.2/lib/flink-json-1.17.2.jar:/opt/module/flink-1.17.2/lib/flink-scala_2.12-1.17.2.jar:/opt/module/flink-1.17.2/lib/flink-table-api-java-uber-1.17.2.jar:/opt/module/flink-1.17.2/lib/flink-table-planner-loader-1.17.2.jar:/opt/module/flink-1.17.2/lib/flink-table-runtime-1.17.2.jar:/opt/module/flink-1.17.2/lib/log4j-1.2-api-2.17.1.jar:/opt/module/flink-1.17.2/lib/log4j-api-2.17.1.jar:/opt/module/flink-1.17.2/lib/log4j-core-2.17.1.jar:/opt/module/flink-1.17.2/lib/log4j-slf4j-impl-2.17.1.jar:/opt/module/flink-1.17.2/lib/flink-dist-1.17.2.jar:::
            org.apache.flink.client.cli.CliFrontend run -m localhost:8081 -c com.tiger.test.job.SimpleFlinkJob /mnt/j/desktop-shortcut/example/example4all/flink-java/target/flink-java-1.0-SNAPSHOT.jar

            -classpath 实际上就是flink的lib下的所有的jar
         */

        String[] submitToStandalone = {
            "run", "-m", "172.31.81.41:8081", "-c", "com.tiger.job.SimpleFlinkJob",
            "-m", "wind:8081",
            "-C", new File("J:\\desktop-shortcut\\example\\example4all\\flink-submit\\submit-common\\target\\submit-common-1.0-SNAPSHOT.jar").toURI().toURL().toString(),
            "J:\\desktop-shortcut\\example\\example4all\\flink-submit\\submit-job\\target\\submit-job-1.0-SNAPSHOT.jar"
        };

        String[] submitToYarnPerJob = {
            "run",
            "-t", "yarn-per-job",
            "-m", "172.31.81.41:8081",
            "-c", "com.tiger.job.SimpleFlinkJob",
            "-m", "wind:8081",
            "-C", new File("J:\\desktop-shortcut\\example\\example4all\\flink-submit\\submit-common\\target\\submit-common-1.0-SNAPSHOT.jar").toURI().toURL().toString(),
            "J:\\desktop-shortcut\\example\\example4all\\flink-submit\\submit-job\\target\\submit-job-1.0-SNAPSHOT.jar"
        };

        String[] submitToYarnPerJob = {
            "run",
            "-t", "yarn-per-job",
            "-m", "172.31.81.41:8081",
            "-c", "com.tiger.job.SimpleFlinkJob",
            "-m", "wind:8081",
            "-C", new File("J:\\desktop-shortcut\\example\\example4all\\flink-submit\\submit-common\\target\\submit-common-1.0-SNAPSHOT.jar").toURI().toURL().toString(),
            "J:\\desktop-shortcut\\example\\example4all\\flink-submit\\submit-job\\target\\submit-job-1.0-SNAPSHOT.jar"
        };

        CliFrontend.main(submitToStandalone);
    }
}
