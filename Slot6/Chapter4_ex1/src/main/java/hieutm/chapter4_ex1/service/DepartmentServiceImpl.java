package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.dto.DepartmentStatDTO;
import hieutm.chapter4_ex1.pojo.Department;
import hieutm.chapter4_ex1.repository.DepartmentRepository;
import hieutm.chapter4_ex1.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    @Transactional
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public List<Department> saveAll(Iterable<Department> departments) {
        return departmentRepository.saveAll(departments);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public long count() {
        return departmentRepository.count();
    }

    @Override
    public boolean existsById(Long id) {
        return departmentRepository.existsById(id);
    }

    @Override
    public List<Department> findDepartmentsWithoutStudents() {
        return departmentRepository.findByStudentsIsEmpty();
    }

    @Override
    public List<DepartmentStatDTO> getStatistics() {
        return departmentRepository.getStatistics();
    }

    @Override
    public Optional<Department> findByCode(String code) {
        return departmentRepository.findByCode(code);
    }

    @Override
    public Department getWithStudents(String code) {
        return departmentRepository.findByCodeWithStudents(code)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with code: " + code));
    }

    @Override
    @Transactional
    public int transferStudentsAndDelete(String fromCode, String toCode) {
        if (fromCode == null || toCode == null || fromCode.trim().equalsIgnoreCase(toCode.trim())) {
            throw new IllegalArgumentException("From and to department codes must be valid, non-null, and different");
        }
        Department fromDept = departmentRepository.findByCode(fromCode.trim())
                .orElseThrow(() -> new IllegalArgumentException("Department not found with code: " + fromCode));
        Department toDept = departmentRepository.findByCode(toCode.trim())
                .orElseThrow(() -> new IllegalArgumentException("Department not found with code: " + toCode));

        int transferred = studentRepository.transferStudents(fromDept, toDept);
        departmentRepository.deleteById(fromDept.getId());
        return transferred;
    }
}
