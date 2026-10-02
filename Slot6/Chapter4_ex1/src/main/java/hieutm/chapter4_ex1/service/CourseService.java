package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.pojo.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    long count();
    List<Course> findAllOrderByCode();
    Optional<Course> findById(Long id);
}
