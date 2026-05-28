package com.tiger;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class _08_target属性的转换表达式 {

    @Data
    public static class Person {
        private String name;
    }

    @Data
    public static class PersonDTO {
        private String name;
        private Integer nameLength; // 名字的长度
    }

    @Mapper
    public interface PersonMapper {
        PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

        // 如果name为null, 那么nameLength设置为0, 否则设置为name的长度
        @Mapping(target = "nameLength", expression = "java(person.getName() == null ? 0 : person.getName().length())")
        PersonDTO personToPersonDTO(Person person);
    }


    @Test
    public void givenPersonEntitytoPersonWithExpression_whenMaps_thenCorrect() {
        Person entity = new Person();
        entity.setName("John");
        PersonDTO personDto = PersonMapper.INSTANCE.personToPersonDTO(entity);
        assertEquals(entity.getName().length(), personDto.getNameLength());

        entity.setName(null);
        personDto = PersonMapper.INSTANCE.personToPersonDTO(entity);
        assertEquals(0, personDto.getNameLength());

    }


}
