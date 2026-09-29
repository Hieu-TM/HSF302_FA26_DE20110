package hieutm.chapter4_ex1.repository;

import hieutm.chapter4_ex1.pojo.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hieutm.chapter4_ex1.dto.DepartmentStatDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByStudentsIsEmpty();

    @Query("SELECT new hieutm.chapter4_ex1.dto.DepartmentStatDTO(d.code, d.name, COUNT(s), AVG(s.gpa)) " +
           "FROM Department d LEFT JOIN d.students s " +
           "GROUP BY d.code, d.name " +
           "ORDER BY d.code ASC")
    List<DepartmentStatDTO> getStatistics();

    Optional<Department> findByCode(String code);

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.students WHERE d.code = :code")
    Optional<Department> findByCodeWithStudents(@Param("code") String code);
}
