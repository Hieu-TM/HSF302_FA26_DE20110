package hieutm.chapter4_ex1.repository;

import hieutm.chapter4_ex1.pojo.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByStudentsIsEmpty();
}
