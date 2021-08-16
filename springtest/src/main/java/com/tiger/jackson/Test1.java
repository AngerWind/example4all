package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.Map;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Test1
 * @date 2021/6/15 19:52
 * @description
 */
public class Test1 {

    static ObjectMapper objectMapper = new ObjectMapper();

    @Data
    @AllArgsConstructor
    public static class Car{
        private String color;
        private String type;

        @JsonAnySetter
        private Map<String, Object> other = Maps.newHashMap();
    }

    @Test
    public void ObjectToJson() {
        Car car = new Car("yellow", "renault", Maps.newHashMap());
        try {
            String carString = objectMapper.writeValueAsString(car);
            System.out.println(carString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void JsonToObject(){
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        try {
            Car car = objectMapper.readValue(json, Car.class);
            System.out.println(car);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void JsonToJsonNode() {
        String json = "{ \"color\" : \"Black\", \"type\" : \"FIAT\" }";
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            String color = jsonNode.get("color").asText();
            System.out.println(color);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void JsonToMap() {
        String json = "{ \"color\" : \"Black\", \"type\" : \"FIAT\" }";
        try {
            Map<String, Object> jsonNode = objectMapper.readValue(json, new TypeReference<Map<String, Object>>(){});
            Map<String, Object> jsonNode1= objectMapper.readValue(json, Map.class);
            System.out.println(jsonNode);
            System.out.println(jsonNode1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
