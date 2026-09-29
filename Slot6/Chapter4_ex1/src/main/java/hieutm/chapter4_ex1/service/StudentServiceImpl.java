package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.dto.StudentSummary;
import hieutm.chapter4_ex1.pojo.Gender;
import hieutm.chapter4_ex1.pojo.Student;
import hieutm.chapter4_ex1.repository.StudentRepository;
import hieutm.chapter4_ex1.specification.StudentSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
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

    @Override
    public Optional<Student> findByStudentCode(String code) {
        return studentRepository.findByStudentCode(code);
    }

    @Override
    public boolean isEmailExisted(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public long countActive() {
        return studentRepository.countByActiveTrue();
    }

    @Override
    public List<Student> searchByName(String kw) {
        if (kw == null || kw.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return studentRepository.findByFullNameContainingIgnoreCase(kw.trim());
    }

    @Override
    public List<Student> findByEmailDomain(String domain) {
        if (domain == null || domain.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String cleanDomain = domain.trim();
        if (!cleanDomain.startsWith("@")) {
            cleanDomain = "@" + cleanDomain;
        }
        return studentRepository.findByEmailEndingWith(cleanDomain);
    }

    @Override
    public List<Student> findWithoutEmail() {
        return studentRepository.findByEmailIsNull();
    }

    @Override
    public List<Student> findByGpaRange(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min GPA must be less than or equal to max GPA");
        }
        return studentRepository.findByGpaBetweenOrderByGpaDesc(min, max);
    }

    @Override
    public List<Student> findActiveByGender(Gender g) {
        return studentRepository.findByGenderAndActiveTrue(g);
    }

    @Override
    public List<Student> findBornAfter(LocalDate d) {
        return studentRepository.findByDobAfter(d);
    }

    @Override
    public List<Student> findByDepartment(String deptCode) {
        return studentRepository.findByDepartment_CodeOrderByFullNameAsc(deptCode);
    }

    @Override
    public long countByDepartment(String deptCode) {
        return studentRepository.countByDepartment_Code(deptCode);
    }

    @Override
    public List<Student> findTop3ByGpa() {
        return studentRepository.findTop3ByOrderByGpaDesc();
    }

    @Override
    public List<Student> findGoodStudents(String deptCode, double minGpa) {
        return studentRepository.findGoodStudents(deptCode, minGpa);
    }

    @Override
    public List<Student> searchByKeyword(String kw) {
        if (kw == null || kw.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return studentRepository.searchByKeyword(kw.trim());
    }

    @Override
    public List<Student> findAboveAverageGpa() {
        return studentRepository.findAboveAverageGpa();
    }

    @Override
    public List<Student> findTopNInDepartment(String deptCode, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        return studentRepository.findTopNInDepartment(deptCode, n);
    }

    @Override
    public List<StudentSummary> getActiveSummaries() {
        return studentRepository.getActiveSummaries();
    }

    @Override
    public Page<Student> findActiveByDepartment(String deptCode, int pageIndex, int size) {
        if (pageIndex < 0) {
            throw new IllegalArgumentException("pageIndex must be greater than or equal to 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }
        Pageable pageable = PageRequest.of(pageIndex, size);
        return studentRepository.findActiveByDepartment(deptCode, pageable);
    }

    @Override
    public List<Student> search(String kw, String deptCode, Double minGpa, Boolean active) {
        Specification<Student> spec = Specification.where(StudentSpecs.nameContains(kw))
                .and(StudentSpecs.inDepartment(deptCode))
                .and(StudentSpecs.gpaAtLeast(minGpa))
                .and(StudentSpecs.isActive(active));
        return studentRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "fullName"));
    }

    @Override
    @Transactional
    public Student updateGpa(String code, double newGpa) {
        if (newGpa < 0 || newGpa > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0 and 4");
        }
        Student student = studentRepository.findByStudentCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with code: " + code));
        student.setGpa(newGpa);
        return student;
    }

    @Override
    @Transactional
    public int deactivateLowGpa(double threshold) {
        if (threshold < 0 || threshold > 4.0) {
            throw new IllegalArgumentException("Threshold must be between 0 and 4");
        }
        return studentRepository.deactivateLowGpa(threshold);
    }

    @Override
    @Transactional
    public long deleteInactiveStudents() {
        return studentRepository.deleteByActiveFalse();
    }
}
