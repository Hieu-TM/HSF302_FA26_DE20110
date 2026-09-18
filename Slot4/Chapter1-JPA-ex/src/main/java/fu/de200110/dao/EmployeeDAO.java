package fu.de200110.dao;

import fu.de200110.pojo.Employee;
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

}
