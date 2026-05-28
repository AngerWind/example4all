package com.tiger._02_datastream._1_execution_environment;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.config.TableConfigOptions;

public class _02_EnvironmentSettingTest {

    public static void main(String[] args) {

        // flink的配置
        Configuration configuration = new Configuration();
        configuration.set(TableConfigOptions.MAX_LENGTH_GENERATED_CODE, Integer.MAX_VALUE);


        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance()
            .inStreamingMode() // 设置流/批模式
            .withBuiltInCatalogName("default_catalog") // 设置默认使用的catalog
            .withBuiltInDatabaseName("default_database") // 设置默认使用的database
            /*
                 设置类加载器, 用于
                     1. using in the planner for operations related to code generation,
                     2. UDF loading
                     3. operations requiring reflections on user classes
                     4. discovery of factories.
             */
            .withClassLoader(Thread.currentThread().getContextClassLoader())
            .withConfiguration(configuration) // 添加其他的配置
            .build();
    }
}
