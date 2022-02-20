package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;

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
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(bean);
        System.out.println(result);

        // !!!!
        // @JsonRawValue只能单向工作，下面代码将导致报错
        // 双向工作可以再添加一个@JsonDeserialize自定义反序列化器
        RawBean rawBean = objectMapper.readValue(result, RawBean.class);

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RawBean {
        public String name;

        @JsonRawValue
        @JsonDeserialize(using = JsonDataDeserializer.class)
        public String json;  // 添加注释， json将会原样输出， 不加的时候，json会被添加上引号
    }

    public static class JsonDataDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return node.toString();
        }

    }
}
