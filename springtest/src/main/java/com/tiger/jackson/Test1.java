package com.tiger.jackson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
    @NoArgsConstructor
    public static class Car {
        private String color;
        private String type;

        @JsonSerialize(using = LocalDataTimeSerializer.class)
        @JsonDeserialize(using = LocalDataTimeDerializer.class)
        private LocalDateTime time;

        // @JsonAnySetter
        private Map<String, Object> other = Maps.newHashMap();
    }

    @Test
    public void ObjectToJson() {
        // Car car = new Car("yellow", "renault", Maps.newHashMap());
        // try {
        //     String carString = objectMapper.writeValueAsString(car);
        //     System.out.println(carString);
        // } catch (JsonProcessingException e) {
        //     e.printStackTrace();
        // }
    }

    @Test
    public void JsonToObject() {
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
            Map<String, Object> jsonNode = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> jsonNode1 = objectMapper.readValue(json, Map.class);
            System.out.println(jsonNode);
            System.out.println(jsonNode1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    @SneakyThrows
    public void xmlToJson() {
        String xml = "<data>\n" + " <step>\n" + " <line/>\n" + " <line/>\n" + " </step>\n" + " <step>\n"
            + " <line>1820</line>\n" + " <line>10100</line>\n" + " </step>\n" + "</data>";
        XmlMapper xmlMapper = new XmlMapper();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = xmlMapper.readTree(xml.getBytes());
        System.out.println(objectMapper.writeValueAsString(jsonNode));

    }

    @Test
    @SneakyThrows
    public void jsonFormat(){
        Car car = new Car("red", "skds", LocalDateTime.now(), Maps.newHashMap());
        String string = objectMapper.writeValueAsString(car);
        System.out.println(string);


        Car value = objectMapper.readValue(string, Car.class);
        System.out.println(value);
    }

    public static class  LocalDataTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
            LocalDateTimeSerializer serializer = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            serializer.serialize(value, gen, serializers);
        }
    }

    public static class  LocalDataTimeDerializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
            LocalDateTimeDeserializer deserializer =
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return deserializer.deserialize(p, ctxt);
        }
    }

    @Test
    @SneakyThrows
    public void stringToLong(){
//        Age age = new Age();
//        age.age = 1L;
//        System.out.println(new ObjectMapper().writeValueAsString(age));

        String ageStr = "{\"age\":1}";
        Age age1 = new ObjectMapper().readValue(ageStr, Age.class);
        System.out.println(age1.age);
    }
    public static class Age{
        public String age;
    }

}
