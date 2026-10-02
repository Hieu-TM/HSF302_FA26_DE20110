package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.dto.EnrollmentView;
import hieutm.chapter4_ex1.dto.StudentCreditDTO;
import hieutm.chapter4_ex1.pojo.Course;
import hieutm.chapter4_ex1.pojo.Student;

import java.util.List;

public interface EnrollmentService {
    List<Course> getCoursesOfStudent(String studentCode);
    List<Student> getStudentsOfCourse(String courseCode);

    List<Student> findStudentsInCourse(String courseCode);
    long countStudentsInCourse(String courseCode);
    List<Student> findActiveStudentsInCourse(String courseCode);

    List<Student> findStudentsWithoutCourses();
    boolean isEnrolled(String studentCode, String courseCode);

    List<Student> findGoodStudentsInCourse(String courseCode, double minGpa);

    List<StudentCreditDTO> getCreditSummary(int minCredits);

    List<Student> findStudentsWithMoreThan(int n);

    Student getStudentWithCourses(String studentCode);

    List<EnrollmentView> getEnrollmentsOfDepartment(String deptCode);
}
