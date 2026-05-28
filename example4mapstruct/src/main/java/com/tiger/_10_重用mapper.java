package com.tiger;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class _10_重用mapper {

    @Data
    public static class Person {
        private String id;
        private Address address;
    }

    @Data
    public static class PersonDTO {
        private String id;
        private AddressDTO address;
    }

    @Data
    public static class Address {
        private String id;
        private String city;
    }

    @Data
    public static class AddressDTO {
        private String id;
        private String city;
    }

    @Mapper
    public interface AddressMapper {
        AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

        AddressDTO addressToAddressDTO(Address address);
    }

    @Mapper(uses = {AddressMapper.class})
    public interface PersonMapper {
        PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

        // 正常来说, 是无法转换person中的address的, 因为这个mapper中没有address的转换规则
        // 但是因为我们在@Mapper中指定了uses = {AddressMapper.class}, 所以可以转换

        // 你也可以指定一些通用的mapper转换规则, 比如通用的Date和String转换, String和枚举转换
        // 这样就可以进行复用了
        PersonDTO personToPersonDTO(Person person);
    }


    @Test
    public void test() {
        Person person = new Person();
        person.setId("1");
        Address address = new Address();
        address.setId("1");
        address.setCity("Beijing");
        person.setAddress(address);

        PersonDTO personDto = PersonMapper.INSTANCE.personToPersonDTO(person);

        assertNotNull(personDto);
        assertNotNull(personDto.getAddress());
        assertEquals(person.getAddress().getCity(), personDto.getAddress().getCity());
    }


}
