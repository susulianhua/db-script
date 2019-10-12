package com.xquant.script.dao;

import com.xquant.script.pojo.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMapper {
    List<Student> getStudent();

    List<Student> getAllStudents();

    Student getStudentBySno(@Param("sno") String sno);

    int addStudent(Student student);

    int deleteStudent(@Param("sno") String sno);

    int updateStudent(Student student);
}
