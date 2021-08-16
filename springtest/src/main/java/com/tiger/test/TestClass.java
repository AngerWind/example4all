package com.tiger.test;

import com.alibaba.fastjson.JSONObject;
import com.tiger.innerbean.Address;
import com.tiger.springtest.pojo.CountOnDate;
import com.tiger.test1.Student1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title TestClass
 * @date 2021/1/26 14:04
 * @description
 */
public class TestClass {

    public static void main(String[] args) {
        // boolean flag = checkIp("127.0.*.0", "127.1.0.0");
        // System.out.println(flag);
        // Date date = new Date(1610380800 * 1000L);
        // System.out.println(date);
        // List<CountOnDate> countOnDates = new ArrayList<>();
        // countOnDates.add(new CountOnDate(16L, 15L));
        // countOnDates.add(new CountOnDate(348L, 29833L));
        //
        // // Address address = new Address();
        // // address.setAge(18L);
        // // JSONObject jsonObject = new JSONObject();
        // // jsonObject.put("total", Long.valueOf("5"));
        // // jsonObject.put("address", (Object) address);
        // // jsonObject.put("countOnDate", countOnDates);
        // // System.out.println(jsonObject.toJSONString());
        //
        // fori(null);

        Logger logger = LoggerFactory.getLogger(Student1.class);
        try {
            throw new IOException("error");
        } catch (Exception e) {
            logger.error("taskId: {}, hahah:{}, error:{}", 123, "world", e);
            // System.out.println(e.toString());
        }



    }

    public static boolean checkIp(String ip, String host) {
        String[] hostSegements = host.split("\\.");
        String[] ipSegments = ip.split("\\.");

        if (hostSegements.length != ipSegments.length) {
            return false;
        }
        for (int i = 0; i < ipSegments.length; i++) {
            if (!ipSegments[i].equals(hostSegements[i]) && !"*".equals(ipSegments[i])) {
                return false;
            }
        }
        return true;
    }

    public static void fori(String... args) {
        for (String arg : args) {
            System.out.println(arg);
        }
    }
}
