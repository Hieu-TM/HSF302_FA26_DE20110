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
        runTodo6();
    }

    private void runTodo6() {
        System.out.println("===== TODO 6: Built-in count, findById, existsById =====");
        System.out.printf("Total departments: %d, Total students: %d%n", departmentService.count(), studentService.count());

        studentService.findById(1L).ifPresentOrElse(
                s -> System.out.println("Student id=1: " + s.getFullName()),
                () -> System.out.println("Student id=1: Not found")
        );

        studentService.findById(99L).ifPresentOrElse(
                s -> System.out.println("Student id=99: " + s.getFullName()),
                () -> System.out.println("Student id=99: Not found")
        );

        System.out.println("Department id=4 exists: " + departmentService.existsById(4L));
    }
}
