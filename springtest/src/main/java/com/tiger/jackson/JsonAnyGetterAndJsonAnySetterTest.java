package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Map;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title JsonAnyGetterAndJsonAnySetterTest
 * @date 2021/6/18 18:27
 * @description
 */
public class JsonAnyGetterAndJsonAnySetterTest {

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @JsonPropertyOrder(alphabetic = true)
    public static class ExtendableBean {
        private String name;

        @JsonAnySetter
        private Map<String, String> properties = Maps.newHashMap();

        @JsonAnyGetter
        public Map<String, String> getProperties() {
            return properties;
        }

        // @JsonAnySetter, 标注在字段上或者方法上都可以
        public void setProp(String key, String value) {
            this.properties.put(key, value);
        }

        public ExtendableBean(String name) {
            this.name = name;
        }
    }

    @SneakyThrows
    @Test
    public void serialized (){
        ExtendableBean extendableBean = new ExtendableBean("zhangsan");
        extendableBean.setProp("key1", "value1");
        extendableBean.setProp("key2", "value2");
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(extendableBean));
    }

    @SneakyThrows
    @Test
    public void deserialized(){
        String json = "{\n" +
                "  \"name\" : \"zhangsan\",\n" +
                "  \"key1\" : \"value1\",\n" +
                "  \"key2\" : \"value2\"\n" +
                "}";
        ExtendableBean extendableBean = new ObjectMapper().readValue(json, ExtendableBean.class);
        System.out.println(extendableBean);
    }
}
