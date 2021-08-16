package com.tiger.test1;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Student2
 * @date 2021/3/8 18:41
 * @description
 */
@Data
public class Student2 {

    private String seconds;

    public static void main(String[] args) {
        // Student2 student2 = new Student2();
        // student2.setAge(18.5);
        // student2.setName("name");
        //
        // Student1 student1 = new Student1();
        // BeanUtils.copyProperties(student2, student1);
        // System.out.println(student1.getAge() + " " + student1.getName());
        // JSONArray objects = new JSONArray();
        // objects.add(new Student1("1天21小时21分钟55.99秒"));
        // List<Student1> student2s = objects.toJavaList(Student1.class);
        // System.out.println(student2s);

        // JSONArray objects1 = JSONArray.parseArray("[{\"seconds\": \"1天21小时21分钟55.99秒\"}]");
        // List<Student2> student2s = objects1.toJavaList(Student2.class);
        // System.out.println(student2s);

        // path example /scheduler/welab-skyscanner-tenma/nodes/worker/default/10.2.24.63:25551
        String path = "/scheduler/welab-skyscanner-tenma/nodes/worker/default/10.2.24.63:25551";
        String[] info = path.split("/");
        String host = info[info.length - 1];
        System.out.println(host);


    }

    public void setSeconds(double s) {
        StringBuilder sb = new StringBuilder();
        int se = (int)s;
        int days = se / ( 60 * 60 * 24);
        int hours = (se % (60 * 60 * 24)) / (60 * 60);
        int minutes = (se % (60 * 60)) / 60;
        double seconds = Double.parseDouble(new BigDecimal(String.valueOf(s)).remainder(BigDecimal.valueOf(60)).toString());

        if (days > 0) {
            sb.append(days).append("天");
        }
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        if (seconds > 0) {
            sb.append(seconds).append("秒");
        }
        this.seconds = sb.toString();
    }
    public void setSeconds() {

    }
}
