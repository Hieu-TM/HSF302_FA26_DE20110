package hieutm.chapter4_ex1.runner;

import hieutm.chapter4_ex1.pojo.Student;
import hieutm.chapter4_ex1.service.DepartmentService;
import hieutm.chapter4_ex1.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
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
        runTodo7();
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

    private void runTodo7() {
        System.out.println("===== TODO 7: Built-in sort and paginate with findAll =====");
        System.out.println("-- 7a. All students sorted by GPA desc --");
        studentService.findAllOrderByGpaDesc().forEach(
                s -> System.out.printf("%s: %.1f%n", s.getFullName(), s.getGpa())
        );

        System.out.println("-- 7b. Page 2 (size 3) sorted by fullName asc --");
        Page<Student> page = studentService.findPage(1, 3, "fullName");
        page.getContent().forEach(
                s -> System.out.printf("%s (GPA: %.1f)%n", s.getFullName(), s.getGpa())
        );
        System.out.printf("totalElements = %d, totalPages = %d, hasNext = %b%n",
                page.getTotalElements(), page.getTotalPages(), page.hasNext());
    }
}
