package com.tiger.quartz;

import lombok.SneakyThrows;
import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 分布式调度案例
 */

public class ClustedScheduler {

    @Test
    @SneakyThrows
    public void test(){

    }

    public Scheduler getSchedulerByFactory(DataSource dataSource)
    {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);

        // quartz参数
        Properties prop = new Properties();
        prop.put("org.quartz.scheduler.instanceName", "ZyxScheduler"); // 集群的名字， 同一个名字的quartz才属于一个集群
        prop.put("org.quartz.scheduler.instanceId", "AUTO"); //如果使用集群，instanceId必须唯一，设置成AUTO
        // 线程池配置
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "20"); //线程数
        prop.put("org.quartz.threadPool.threadPriority", "5"); //优先级
        // JobStore配置
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX"); // 配置使用数据库进行quartz数据存储， 默认是内存

        // 集群配置
        prop.put("org.quartz.jobStore.isClustered", "true"); //是否是集群模式
        prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
        prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "true");

        // mysql 启用
        // prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS UPDLOCK WHERE LOCK_NAME = ?");
        prop.put("org.quartz.jobStore.misfireThreshold", "12000");
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_"); // 数据库表前缀
        factory.setQuartzProperties(prop);

        // factory.setSchedulerName("ZyxScheduler"); // 与org.quartz.scheduler.instanceName一样
        // 延时启动
        factory.setStartupDelay(1);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        // 可选，QuartzScheduler
        // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        // 设置自动启动，默认为true
        factory.setAutoStartup(true);

        return factory.getObject();
    }

    public Scheduler getSchedulerByPropertyFile() {
        String propertyFile = "classpath:/quartz/quartz.properties";
        Properties properties = new Properties();

        try (InputStream fileStream = this.getClass().getResourceAsStream(propertyFile);) {
            properties.load(fileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            StdSchedulerFactory stdScheduler = new StdSchedulerFactory(properties);
            return stdScheduler.getScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
