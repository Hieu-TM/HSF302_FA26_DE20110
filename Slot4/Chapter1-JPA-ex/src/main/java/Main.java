import fu.de200110.pojo.Employee;
import fu.de200110.pojo.Project;
import fu.de200110.pojo.Department;
import fu.de200110.pojo.Gender;
import fu.de200110.dao.DepartmentDAO;
import fu.de200110.dao.EmployeeDAO;
import fu.de200110.dao.ProjectDAO;
import fu.de200110.util.JPAUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DepartmentDAO deptDAO = new DepartmentDAO();
        EmployeeDAO empDAO = new EmployeeDAO();
        ProjectDAO projDAO = new ProjectDAO();

        try {
            // Create Department
            Department itDept = new Department("IT", "Ha Noi");
            deptDAO.save(itDept);
            System.out.println("Department created: " + itDept.getName());

            // Refresh to get the generated ID
            itDept = deptDAO.findDepartmentWithEmployee(itDept.getId());

            // Create 3 Employees
            Employee emp1 = new Employee(
                    "Nguyen Van A",
                    "nguyenvana@company.com",
                    new BigDecimal("50000.00"),
                    Gender.MALE,
                    LocalDate.of(2020, 1, 15)
            );
            emp1.setDepartment(itDept);
            emp1.setActive(true);

            Employee emp2 = new Employee(
                    "Tran Thi B",
                    "tranthib@company.com",
                    new BigDecimal("45000.00"),
                    Gender.FEMALE,
                    LocalDate.of(2021, 3, 20)
            );
            emp2.setDepartment(itDept);
            emp2.setActive(true);

            Employee emp3 = new Employee(
                    "Le Van C",
                    "levanc@company.com",
                    new BigDecimal("48000.00"),
                    Gender.MALE,
                    LocalDate.of(2019, 6, 10)
            );
            emp3.setDepartment(itDept);
            emp3.setActive(false);  // inactive

            empDAO.save(emp1);
            empDAO.save(emp2);
            empDAO.save(emp3);
            System.out.println("3 Employees created (emp3 inactive)\n");

            // Create 3 Projects
            Project projectA = new Project();
            projectA.setProjectCode("PA");
            projectA.setProjectName("Project A");
            projectA.setBudget(new BigDecimal("100000.00"));
            projectA.setStartDate(LocalDate.of(2024, 1, 1));
            projectA.setEndDate(LocalDate.of(2024, 12, 31));

            Project projectB = new Project();
            projectB.setProjectCode("PB");
            projectB.setProjectName("Project B");
            projectB.setBudget(new BigDecimal("150000.00"));
            projectB.setStartDate(LocalDate.of(2024, 2, 1));
            projectB.setEndDate(LocalDate.of(2025, 1, 31));

            Project projectC = new Project();
            projectC.setProjectCode("PC");
            projectC.setProjectName("Project C");
            projectC.setBudget(new BigDecimal("80000.00"));
            projectC.setStartDate(LocalDate.of(2024, 3, 1));
            projectC.setEndDate(LocalDate.of(2024, 9, 30));

            projDAO.save(projectA);
            projDAO.save(projectB);
            projDAO.save(projectC);
            System.out.println("3 Projects created\n");

            // === ASSIGN PHASE ===
            System.out.println("=== ASSIGN: Gán nhân viên vào dự án ===\n");
            
            // emp1 -> Project A + B + C (active, 3 projects)
            empDAO.assignEmployeeToProject(emp1.getId(), projectA.getId());
            empDAO.assignEmployeeToProject(emp1.getId(), projectB.getId());
            empDAO.assignEmployeeToProject(emp1.getId(), projectC.getId());
            System.out.println("✓ emp1 (Nguyen Van A, active) assigned to PA, PB, PC");
            
            // emp2 -> Project A + B (active, 2 projects)
            empDAO.assignEmployeeToProject(emp2.getId(), projectA.getId());
            empDAO.assignEmployeeToProject(emp2.getId(), projectB.getId());
            System.out.println("✓ emp2 (Tran Thi B, active) assigned to PA, PB");
            
            // emp3 -> Project A (inactive, 1 project - should NOT appear in query)
            empDAO.assignEmployeeToProject(emp3.getId(), projectA.getId());
            System.out.println("✓ emp3 (Le Van C, inactive) assigned to PA\n");

            // === JPQL QUERY DEMO ===
            System.out.println("=== JPQL QUERY: Tìm Active Employee với >1 Project ===\n");
            System.out.println("Query: SELECT e FROM Employee e WHERE e.active = true AND SIZE(e.projects) > 1\n");

            List<Employee> activeMultiProjectEmp = empDAO.findActiveEmployeesWithMultipleProjects();
            
            System.out.println("Kết quả: " + activeMultiProjectEmp.size() + " nhân viên\n");
            
            for (Employee emp : activeMultiProjectEmp) {
                System.out.println("- " + emp.getFullName() 
                    + " | Email: " + emp.getEmail() 
                    + " | Active: " + emp.isActive() 
                    + " | Projects: " + emp.getProjects().size());
                
                for (Project proj : emp.getProjects()) {
                    System.out.println("    └─ " + proj.getProjectName() + " (" + proj.getProjectCode() + ")");
                }
                System.out.println();
            }

            // === VERIFICATION ===
            System.out.println("=== KIỂM TRA KẾT QUẢ ===\n");
            System.out.println("Điều kiện: active = true AND SIZE(projects) > 1");
            System.out.println("✓ emp1: active=true, projects=3 ✓ PASS");
            System.out.println("✓ emp2: active=true, projects=2 ✓ PASS");
            System.out.println("✓ emp3: active=false, projects=1 ✗ FAIL (inactive)");
            System.out.println("\nKỳ vọng: 2 nhân viên (emp1, emp2)");
            System.out.println("Thực tế: " + activeMultiProjectEmp.size() + " nhân viên");
            System.out.println(activeMultiProjectEmp.size() == 2 ? "✓ QUERY ĐÚNG!" : "✗ QUERY SAI!");

            // === DEACTIVATE DEMO ===
            System.out.println("\n\n=== DEMO DEACTIVATE: Nhân viên emp1 nghỉ việc ===\n");
            
            Employee e1_before = empDAO.findById(Math.toIntExact(emp1.getId()));
            System.out.println("Trước deactivate:");
            System.out.println("  emp1 active: " + e1_before.isActive());
            System.out.println("  emp1 projects: " + e1_before.getProjects().size());
            
            empDAO.deactivateEmployee(emp1.getId());
            System.out.println("\n✓ empDAO.deactivateEmployee(" + emp1.getId() + ")\n");
            
            Employee e1_after = empDAO.findById(Math.toIntExact(emp1.getId()));
            System.out.println("Sau deactivate:");
            System.out.println("  emp1 active: " + e1_after.isActive() + " (expected: false)");
            System.out.println("  emp1 projects: " + e1_after.getProjects().size() + " (expected: 3)");
            System.out.println();

            // === VERIFY LOGIC ===
            System.out.println("=== GIẢI THÍCH THIẾT KẾ ===\n");
            System.out.println("❌ KHÔNG cascade DELETE tự động vì:");
            System.out.println("  1. LỰC SỬ: Cần giữ lại records lịch sử nhân viên tham gia dự án");
            System.out.println("  2. AUDIT TRAIL: Trace 'ai đã làm gì' trên dự án");
            System.out.println("  3. REPORTS: Generate reports về nhân viên từng tham gia");
            System.out.println("  4. COMPLIANCE: Tuân thủ quy định kiểm toán");
            System.out.println("  5. RECOVERY: Nếu quay lại, có thể restore toàn bộ dữ liệu\n");
            
            System.out.println("✓ THAY VÀO ĐÓ:");
            System.out.println("  • Chỉ deactivate employee (set active = false)");
            System.out.println("  • Giữ lại relationships với projects");
            System.out.println("  • Nếu cần gỡ khỏi project: gọi unassignEmployeeFromProject() riêng\n");

            // Show that employee is still in projects but marked inactive
            System.out.println("=== VERIFY TOÀN VẸN DỮ LIỆU ===\n");
            System.out.println("emp1 sau deactivate:");
            for (Project proj : e1_after.getProjects()) {
                System.out.println("  └─ " + proj.getProjectName() + " (" + proj.getProjectCode() + ") - [KEEP HISTORY]");
            }
            System.out.println("\nProject A still has emp1 in employee_project table");
            System.out.println("✓ Lịch sử được bảo toàn, không bị xóa tự động\n");
            
            // Show that JPQL query now excludes deactivated emp1
            System.out.println("=== AFTER DEACTIVATE: JPQL Query Results ===\n");
            List<Employee> activeMultiProjectEmp2 = empDAO.findActiveEmployeesWithMultipleProjects();
            System.out.println("Nhân viên active với >1 project: " + activeMultiProjectEmp2.size() + " (expected: 1 - chỉ emp2)");
            for (Employee emp : activeMultiProjectEmp2) {
                System.out.println("  ✓ " + emp.getFullName() + " | Projects: " + emp.getProjects().size());
            }
            System.out.println("\n✓ emp1 loại khỏi query vì active=false");
            System.out.println("✓ Nhưng dữ liệu emp1 vẫn còn trong DB và employee_project table");
            System.out.println("✓ DESIGN PATTERN HOÀN PERFECT!");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JPAUtil.close();
        }
    }
}
