package hieutm.chapter4_ex1.repository;

import hieutm.chapter4_ex1.pojo.Gender;
import hieutm.chapter4_ex1.pojo.Student;
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
}
