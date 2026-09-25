package hieutm.chapter4_ex1.service;

import hieutm.chapter4_ex1.pojo.Department;
import hieutm.chapter4_ex1.repository.DepartmentRepository;
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
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
}
