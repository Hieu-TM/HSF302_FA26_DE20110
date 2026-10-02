package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.pojo.Course;
import hieutm.chapter4_ex1.pojo.Student;

import java.util.List;

public interface EnrollmentService {
    List<Course> getCoursesOfStudent(String studentCode);
    List<Student> getStudentsOfCourse(String courseCode);
}
