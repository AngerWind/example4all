package com.tiger.dynamic_proxy.cglib.dao;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/1/4
 * @description
 */
public interface StudentMapper {

    void selectStudentById(Long id);

    default void printStudent(Long id){
        System.out.println("print student method");
    }

}
