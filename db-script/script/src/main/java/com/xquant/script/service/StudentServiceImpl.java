package com.xquant.script.service;

import com.xquant.script.dao.StudentMapper;
import com.xquant.script.pojo.Student;
import com.xquant.script.service.impl.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("studentService")
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentMapper studentMapper;

    public StudentServiceImpl(){}

    public List<Student> getAllStudents(){ return studentMapper.getAllStudents(); }

    public Student getStudentBySno(String sno){
        return studentMapper.getStudentBySno(sno);
    }

    public int addStudent(Student student){
        return studentMapper.addStudent(student);
    }

    public int deleteStudent(String sno){
        return studentMapper.deleteStudent(sno);
    }

    public int updateStudent(Student student){
        return studentMapper.updateStudent(student);
    }
}
