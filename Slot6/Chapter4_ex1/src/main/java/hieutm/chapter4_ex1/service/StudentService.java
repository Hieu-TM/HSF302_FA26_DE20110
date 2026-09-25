package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.pojo.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    
    List<Student> findAll();
    
    Optional<Student> findById(Long id);
    
    Student save(Student student);
    
    void deleteById(Long id);
}
