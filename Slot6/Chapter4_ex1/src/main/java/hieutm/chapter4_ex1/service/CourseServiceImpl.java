package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.dto.CourseEnrollmentCount;
import hieutm.chapter4_ex1.dto.CourseStatDTO;
import hieutm.chapter4_ex1.pojo.Course;
import hieutm.chapter4_ex1.pojo.Student;
import hieutm.chapter4_ex1.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public long count() {
        return courseRepository.count();
    }

    @Override
    public List<Course> findAllOrderByCode() {
        return courseRepository.findAll(Sort.by("code"));
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCode(code);
    }

    @Override
    public List<Course> findBySemester(String semester) {
        return courseRepository.findBySemesterOrderByCodeAsc(semester);
    }

    @Override
    public long countBySemester(String semester) {
        return courseRepository.countBySemester(semester);
    }

    @Override
    public List<Course> findCoursesOfStudent(String studentCode) {
        return courseRepository.findByStudents_StudentCodeOrderByCodeAsc(studentCode);
    }

    @Override
    public List<Course> findCoursesOfDepartment(String deptCode, boolean distinct) {
        return distinct
                ? courseRepository.findDistinctByStudents_Department_CodeOrderByCodeAsc(deptCode)
                : courseRepository.findByStudents_Department_CodeOrderByCodeAsc(deptCode);
    }

    @Override
    public List<Course> findCoursesWithoutStudents() {
        return courseRepository.findByStudentsIsEmpty();
    }

    @Override
    public List<CourseStatDTO> getStatistics() {
        return courseRepository.getCourseStats();
    }

    @Override
    public List<Course> findFullCourses() {
        return courseRepository.findFullCourses();
    }

    @Override
    public Course getWithStudents(String code) {
        return courseRepository.findWithStudentsByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + code));
    }

    @Override
    public List<CourseEnrollmentCount> findTopEnrolled(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        }
        return courseRepository.findTopEnrolledNative(n);
    }

    @Override
    @Transactional
    public void deleteCourseDirectly(String code) {
        Course c = getCourse(code);
        courseRepository.delete(c);
        courseRepository.flush();          // ép Hibernate chạy DELETE ngay để thấy lỗi
    }

    @Override
    @Transactional
    public int deleteCourse(String code) {
        Course c = getCourse(code);
        // copy ra Set mới: unenroll() sẽ sửa c.getStudents() → tránh ConcurrentModificationException
        Set<Student> students = new HashSet<>(c.getStudents());
        students.forEach(s -> s.unenroll(c));   // gỡ từ OWNING side → DELETE các dòng student_courses
        courseRepository.delete(c);             // sau đó mới DELETE courses
        return students.size();
    }

    private Course getCourse(String code) {
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + code));
    }
}
