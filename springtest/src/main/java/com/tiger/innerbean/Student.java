package com.tiger.innerbean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Student
 * @date 2020/12/22 18:24
 * @description
 */
public class Student {

    public Address address;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    public Date birth;

    public static void main(String[] args) {
        Date end = new Date();
        Date d1 = new Date(1,0,9,0,0,0);
        System.out.println(d1);
        System.out.println(new Date(d1.getTime() - 6*24*3600*1000));
        System.out.println();
        Date start = new Date(System.currentTimeMillis() - 6*24*3600*1000);
        System.out.println(end);
        System.out.println(start);

        System.out.println("-----------");
        long longTime = System.currentTimeMillis();
        Date predictedTimeStart = new Date(longTime - (longTime % (1000 * 3600)));
        Date predictedTimeEnd = new Date(predictedTimeStart.getTime() + (24 * 3600* 3600 -1) * 1000L);
        System.out.println(predictedTimeStart);
        System.out.println(predictedTimeEnd);
    }
}
