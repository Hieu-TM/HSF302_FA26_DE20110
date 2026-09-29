package hieutm.chapter4_ex1.repository;

import hieutm.chapter4_ex1.pojo.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hieutm.chapter4_ex1.dto.DepartmentStatDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByStudentsIsEmpty();

    @Query("SELECT new hieutm.chapter4_ex1.dto.DepartmentStatDTO(d.code, d.name, COUNT(s), AVG(s.gpa)) " +
           "FROM Department d LEFT JOIN d.students s " +
           "GROUP BY d.code, d.name " +
           "ORDER BY d.code ASC")
    List<DepartmentStatDTO> getStatistics();
}
