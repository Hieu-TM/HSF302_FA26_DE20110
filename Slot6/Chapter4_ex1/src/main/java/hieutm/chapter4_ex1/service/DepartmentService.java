package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.pojo.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    
    List<Department> findAll();
    
    Optional<Department> findById(Long id);
    
    Department save(Department department);
    
    void deleteById(Long id);
}
