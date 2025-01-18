package com.tiger.tableapi.join.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dept {
    Integer deptno;
    String dname;
    Long ts;
}