package com.tiger;

import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

public class _06_集合类型的转换 {

    @Data
    public static class EmployeeDTO {
        private double salary;
    }


    @Data
    public static class Employee {
        private double salary;
    }


    @Mapper
    public interface EmployeeMapper {

        public static final EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);


        EmployeeDTO employeeToEmployeeDTO(Employee entity);

        // mapstruct会自动的调用上面的方法, 通过for循环来进行转换, 不需要我们手写
        List<EmployeeDTO> employeeListToEmployeeDTOList(Collection<Employee> entity);

    }


}
