package com.tiger;

import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Date;

public class _05_数字和字符串的映射 {

    @Data
    public static class EmployeeDTO {
        private double salary;
    }


    @Data
    public static class Employee {
        private String salaryStr;
    }


    @Mapper
    public interface EmployeeMapper {

        public static final EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

        /**
         * 原理是通过DecimalFormat来进行格式化的
         * double salary = 1234.567;
         * DecimalFormat df = new DecimalFormat("#.00");
         * String formattedSalary = df.format(salary);
         */

        // 字符串转换为Date
        @Mapping(target = "salary", source = "salaryStr", numberFormat = "#.00")
        EmployeeDTO employeeToEmployeeDTO(Employee entity);

        // Data转换为字符串
        @Mapping(target = "salaryStr", source = "salary", numberFormat = "#.00")
        Employee employeeDTOtoEmployee(EmployeeDTO dto);

    }


}
