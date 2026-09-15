package fu.de200110;

import fu.de200110.dao.DepartmentDAO;
import fu.de200110.pojo.Department;
import fu.de200110.pojo.Employee;
import fu.de200110.pojo.Gender;
import fu.de200110.util.JPAUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        DepartmentDAO departmentDAO = new DepartmentDAO();

    // 1) Tạo Department + 3 Employee, add qua helper method (TODO 2.4)
        Department it = new Department("Marketing", "Ha Noi");

        Employee e1 = new Employee("Nguyen Van A", "aa. nguyen@company.com", new BigDecimal("15000000"), Gender.MALE, LocalDate.of(2022, 1, 10));
        Employee e2 = new Employee("Tran Thi B", "bb. tran@company.com", new BigDecimal("18000000"), Gender.OTHER, LocalDate.of(2021, 6, 1));
        Employee e3 = new Employee("Le Van C", "cc. le@company.com", new BigDecimal("12000000"), Gender.OTHER, LocalDate.of(2023, 3, 15));

        it.addEmployee(e1);
        it.addEmployee(e2);
        it.addEmployee(e3);

    // 2) Chi persist (department) - cascade = ALL tự lo phần Employee (TODO 2.7)
        departmentDAO.save(it);

        System.out.println("Da luu Department, id =" + it.getId());

    // 3) Tim lai kem employees bang JOIN FETCH (TODO 2.6) - khong bi
    //    LazyInitializationException du EntityManager cua lan tim nay da dong,
    //    vi employees da duoc load ngay trong cung 1 query.
        Department found = departmentDAO.findDepartmentWithEmployee(it.getId());
        System.out.println("Phong ban: " + found.getName());
        for (Employee e : found.getEmployees()) {
            System.out.println(" - " + e);
            JPAUtil.close();
        }
    }
}
