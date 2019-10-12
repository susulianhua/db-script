package com.xquant.script.service.impl;

import com.xquant.script.pojo.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    Student getStudentBySno(String sno);

    int addStudent(Student student);

    int deleteStudent(String sno);

    int updateStudent(Student student);
}
