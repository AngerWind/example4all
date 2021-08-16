package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;


/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonRawValueTest
 * @date 2021/6/15 17:03
 * @description
 */
public class JsonRawValueTest {

    @SneakyThrows
    public static void main(String[] args) {
        RawBean bean = new RawBean("My bean", "{\"attr\":false}");
        String result = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(bean);
        System.out.println(result);
    }

    @Data
    @AllArgsConstructor
    public static class RawBean {
        public String name;

        // @JsonRawValue
        public String json;  // 添加注释， json将会原样输出， 不加的时候，json会被添加上引号
    }
}
