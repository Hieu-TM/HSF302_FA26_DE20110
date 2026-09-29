package hieutm.chapter4_ex1.repository;

import hieutm.chapter4_ex1.dto.StudentSummary;
import hieutm.chapter4_ex1.pojo.Gender;
import hieutm.chapter4_ex1.pojo.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    Optional<Student> findByStudentCode(String studentCode);

    boolean existsByEmail(String email);

    long countByActiveTrue();

    List<Student> findByFullNameContainingIgnoreCase(String keyword);

    List<Student> findByEmailEndingWith(String suffix);

    List<Student> findByEmailIsNull();

    List<Student> findByGpaBetweenOrderByGpaDesc(Double min, Double max);

    List<Student> findByGenderAndActiveTrue(Gender gender);

    List<Student> findByDobAfter(LocalDate dob);

    List<Student> findByDepartment_CodeOrderByFullNameAsc(String deptCode);

    long countByDepartment_Code(String deptCode);

    List<Student> findTop3ByOrderByGpaDesc();

    @Query("SELECT s FROM Student s WHERE s.department.code = :deptCode AND s.gpa >= :minGpa ORDER BY s.gpa DESC")
    List<Student> findGoodStudents(@Param("deptCode") String deptCode, @Param("minGpa") double minGpa);

    @Query("SELECT s FROM Student s WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :kw, '%')) OR (s.email IS NOT NULL AND LOWER(s.email) LIKE LOWER(CONCAT('%', :kw, '%'))) ORDER BY s.fullName ASC")
    List<Student> searchByKeyword(@Param("kw") String kw);

    @Query("SELECT s FROM Student s WHERE s.gpa > (SELECT AVG(s2.gpa) FROM Student s2) ORDER BY s.gpa DESC")
    List<Student> findAboveAverageGpa();

    @Query(value = "SELECT TOP (:n) s.* FROM students s JOIN departments d ON s.department_id = d.id WHERE d.code = :deptCode ORDER BY s.gpa DESC", nativeQuery = true)
    List<Student> findTopNInDepartment(@Param("deptCode") String deptCode, @Param("n") int n);

    @Query("SELECT s.studentCode AS studentCode, s.fullName AS fullName, s.gpa AS gpa, s.department.name AS departmentName " +
           "FROM Student s WHERE s.active = true ORDER BY s.fullName ASC")
    List<StudentSummary> getActiveSummaries();

    @Query("SELECT s FROM Student s WHERE s.department.code = :deptCode AND s.active = true ORDER BY s.gpa DESC")
    Page<Student> findActiveByDepartment(@Param("deptCode") String deptCode, Pageable pageable);
}
