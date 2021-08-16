package com.tiger.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.IOException;

/**
 * @author tiger.shen
 * @version v1.0
 * @Title Student
 * @date 2021/5/17 14:34
 * @description
 */
@Data
public class JsonSerializeAndJsonDeserializeTest {

    @Data
    public static class Student{
        String name;
        Address address;
    }

    @Data
    public static class Address {
        @JsonSerialize(using = JsonDataSerializer.class)
        @JsonDeserialize(using = JsonDataDeserializer.class)
        String address;
    }

    public static class JsonDataSerializer extends JsonSerializer<String> {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeRawValue(value);
        }
    }

    public static class JsonDataDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return node.toString();
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        String student = "{\n" +
                "    \"name\": \"zhangsan\",\n" +
                "    \"address\": {\n" +
                "       \"address\": {\n" +
                "       \n" +
                "}\n" +
                "    }\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        Student student1 = objectMapper.readValue(student, Student.class);
        String s = objectMapper.writeValueAsString(student1);
        System.out.println(s);
    }





}


