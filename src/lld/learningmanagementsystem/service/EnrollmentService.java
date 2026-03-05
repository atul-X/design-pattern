package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrollmentService {
    Map<Integer, List<Integer>> studentCourseMap;  // studentId → courseIds
    Map<Integer, List<Integer>> courseStudentMap;
    CommandInvoker invoker;
    CourseService courseService;

    public EnrollmentService() {
        this(new CourseService());
    }

    public EnrollmentService(CourseService courseService) {
        this.invoker=new CommandInvoker();
        this.studentCourseMap = new HashMap<>();
        this.courseStudentMap = new HashMap<>();
        this.courseService = courseService;
    }

    public void enroll(int studentId, int courseId){
        // First validate via course state
        Course course = courseService.getCourse(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found: " + courseId);
        }
        
        // Let course state validate if enrollment is allowed
        course.enroll(studentId);  // this prints validation message
        
        Command command=new CommandImpl(
                ()->{
                    studentCourseMap.computeIfAbsent(studentId,k->new ArrayList<>()).add(courseId);
                    courseStudentMap.computeIfAbsent(courseId,k->new ArrayList<>()).add(studentId);
                },
                ()->{
                    studentCourseMap.get(studentId).remove(Integer.valueOf(courseId));
                    courseStudentMap.get(courseId).remove(Integer.valueOf(studentId));
                }
        );
        invoker.executeCommand(command);
    }
    public List<Integer> getEnrolledStudent(int studentId){
        return studentCourseMap.get(studentId);
    }

    public CommandInvoker getCommandInvoker() {
        return invoker;
    }
}
