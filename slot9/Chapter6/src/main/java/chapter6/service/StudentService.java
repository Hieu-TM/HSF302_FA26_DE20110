package chapter6.service;

import chapter6.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    List<Student> findAll();

    Optional<Student> findById(Long id);

    Student create(Student student);

    boolean update(Long id, Student data);

    boolean delete(Long id);

    boolean isEmailTaken(String email, Long excludeId);

    List<String> getMajors();
}