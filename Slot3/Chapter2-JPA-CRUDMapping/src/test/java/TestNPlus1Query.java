import fu.de200110.dao.DepartmentDAO;
import fu.de200110.pojo.Department;
import fu.de200110.util.JPAUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;

import java.util.List;

public class TestNPlus1Query {
    public static void main(String[] args) {
        DepartmentDAO departmentDAO = new DepartmentDAO();
        List<Department> dList = departmentDAO.findAll();

        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            for (Department d : dList) {
                d = em.find(Department.class, d.getId());
                System.out.println(d.getEmployees());
            }
        }
    }
}