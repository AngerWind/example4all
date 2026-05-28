package com.tiger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class _04_日期和字符串的映射 {

    @Data
    public static class EmployeeDTO {
        private Date startDt;
    }


    @Data
    public static class Employee {
        private String employeeStartDt;
    }


    @Mapper
    public interface EmployeeMapper {

        public static final EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

        /**
         * 原理是通过SimpleDateFormat来进行格式化的
         * Date date = new Date();
         * SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
         * String formattedDate = sdf.format(date);
         */

        // 字符串转换为Date
        @Mapping(target="startDt", source = "employeeStartDt",
                dateFormat = "dd-MM-yyyy HH:mm:ss")
        EmployeeDTO employeeToEmployeeDTO(Employee entity);

        // Data转换为字符串
        @Mapping(target="employeeStartDt", source="startDt",
                dateFormat="dd-MM-yyyy HH:mm:ss")
        Employee employeeDTOtoEmployee(EmployeeDTO dto);
    }


}
