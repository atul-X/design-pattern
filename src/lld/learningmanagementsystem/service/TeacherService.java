package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Course;
import lld.learningmanagementsystem.model.Teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherService {
    Map<Integer, Teacher> teacherMap;
    Map<Integer, List<Integer>> courseMap;
    CourseService courseService;
    public TeacherService(){
        this.teacherMap=new HashMap<>();
        courseMap=new HashMap<>();
        courseService=new CourseService();
    }

    public Teacher createTeacher(Teacher teacher){
        teacherMap.put(teacher.getId(),teacher);
        return teacher;
    }

    public Teacher getTeacher(int teacherId){
        return teacherMap.get(teacherId);
    }

    public void assignCourseToTeacher(int teacherId, Course course){
        Teacher teacher=teacherMap.get(teacherId);
        teacher.getTeacherCourses().add(course);
        List<Integer> courseList=courseMap.getOrDefault(
                        teacherId,new ArrayList<>());
        courseList.add(course.getId());
        courseMap.put(teacherId,courseList);
    }

    public List<Course> getCourseByTeacher(int teacherId){
        List<Integer> courses= courseMap.get(teacherId);
        List<Course> courseList=new ArrayList<>();
        for(Integer courseId:courses){
            courseList.add(courseService.getCourse(courseId));
        }
        return courseList;
    }
}
