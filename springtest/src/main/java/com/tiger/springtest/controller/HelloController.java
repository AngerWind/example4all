package com.tiger.springtest.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiger.innerbean.Student;
import com.tiger.springtest.annotation.CoolMapping;
import com.tiger.springtest.annotation.HelloComponent;
import com.tiger.springtest.annotation.HotMapping;
import org.springframework.beans.factory.BeanNameAware;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title HelloController
 * @date 2020/12/8 19:44
 * @description
 */
@HelloComponent(value = "hello!!!!!")
public class HelloController implements BeanNameAware {

    @CoolMapping(path = "hello")
    public String hello() {
        return "hello world";
    }

    @HotMapping(path = "hot")
    public String hot() {
        return "hot !!!!";
    }

    @Override
    public void setBeanName(String name) {
        System.out.println(name);
    }

    public static void main(String[] args) {
        String processDefinitionJson = "{\"globalParams\":[{\"prop\":\"now\",\"direct\":\"IN\"," +
                "\"type\":\"STRING\",\"value\":\"$[yyyy-MM-dd HH:mm:ss]\"},{\"prop\":\"now_1\",\"direct\":\"IN\"," +
                "\"type\":\"STRING\",\"value\":\"$[yyyy-MM-dd HH:mm:ss-1]\"},{\"prop\":\"now_3\",\"direct\":\"IN\"," +
                "\"type\":\"STRING\",\"value\":\"$[yyyy-MM-dd HH:mm:ss-3]\"},{\"prop\":\"now_7\",\"direct\":\"IN\"," +
                "\"type\":\"STRING\",\"value\":\"$[yyyy-MM-dd HH:mm:ss-7]\"},{\"prop\":\"now_30\",\"direct\":\"IN\"," +
                "\"type\":\"STRING\",\"value\":\"$[yyyy-MM-dd HH:mm:ss-30]\"},{\"prop\":\"now_180\",\"direct\":\"IN\"," +
                "\"type\":\"STRING\",\"value\":\"$[yyyy-MM-dd HH:mm:ss-180]\"},{\"prop\":\"topic\",\"direct\":\"IN\"," +
                "\"type\":\"STRING\",\"value\":\"etl.kudu_analysis.loan.loan.1\"},{\"prop\":\"kafka_address\"," +
                "\"direct\":\"IN\",\"type\":\"STRING\",\"value\":\"10.2.0.211:9092,10.2.0.212:9092,10.2.0.214:9092\"}," +
                "{\"prop\":\"commit_size\",\"direct\":\"IN\",\"type\":\"STRING\",\"value\":\"100\"}," +
                "{\"prop\":\"suffix\",\"direct\":\"IN\",\"type\":\"STRING\",\"value\":\"214\"},{\"prop\":\"op\"," +
                "\"direct\":\"IN\",\"type\":\"STRING\",\"value\":\"false\"},{\"prop\":\"log\",\"direct\":\"IN\"," +
                "\"type\":\"STRING\",\"value\":\"true\"},{\"prop\":\"now_90\",\"direct\":\"IN\",\"type\":\"STRING\"," +
                "\"value\":\"$[yyyy-MM-dd HH:mm:ss-90]\"}],\"tasks\":[{\"id\":\"3b353b57\",\"type\":\"SHELL\"," +
                "\"name\":\"test\",\"runFlag\":\"NORMAL\",\"dependence\":{},\"maxRetryTimes\":1,\"retryInterval\":1," +
                "\"timeout\":{\"enable\":false,\"strategy\":\"\",\"interval\":null},\"taskInstancePriority\":\"MEDIUM\"," +
                "\"workerGroupId\":-1,\"workerGroup\":\"default\",\"preTasks\":[],\"params\":{\"resourceList\":[]," +
                "\"localParams\":[],\"rawScript\":\"echo '${id}'\",\"resourceId\":\"undefined\"," +
                "\"resourceName\":\"Untitled0\"},\"dataSourceAlisList\":[],\"postTasks\":[]}],\"timeout\":0," +
                "\"targetTime\":0,\"isCompleteAlarm\":false}\n";

        ObjectMapper objectMapper = new ObjectMapper();

    }
}
