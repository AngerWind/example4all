package com.tiger.innerbean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Address
 * @date 2020/12/22 18:24
 * @description
 */
@Component
@Data
public class Address {

    public static void main(String[] args) throws ParseException {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(0, new HashMap<>());
        list.add(1, null);
        list.add(2, new HashMap<>());
        list.forEach(System.out::println);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", null);
        System.out.println(jsonObject.toJSONString());

        System.out.println(System.currentTimeMillis());
    }

    Long age;

}
