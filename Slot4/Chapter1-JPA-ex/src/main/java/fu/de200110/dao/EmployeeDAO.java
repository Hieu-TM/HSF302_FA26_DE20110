package fu.de200110.dao;

import fu.de200110.pojo.Employee;
import fu.de200110.pojo.Project;
import fu.de200110.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

import static fu.de200110.util.JPAUtil.getEntityManagerFactory;


public class EmployeeDAO {

    public List<Employee> findAll() {
        try (EntityManager em = getEntityManagerFactory().createEntityManager()) {
            return em.createQuery("select e from Employee e", Employee.class).getResultList();
        }
    }

    public Employee findById(int id) {
        try (EntityManager em = getEntityManagerFactory().createEntityManager()) {
            return em.find(Employee.class, id);
        }
    }

    public void update(Employee employee) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction tx =  em.getTransaction();

        try {
            tx.begin();
            em.merge(employee);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public void delete(Employee employee) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction tx =  em.getTransaction();
        try {
            tx.begin();
            em.remove(employee);
            tx.commit();
        }  catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally{
            em.close();
        }
    }

    public void save(Employee employee) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction tx =  em.getTransaction();
        try {
            tx.begin();
            em.persist(employee);
            tx.commit();
        }   catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally{
            em.close();
        }
    }

    public void assignEmployeeToProject(Long employeeId, Long projectId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee employee = em.find(Employee.class, employeeId);
            Project project = em.find(Project.class, projectId);
            
            if (employee == null || project == null) {
                throw new IllegalArgumentException("Employee or Project not exists");
            }
            
            employee.assignToProject(project);
            
            em.merge(employee);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public void unassignEmployeeFromProject(Long employeeId, Long projectId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee employee = em.find(Employee.class, employeeId);
            Project project = em.find(Project.class, projectId);
            
            if (employee == null || project == null) {
                throw new IllegalArgumentException("Employee or Project not exists");
            }
            
            employee.unassignFromProject(project);
            
            em.merge(employee);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Employee> findActiveEmployeesWithMultipleProjects() {
        try (EntityManager em = getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(
                "SELECT e FROM Employee e WHERE e.active = true AND SIZE(e.projects) > 1",
                Employee.class
            ).getResultList();
        }
    }

    /**
     * Deactivate an employee by setting active = false.
     * 
     * DESIGN DECISION: Khi nhân viên nghỉ việc, KHÔNG nên tự động xóa relationships với projects.
     * 
     * LÝ DO KHÔNG NÊN CASCADE REMOVE:
     * 1. AUDIT TRAIL & LỊCH SỬ: Cần giữ lại records lịch sử người tham gia dự án nào, từ khi nào
     *    - Nếu xóa luôn, sẽ mất dữ liệu lịch sử quan trọng
     *    - Không thể trace "ai đã làm gì trên dự án này"
     * 
     * 2. BUSINESS REPORTS: Cần generate reports như:
     *    - "Các dự án mà nhân viên X đã tham gia trong thời gian làm việc"
     *    - "Danh sách nhân viên từng tham gia dự án Y"
     *    - Data analytics cần lịch sử đầy đủ
     *
     * 
     * 3. RECOVERY: Nếu nhân viên quay lại làm việc, có thể restore toàn bộ dữ liệu
     *
     * VÍ DỤ:
     *   // Deactivate nhân viên nhưng GIỮ lại lịch sử
     *   empDAO.deactivateEmployee(employeeId);
     *   
     *   // Nếu muốn xóa khỏi dự án hiện tại, gọi riêng:
     *   empDAO.unassignEmployeeFromProject(employeeId, projectId);
     */
    public void deactivateEmployee(Long employeeId) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee employee = em.find(Employee.class, employeeId);
            
            if (employee == null) {
                throw new IllegalArgumentException("Employee not found");
            }
            
            employee.setActive(false);
            em.merge(employee);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

}
