package com.tiger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class _03_子对象的映射 {

    @Data
    public static class EmployeeDTO {
        private int employeeId;
        private String employeeName;
        private DivisionDTO division;
    }


    @Data
    public static class Employee {
        private int id;
        private String name;
        private Division division;
    }
    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public static class Division {
        private int id;
        private String name;
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public static class DivisionDTO {
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

        // 如果在转换employee的时候, 发现了内部有Division到DivisionDTO的转换, 那么会自动调用这个方法
        DivisionDTO divisionToDivisionDTO(Division entity);

        Division divisionDTOtoDivision(DivisionDTO dto);

    }



    @Test
    public void givenEmpDTONestedMappingToEmp_whenMaps_thenCorrect() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setDivision(new DivisionDTO(1, "Division1"));
        Employee entity = EmployeeMapper.INSTANCE.employeeDTOtoEmployee(dto);
        assertEquals(dto.getDivision().getId(),
                entity.getDivision().getId());
        assertEquals(dto.getDivision().getName(),
                entity.getDivision().getName());
    }


}
