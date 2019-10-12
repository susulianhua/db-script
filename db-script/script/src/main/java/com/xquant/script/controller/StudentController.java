package com.xquant.script.controller;

import com.xquant.script.pojo.NormalResponse;
import com.xquant.script.pojo.Student;
import com.xquant.script.service.impl.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @RequestMapping("/module")
    @ResponseBody
    public NormalResponse module(){
        ArrayList<String> modules = new ArrayList<String>();
        File file = new File("resources//");
        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith("table.xml")) return true;
                return false;
            }
        });
        return new NormalResponse();
    }

    @RequestMapping("/getstudent")
    @ResponseBody
    public List<Student> getStudents(){
        List<Student> students = studentService.getAllStudents();
        return students;
    }

    @RequestMapping("/add")
    @ResponseBody
    public int addStudnet(HttpServletRequest request){
        String sno = request.getParameter("sno");
        String sname = request.getParameter("sname");
        String ssex = request.getParameter("ssex");
        String spwd = request.getParameter("spwd");
        String stel = request.getParameter("stel");
        String sage = request.getParameter("sage");
        int sage1 = Integer.parseInt(sage);
        Student student = new Student();
        student.setSage(sage1);
        student.setSno(sno);
        student.setSname(sname);
        student.setSpwd(spwd);
        student.setStel(stel);
        student.setSsex(ssex);
        return studentService.addStudent(student);
    }

    @RequestMapping("/select")
    @ResponseBody
    public Student getStudnetsById(HttpServletRequest request){
        String sno = request.getParameter("studentNum");
        Student student = studentService.getStudentBySno(sno);
        return student;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public int deleteStudent(HttpServletRequest request){
        String sno = request.getParameter("sno");
        return studentService.deleteStudent(sno);
    }
}
