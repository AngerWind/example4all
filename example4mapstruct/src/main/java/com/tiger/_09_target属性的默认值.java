package com.tiger;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class _09_target属性的默认值 {

    @Data
    public static class Person {
        private String id;
    }

    @Data
    public static class PersonDTO {
        private String id;
    }

    @Mapper
    public interface PersonMapper {
        PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

        // 如果person中的id为null, 那么会自动调用defaultExpression, 来生成一个默认值
        @Mapping(target = "id", source = "id",
                defaultExpression = "java(java.util.UUID.randomUUID().toString())")
        PersonDTO personToPersonDTO(Person person);
    }


    @Test
    public void givenPersonEntitytoPersonWithExpression_whenMaps_thenCorrect(){
    Person entity  = new Person();
    PersonDTO personDto = PersonMapper.INSTANCE.personToPersonDTO(entity);
    assertNotNull(personDto.getId());
}


}
