package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Course;
import lld.learningmanagementsystem.model.Quiz;

import java.util.HashMap;
import java.util.Map;

public class CourseService {
    Map<Integer, Course> courseMap = new HashMap<>();
    private CommandInvoker commandInvoker;

    public CourseService() {
        this.courseMap = new HashMap<>();
        this.commandInvoker = new CommandInvoker();
    }

    public Course createCourse(Course course) {
        courseMap.put(course.getId(), course);
        return course;  // course starts as DraftState automatically
    }

    public Course getCourse(int courseId) {
        return courseMap.get(courseId);
    }

    // Command pattern methods for undoable operations
    public void publishCourse(int courseId) {
        Course course = courseMap.get(courseId);
        if (course != null) {
            CourseState previousState = course.getCurrentState();
            
            Command command = new CommandImpl(
                () -> course.publish(),  // Execute
                () -> {
                    // Undo: restore previous state
                    course.setCurrentState(previousState);
                    System.out.println("Undo: Course " + courseId + " restored to " + previousState.getClass().getSimpleName());
                }
            );
            
            commandInvoker.executeCommand(command);
        }
    }

    public void archiveCourse(int courseId) {
        Course course = courseMap.get(courseId);
        if (course != null) {
            CourseState previousState = course.getCurrentState();
            
            Command command = new CommandImpl(
                () -> course.archive(),  // Execute
                () -> {
                    // Undo: restore previous state
                    course.setCurrentState(previousState);
                    System.out.println("Undo: Course " + courseId + " restored to " + previousState.getClass().getSimpleName());
                }
            );
            
            commandInvoker.executeCommand(command);
        }
    }

    public void addQuizToCourse(int courseId, Quiz quiz) {
        Course course = courseMap.get(courseId);
        if (course != null) {
            Command command = new CommandImpl(
                () -> course.addQuiz(quiz),  // Execute
                () -> {
                    // Undo: remove quiz
                    course.getQuizList().remove(quiz);
                    System.out.println("Undo: Quiz " + quiz.getId() + " removed from course " + courseId);
                }
            );
            
            commandInvoker.executeCommand(command);
        }
    }

    public void enrollStudentInCourse(int courseId, int studentId) {
        Course course = courseMap.get(courseId);
        if (course != null) {
            Command command = new CommandImpl(
                () -> course.enroll(studentId),  // Execute
                () -> {
                    // Undo: unenroll student
                    course.getEnrolledStudents().remove(Integer.valueOf(studentId));
                    System.out.println("Undo: Student " + studentId + " unenrolled from course " + courseId);
                }
            );
            
            commandInvoker.executeCommand(command);
        }
    }

    // Command management methods
    public void undoLastAction() {
        commandInvoker.undo();
    }

    public void undoAllActions() {
        while (commandInvoker.hasUndoableActions()) {
            commandInvoker.undo();
        }
    }

    public int getCommandHistorySize() {
        return commandInvoker.getHistorySize();
    }

    public void clearCommandHistory() {
        commandInvoker.clearHistory();
    }

    public CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    // Legacy methods for backward compatibility (non-undoable)
    public void publishCourseDirect(int courseId) {
        Course course = courseMap.get(courseId);
        if (course != null) {
            course.publish();
        }
    }

    public void archiveCourseDirect(int courseId) {
        Course course = courseMap.get(courseId);
        if (course != null) {
            course.archive();
        }
    }
}
