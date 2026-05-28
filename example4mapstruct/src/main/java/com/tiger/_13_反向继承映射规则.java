package com.tiger;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertNull;

public class _13_反向继承映射规则 {

    @Data
    public static class Person {
        private String id;
        private String name;
    }

    @Data
    public static class PersonDTO {
        private String personId;
        private String personName;
    }


    @Mapper
    public interface PersonMapper {
        PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

        @Mapping(target = "personId", source = "id")
        @Mapping(target = "personName", source = "name")
        PersonDTO personToPersonDTO(Person person);

        // 上面我们已经指定了personToPersonDTO的映射规则
        // 所以我们可以重用这个规则, 来指定personDTOToPerson的映射规则
        // name是需要重用的映射规则的函数的名称
        @InheritInverseConfiguration(name = "personToPersonDTO")
        Person personDTOToPerson(PersonDTO personDTO);
    }

}
