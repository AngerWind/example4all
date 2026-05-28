package com.tiger;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class _11_忽略字段 {

    @Data
    public static class Person {
        private String id;
        private String name;
    }

    @Data
    public static class PersonDTO {
        private String id;
        private String name;
    }


    @Mapper
    public interface PersonMapper {
        PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);


        // @Mapping(target = "name", ignore = true) // 忽略单个属性

        @BeanMapping(ignoreByDefault = true) // 忽略所有的属性, 然后需要通过@Mapping来指定需要转换的属性
        // @Mapping(target = "id", source = "id")
        PersonDTO personToPersonDTO(Person person);
    }


    @Test
    public void test() {
        Person person = new Person();
        person.setId("1");
        person.setName("John");
        PersonDTO personDto = PersonMapper.INSTANCE.personToPersonDTO(person);
        assertEquals(person.getId(), personDto.getId());
        assertNull(personDto.getName());
    }


}
