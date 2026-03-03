package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Course;
import lld.learningmanagementsystem.model.Quiz;

import java.util.HashMap;
import java.util.Map;

public class CourseService {
    Map<Integer, Course> courseMap = new HashMap<>();

    public CourseService() {
        this.courseMap = new HashMap<>();
    }

    public Course createCourse(Course course) {
        courseMap.put(course.getId(), course);
        return course;  // course starts as DraftState automatically
    }

    public Course getCourse(int courseId) {
        return courseMap.get(courseId);
    }

    // State-aware methods
    public void publishCourse(int courseId) {
        Course course = courseMap.get(courseId);
        if (course != null) {
            course.publish();  // state handles transition
        }
    }

    public void archiveCourse(int courseId) {
        Course course = courseMap.get(courseId);
        if (course != null) {
            course.archive();  // state handles transition
        }
    }

    public void addQuizToCourse(int courseId, Quiz quiz) {
        Course course = courseMap.get(courseId);
        if (course != null) {
            course.addQuiz(quiz);  // state guards this action
        }
    }

    public void enrollStudentInCourse(int courseId, int studentId) {
        Course course = courseMap.get(courseId);
        if (course != null) {
            course.enroll(studentId);  // state guards this action
        }
    }
}
