package hieutm.chapter4_ex1.repository;

import hieutm.chapter4_ex1.pojo.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
