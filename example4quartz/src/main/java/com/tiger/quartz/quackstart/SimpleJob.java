package com.tiger.quartz.quackstart;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SimpleJob
 * @date 2021/1/24 14:19
 * @description
 */
// 标记在Job类上，告诉框架该Job执行完成后持久化它的JobDataMap，这样多次执行就可以在其中保存一些有状态的值，如这里的count
// 否则的话JobDataMap永远是初始的值
@PersistJobDataAfterExecution
public class SimpleJob implements Job {

    // job每次执行都会创建一个新的实例，所以构造函数每次都会执行
    public SimpleJob() {
        System.out.println("创建job");
    }

    // 当Trigger或者JobDetail的jobDataMap中有setter对应的属性（这里是msg）时， 将会自动调用对应的setter方法
    // Trigger的jobDataMap中的属性将覆盖同名的JobDetail的jobDataMap中的属性
    public void setMsg(String message) {
        System.out.println("setter: " + message);
    }

    // 执行次数
    private Integer count;
    public void setCount(Integer count) {
        this.count = count;
    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 获取JobDetail
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobKey key = jobDetail.getKey();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        System.out.printf("Identity: %s, %s", key.getName(), key.getGroup());
        System.out.printf("JobDateMap: message is %s \n", jobDataMap.getString("msg"));

        // 获取Trigger
        Trigger trigger = jobExecutionContext.getTrigger();

        // 获取scheduler
        Scheduler scheduler = jobExecutionContext.getScheduler();

        // 将count++保存在jobDataMap中
        System.out.println("count: " + count);
        jobExecutionContext.getJobDetail().getJobDataMap().put("count", ++count);
    }

    public static void main(String[] args) throws SchedulerException {

        // 创建jobDetail
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("msg", "jobDetail message");
        jobDataMap.put("count", 0);
        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
                .usingJobData(jobDataMap)   // 添加给job的数据
                .withIdentity(new JobKey("job1", "group1")) // 添加job的唯一标识符，组名默认为"DEFAULT"
                .build();

        // 创建Trigger
        JobDataMap jobDataMap1 = new JobDataMap();
        jobDataMap1.put("msg", "trigger message");
        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey("trigger1", "group1"))
                .usingJobData(jobDataMap1)
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5))
                .startNow() // 设置开始时间
                .endAt(new Date(System.currentTimeMillis() + 24*1000*3600L)) // 设置结束时间
                .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.scheduleJob(jobDetail, simpleTrigger);
        scheduler.start();
    }
}
