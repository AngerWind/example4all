package com.tiger.quartz;

import static org.quartz.TriggerBuilder.newTrigger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import lombok.SneakyThrows;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title SimpleTrigger
 * @date 2022/4/27 19:16
 * @description
 */
public class SimpleTriggerTest {

    @Test
    @SneakyThrows
    public void test(){

        JobKey jobKey = new JobKey("111", "222");


        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate2 = dateFormat2.parse("2022-04-27 20:31:00");
        SimpleTrigger simpleTrigger = newTrigger()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withRepeatCount(0)
                        .withIntervalInSeconds(1))
                .startAt(myDate2).build();

        JobDetail jobDetail = JobBuilder.newJob(JobTest.class).withIdentity(jobKey).build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        System.out.println(scheduler.getSchedulerName());
        System.out.println(scheduler.getSchedulerInstanceId());


        scheduler.scheduleJob(jobDetail, simpleTrigger);

        // DateUtil.toIntSecond()

        Thread.sleep(1000*60);
    }

    public static class JobTest implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("111111");
        }
    }
}
