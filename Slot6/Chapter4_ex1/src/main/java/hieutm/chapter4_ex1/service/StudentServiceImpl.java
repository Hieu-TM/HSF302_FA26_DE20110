package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.pojo.Student;
import hieutm.chapter4_ex1.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public List<Student> saveAll(Iterable<Student> students) {
        return studentRepository.saveAll(students);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public long count() {
        return studentRepository.count();
    }

    @Override
    public List<Student> findAllOrderByGpaDesc() {
        return studentRepository.findAll(Sort.by(Sort.Direction.DESC, "gpa"));
    }

    @Override
    public Page<Student> findPage(int pageIndex, int size, String sortField) {
        if (pageIndex < 0) {
            throw new IllegalArgumentException("pageIndex must be greater than or equal to 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }
        Sort sort = (sortField != null && !sortField.trim().isEmpty())
                ? Sort.by(Sort.Direction.ASC, sortField)
                : Sort.unsorted();
        Pageable pageable = PageRequest.of(pageIndex, size, sort);
        return studentRepository.findAll(pageable);
    }
}
