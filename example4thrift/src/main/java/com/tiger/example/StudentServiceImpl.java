package com.tiger.example;

import org.apache.thrift.TException;

public class StudentServiceImpl implements StudentService.Iface {
    @Override
    public Student getStudentByIdAndName(int id) throws TException {
        return new Student();
    }

    @Override
    public void saveStudent(Student student) throws TException {
        System.out.println(student);
    }
}
