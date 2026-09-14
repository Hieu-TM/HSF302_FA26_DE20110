package fe.de200110.dao;

import fe.de200110.pojo.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class EmployeeDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("hsf302");
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void save(Employee employee) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(employee);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }


    }

    public Employee findById(Long id) {
        EntityManager em = getEntityManager();

        try {
            return  em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public List<Employee> findAll() {
        EntityManager em = getEntityManager();
        try {
            return  em.createQuery("select e from Employee e").getResultList();
        }
        finally {
            em.close();
        }
    }
}
