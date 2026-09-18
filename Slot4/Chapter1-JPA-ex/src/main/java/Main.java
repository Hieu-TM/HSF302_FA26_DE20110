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

            // Create 3 Employees with full information
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
            emp3.setActive(true);

            empDAO.save(emp1);
            empDAO.save(emp2);
            empDAO.save(emp3);
            System.out.println("3 Employees created");

            // Create 2 Projects
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

            projDAO.save(projectA);
            projDAO.save(projectB);
            System.out.println("2 Projects created\n");

            // Cross-assignment: emp1 -> Project A+B, emp2 -> Project B, emp3 -> Project A
            empDAO.assignEmployeeToProject(emp1.getId(), projectA.getId());
            empDAO.assignEmployeeToProject(emp1.getId(), projectB.getId());
            empDAO.assignEmployeeToProject(emp2.getId(), projectB.getId());
            empDAO.assignEmployeeToProject(emp3.getId(), projectA.getId());
            System.out.println("Assignments completed\n");

            // Print project list for each employee
            System.out.println("=== DANH SÁCH DỰ ÁN CỦA TỪNG NHÂN VIÊN ===\n");
            
            for (Employee emp : empDAO.findAll()) {
                System.out.println("Nhân viên: " + emp.getFullName());
                System.out.println("  Email: " + emp.getEmail());
                System.out.println("  Lương: " + emp.getSalary());
                System.out.println("  Giới tính: " + emp.getGender());
                System.out.println("  Ngày tuyển dụng: " + emp.getHireDate());
                System.out.println("  Trạng thái: " + (emp.isActive() ? "Đang làm việc" : "Nghỉ việc"));
                System.out.println("  Dự án tham gia: " + emp.getProjects().size() + " dự án");
                
                for (Project project : emp.getProjects()) {
                    System.out.println("    - " + project.getProjectName() + " (" + project.getProjectCode() + ")");
                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JPAUtil.close();
        }
    }
}
