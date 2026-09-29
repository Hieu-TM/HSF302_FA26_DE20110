package hieutm.chapter4_ex1.runner;

import hieutm.chapter4_ex1.pojo.Department;
import hieutm.chapter4_ex1.pojo.Gender;
import hieutm.chapter4_ex1.pojo.Student;
import hieutm.chapter4_ex1.service.DepartmentService;
import hieutm.chapter4_ex1.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DepartmentService departmentService;
    private final StudentService studentService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeDepartments();
        initializeStudents();
    }

    private void initializeDepartments() {
        List<Department> departments = new ArrayList<>();
        
        Department se = new Department();
        se.setCode("SE");
        se.setName("Software Engineering");
        departments.add(se);
        
        Department ai = new Department();
        ai.setCode("AI");
        ai.setName("Artificial Intelligence");
        departments.add(ai);
        
        Department ia = new Department();
        ia.setCode("IA");
        ia.setName("Information Assurance");
        departments.add(ia);
        
        Department gd = new Department();
        gd.setCode("GD");
        gd.setName("Graphic Design");
        departments.add(gd);
        
        departmentService.saveAll(departments);
    }

    private void initializeStudents() {
        List<Department> departments = departmentService.findAll();
        Department se = departments.stream().filter(d -> d.getCode().equals("SE")).findFirst().orElse(null);
        Department ai = departments.stream().filter(d -> d.getCode().equals("AI")).findFirst().orElse(null);
        Department ia = departments.stream().filter(d -> d.getCode().equals("IA")).findFirst().orElse(null);
        
        List<Student> students = new ArrayList<>();
        
        // SE001
        Student s1 = new Student();
        s1.setStudentCode("SE001");
        s1.setFullName("Nguyen Van An");
        s1.setEmail("an.nv@fpt.edu.vn");
        s1.setGender(Gender.MALE);
        s1.setDob(LocalDate.of(2005, 3, 15));
        s1.setGpa(3.2);
        s1.setActive(true);
        se.addStudent(s1);
        students.add(s1);
        
        // SE002
        Student s2 = new Student();
        s2.setStudentCode("SE002");
        s2.setFullName("Tran Thi Binh");
        s2.setEmail("binh.tt@fpt.edu.vn");
        s2.setGender(Gender.FEMALE);
        s2.setDob(LocalDate.of(2004, 7, 22));
        s2.setGpa(3.8);
        s2.setActive(true);
        se.addStudent(s2);
        students.add(s2);
        
        // SE003
        Student s3 = new Student();
        s3.setStudentCode("SE003");
        s3.setFullName("Le Van Cuong");
        s3.setEmail("cuong.lv@fpt.edu.vn");
        s3.setGender(Gender.MALE);
        s3.setDob(LocalDate.of(2003, 11, 5));
        s3.setGpa(2.5);
        s3.setActive(false);
        se.addStudent(s3);
        students.add(s3);
        
        // AI001
        Student s4 = new Student();
        s4.setStudentCode("AI001");
        s4.setFullName("Pham Thi Dung");
        s4.setEmail("dung.pt@fpt.edu.vn");
        s4.setGender(Gender.FEMALE);
        s4.setDob(LocalDate.of(2006, 1, 10));
        s4.setGpa(3.5);
        s4.setActive(true);
        ai.addStudent(s4);
        students.add(s4);
        
        // AI002
        Student s5 = new Student();
        s5.setStudentCode("AI002");
        s5.setFullName("Hoang Van Em");
        s5.setEmail("em.hv@gmail.com");
        s5.setGender(Gender.MALE);
        s5.setDob(LocalDate.of(2002, 9, 30));
        s5.setGpa(2.8);
        s5.setActive(true);
        ai.addStudent(s5);
        students.add(s5);
        
        // AI003
        Student s6 = new Student();
        s6.setStudentCode("AI003");
        s6.setFullName("Vo Thi Hoa");
        s6.setEmail("hoa.vt@fpt.edu.vn");
        s6.setGender(Gender.FEMALE);
        s6.setDob(LocalDate.of(2005, 5, 18));
        s6.setGpa(3.9);
        s6.setActive(true);
        ai.addStudent(s6);
        students.add(s6);
        
        // IA001
        Student s7 = new Student();
        s7.setStudentCode("IA001");
        s7.setFullName("Dang Van Giang");
        s7.setEmail("giang.dv@gmail.com");
        s7.setGender(Gender.MALE);
        s7.setDob(LocalDate.of(2001, 12, 1));
        s7.setGpa(1.9);
        s7.setActive(false);
        ia.addStudent(s7);
        students.add(s7);
        
        // IA002
        Student s8 = new Student();
        s8.setStudentCode("IA002");
        s8.setFullName("Bui Thi Lan");
        s8.setEmail("lan.bt@fpt.edu.vn");
        s8.setGender(Gender.FEMALE);
        s8.setDob(LocalDate.of(2004, 2, 14));
        s8.setGpa(3.1);
        s8.setActive(true);
        ia.addStudent(s8);
        students.add(s8);
        
        // SE004
        Student s9 = new Student();
        s9.setStudentCode("SE004");
        s9.setFullName("Nguyen Thi Mai");
        s9.setEmail("mai.nt@fpt.edu.vn");
        s9.setGender(Gender.FEMALE);
        s9.setDob(LocalDate.of(2003, 8, 8));
        s9.setGpa(3.6);
        s9.setActive(true);
        se.addStudent(s9);
        students.add(s9);
        
        // IA003
        Student s10 = new Student();
        s10.setStudentCode("IA003");
        s10.setFullName("Do Van Nam");
        s10.setEmail(null);
        s10.setGender(Gender.MALE);
        s10.setDob(LocalDate.of(2005, 10, 20));
        s10.setGpa(2.2);
        s10.setActive(true);
        ia.addStudent(s10);
        students.add(s10);
        
        studentService.saveAll(students);
    }
}
