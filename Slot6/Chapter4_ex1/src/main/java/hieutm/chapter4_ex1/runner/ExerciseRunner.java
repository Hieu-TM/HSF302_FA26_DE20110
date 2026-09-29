package hieutm.chapter4_ex1.runner;

import hieutm.chapter4_ex1.pojo.Gender;
import hieutm.chapter4_ex1.pojo.Student;
import hieutm.chapter4_ex1.service.DepartmentService;
import hieutm.chapter4_ex1.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
        runTodo8();
        runTodo9();
        runTodo10();
        runTodo11();
        runTodo12();
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

    private void runTodo8() {
        System.out.println("===== TODO 8: Derived query - findByStudentCode, existsByEmail, countByActiveTrue =====");
        System.out.println("-- 8a. Find student by code --");
        studentService.findByStudentCode("AI002").ifPresentOrElse(
                s -> System.out.println("Student AI002: " + s.getFullName()),
                () -> System.out.println("Student AI002: Not found")
        );
        studentService.findByStudentCode("XX999").ifPresentOrElse(
                s -> System.out.println("Student XX999: " + s.getFullName()),
                () -> System.out.println("Student XX999: Not found")
        );

        System.out.println("-- 8b. Check email exists --");
        String email = "binh.tt@fpt.edu.vn";
        System.out.printf("Email %s exists: %b%n", email, studentService.isEmailExisted(email));

        System.out.println("-- 8c. Count active students --");
        System.out.println("Active students count: " + studentService.countActive());
    }

    private void runTodo9() {
        System.out.println("===== TODO 9: Derived query - searchByName, findByEmailDomain, findWithoutEmail =====");
        System.out.println("-- 9a. FullName contains 'nguyen' (ignore case) --");
        studentService.searchByName("nguyen").forEach(s -> System.out.println(s.getFullName()));

        System.out.println("-- 9b. Email ending with '@gmail.com' --");
        studentService.findByEmailDomain("@gmail.com").forEach(s -> System.out.println(s.getFullName() + " - " + s.getEmail()));

        System.out.println("-- 9c. Students without email (null) --");
        studentService.findWithoutEmail().forEach(s -> System.out.println(s.getFullName()));
    }

    private void runTodo10() {
        System.out.println("===== TODO 10: Derived query - Between+OrderBy, And+True, After =====");
        System.out.println("-- 10a. GPA in [3.0, 3.6] sorted by GPA desc --");
        studentService.findByGpaRange(3.0, 3.6).forEach(
                s -> System.out.printf("%s: %.1f%n", s.getFullName(), s.getGpa())
        );

        System.out.println("-- 10b. Active Male students --");
        studentService.findActiveByGender(Gender.MALE).forEach(
                s -> System.out.println(s.getFullName() + " - " + s.getGender() + " - active: " + s.getActive())
        );

        System.out.println("-- 10c. Students born after 2005-01-01 --");
        studentService.findBornAfter(LocalDate.of(2005, 1, 1)).forEach(
                s -> System.out.println(s.getFullName() + " - DOB: " + s.getDob())
        );
    }

    private void runTodo11() {
        System.out.println("===== TODO 11: Derived query - Nested property, countBy, Top3, IsEmpty =====");
        System.out.println("-- 11a. Students in department 'SE' sorted by fullName asc --");
        studentService.findByDepartment("SE").forEach(
                s -> System.out.println(s.getFullName())
        );

        System.out.println("-- 11b. Count students in department 'AI' --");
        System.out.println("AI student count: " + studentService.countByDepartment("AI"));

        System.out.println("-- 11c. Top 3 students with highest GPA --");
        studentService.findTop3ByGpa().forEach(
                s -> System.out.printf("%s: %.1f%n", s.getFullName(), s.getGpa())
        );

        System.out.println("-- 11d. Departments without students --");
        departmentService.findDepartmentsWithoutStudents().forEach(
                d -> System.out.printf("%s - %s%n", d.getCode(), d.getName())
        );
    }

    private void runTodo12() {
        System.out.println("===== TODO 12: Custom query - JPQL with @Param =====");
        System.out.println("Students in 'SE' with GPA >= 3.0 sorted by GPA desc:");
        studentService.findGoodStudents("SE", 3.0).forEach(
                s -> System.out.printf("%s: %.1f%n", s.getFullName(), s.getGpa())
        );
    }
}
