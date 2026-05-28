package com.tiger;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class _12_继承映射规则 {

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

        // 通过person来更新personDTO, 我们可以通过@InheritConfiguration来重用上面的映射规则
        // 但是更新的时候, 不要更新id, 只更新name
        @InheritConfiguration(name = "personToPersonDTO") // 继承personToPersonDTO的映射规则
        @Mapping(target = "personId", source = "id", ignore = true)
        void updateDtoFromPerson(Person person, @MappingTarget PersonDTO personDTO);
    }


}
