package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.pojo.Course;
import hieutm.chapter4_ex1.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public long count() {
        return courseRepository.count();
    }

    @Override
    public List<Course> findAllOrderByCode() {
        return courseRepository.findAll(Sort.by("code"));
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }
}
