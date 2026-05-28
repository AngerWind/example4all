package com.tiger;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ComponentScan
@Configuration
public class _14_与spring集成 {

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


    // 指定了这个属性的话, 那么会自动注入一个PersonMapper的实例到spring容器中, 然后我们就可以使用@Autowired来注入这个实例了
    // 他的原理就是在生成的类上面, 添加了一个@Component注解, 这样spring在扫描的时候就可以扫描到了
    @Mapper(componentModel = "spring", injectionStrategy = org.mapstruct.InjectionStrategy.CONSTRUCTOR)
    public interface PersonMapper {

        @Mapping(target = "personId", source = "id")
        @Mapping(target = "personName", source = "name")
        PersonDTO personToPersonDTO(Person person);


        @InheritInverseConfiguration(name = "personToPersonDTO")
        Person personDTOToPerson(PersonDTO personDTO);
    }

    @Test
    public void test() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                _14_与spring集成.class);
        PersonMapper personMapper = context.getBean(PersonMapper.class);

        assertNotNull(personMapper);

        Person person = new Person();
        person.setId("1");
        person.setName("tiger");

        PersonDTO personDTO = personMapper.personToPersonDTO(person);
        assertEquals("1", personDTO.getPersonId());
        assertEquals("tiger", personDTO.getPersonName());
    }
    
}
