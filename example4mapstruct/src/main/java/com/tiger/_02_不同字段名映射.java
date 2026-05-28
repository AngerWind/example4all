package com.tiger;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class _02_不同字段名映射 {

    @Data
    public static class EmployeeDTO {
        private int employeeId;
        private String employeeName;
        // getters and setters
    }


    @Data
    public static class Employee {
        private int id;
        private String name;
    }


    @Mapper
    public interface EmployeeMapper {

        public static final EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

        // 这里通过entity.id, entity.name来映射也可以
        @Mapping(target = "employeeId", source = "id")
        @Mapping(target = "employeeName", source = "name")
        EmployeeDTO employeeToEmployeeDTO(Employee entity);

        @Mapping(target = "id", source = "employeeId")
        @Mapping(target = "name", source = "employeeName")
        Employee employeeDTOtoEmployee(EmployeeDTO dto);
    }



    @Test
    public void givenEmployeeDTOwithDiffNametoEmployee_whenMaps_thenCorrect() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(1);
        dto.setEmployeeName("John");

        Employee entity = EmployeeMapper.INSTANCE.employeeDTOtoEmployee(dto);

        assertEquals(dto.getEmployeeId(), entity.getId());
        assertEquals(dto.getEmployeeName(), entity.getName());
    }

}
