package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.dto.CourseEnrollmentCount;
import hieutm.chapter4_ex1.dto.CourseStatDTO;
import hieutm.chapter4_ex1.pojo.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    long count();
    List<Course> findAllOrderByCode();
    Optional<Course> findById(Long id);

    Optional<Course> findByCode(String code);
    List<Course> findBySemester(String semester);
    long countBySemester(String semester);

    List<Course> findCoursesOfStudent(String studentCode);
    List<Course> findCoursesOfDepartment(String deptCode, boolean distinct);

    List<Course> findCoursesWithoutStudents();

    List<CourseStatDTO> getStatistics();

    List<Course> findFullCourses();

    Course getWithStudents(String code);

    List<CourseEnrollmentCount> findTopEnrolled(int n);

    void deleteCourseDirectly(String code);   // cách SAI — để quan sát lỗi
    int deleteCourse(String code);            // cách ĐÚNG
}
