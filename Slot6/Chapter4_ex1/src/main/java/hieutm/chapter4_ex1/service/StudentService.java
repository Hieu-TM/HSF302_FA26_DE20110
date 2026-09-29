package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.pojo.Student;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    
    List<Student> findAll();
    
    Optional<Student> findById(Long id);
    
    Student save(Student student);
    
    List<Student> saveAll(Iterable<Student> students);
    
    void deleteById(Long id);

    long count();

    List<Student> findAllOrderByGpaDesc();

    Page<Student> findPage(int pageIndex, int size, String sortField);

    Optional<Student> findByStudentCode(String code);

    boolean isEmailExisted(String email);

    long countActive();

    List<Student> searchByName(String kw);

    List<Student> findByEmailDomain(String domain);

    List<Student> findWithoutEmail();
}
