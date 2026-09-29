package hieutm.chapter4_ex1.runner;

import hieutm.chapter4_ex1.service.DepartmentService;
import hieutm.chapter4_ex1.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
public class ExerciseRunner implements CommandLineRunner {

    private final DepartmentService departmentService;
    private final StudentService studentService;

    @Override
    public void run(String... args) throws Exception {
        // TODO: Exercise tasks will be implemented here
        System.out.println("ExerciseRunner started with DepartmentService and StudentService injected");
    }
}
