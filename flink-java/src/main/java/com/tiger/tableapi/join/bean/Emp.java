package com.tiger.tableapi.join.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp {
    Integer empno;
    String ename;
    Integer deptno;
    Long ts;
}