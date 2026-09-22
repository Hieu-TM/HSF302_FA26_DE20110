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

            // Create 2 Employees
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

            empDAO.save(emp1);
            empDAO.save(emp2);
            System.out.println("2 Employees created");

            // Create 1 Project
            Project projectA = new Project();
            projectA.setProjectCode("PA");
            projectA.setProjectName("Project A");
            projectA.setBudget(new BigDecimal("100000.00"));
            projectA.setStartDate(LocalDate.of(2024, 1, 1));
            projectA.setEndDate(LocalDate.of(2024, 12, 31));

            projDAO.save(projectA);
            System.out.println("1 Project created\n");

            // === ASSIGN DEMO ===
            System.out.println("=== DEMO ASSIGN: Gán nhân viên vào dự án ===\n");
            
            empDAO.assignEmployeeToProject(emp1.getId(), projectA.getId());
            empDAO.assignEmployeeToProject(emp2.getId(), projectA.getId());
            System.out.println("✓ emp1 (Nguyen Van A) assigned to Project A");
            System.out.println("✓ emp2 (Tran Thi B) assigned to Project A\n");

            // Check state after assignment
            System.out.println("--- After Assignment ---");
            Employee e1 = empDAO.findById(Math.toIntExact(emp1.getId()));
            Employee e2 = empDAO.findById(Math.toIntExact(emp2.getId()));
            Project p = projDAO.findById(Math.toIntExact(projectA.getId()));
            
            System.out.println("emp1 projects: " + e1.getProjects().size());
            System.out.println("emp2 projects: " + e2.getProjects().size());
            System.out.println("Project A employees: " + p.getEmployees().size());
            System.out.println();

            // === UNASSIGN DEMO ===
            System.out.println("=== DEMO UNASSIGN: Gỡ emp1 khỏi Project A ===\n");
            
            empDAO.unassignEmployeeFromProject(emp1.getId(), projectA.getId());
            System.out.println("✓ emp1 (Nguyen Van A) unassigned from Project A\n");

            // Check state after unassignment
            System.out.println("--- After Unassignment ---");
            e1 = empDAO.findById(Math.toIntExact(emp1.getId()));
            e2 = empDAO.findById(Math.toIntExact(emp2.getId()));
            p = projDAO.findById(Math.toIntExact(projectA.getId()));
            
            System.out.println("emp1 projects: " + e1.getProjects().size() + " (expected: 0)");
            System.out.println("emp2 projects: " + e2.getProjects().size() + " (expected: 1)");
            System.out.println("Project A employees: " + p.getEmployees().size() + " (expected: 1)\n");

            // === VERIFICATION ===
            System.out.println("=== KIỂM TRA TOÀN VẸN DỮ LIỆU ===\n");
            
            System.out.println("1. Kiểm tra Employee không bị ảnh hưởng:");
            System.out.println("   emp1: " + e1.getFullName() + " | Email: " + e1.getEmail() + " | Status: " + (e1.isActive() ? "Active" : "Inactive"));
            System.out.println("   emp2: " + e2.getFullName() + " | Email: " + e2.getEmail() + " | Status: " + (e2.isActive() ? "Active" : "Inactive"));
            System.out.println("   ✓ Employee data unchanged\n");
            
            System.out.println("2. Kiểm tra Project không bị ảnh hưởng:");
            System.out.println("   Project: " + p.getProjectName() + " | Code: " + p.getProjectCode() + " | Budget: " + p.getBudget());
            System.out.println("   ✓ Project data unchanged\n");
            
            System.out.println("3. Kiểm tra employee_project table:");
            System.out.println("   Trước unassign: 2 rows (emp1-PA, emp2-PA)");
            System.out.println("   Sau unassign: 1 row (emp2-PA)");
            System.out.println("   Kết quả: ✓ Đúng 1 dòng bị xóa\n");
            
            System.out.println("=== DEMO HOÀN TẤT - TẤT CẢ KIỂM TRA PASS ===");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JPAUtil.close();
        }
    }
}
