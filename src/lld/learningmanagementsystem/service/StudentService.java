package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Course;
import lld.learningmanagementsystem.model.Students;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    Map<Integer, Students> studentsMap;
    public StudentService() {
        this.studentsMap = new HashMap<>();
    }
    public Students createStudent(Students student){
        studentsMap.put(student.getId(),student);
        return student;
    }
    public Students getStudent(int studentMobileNo){
        return studentsMap.get(studentMobileNo);
    }

}
