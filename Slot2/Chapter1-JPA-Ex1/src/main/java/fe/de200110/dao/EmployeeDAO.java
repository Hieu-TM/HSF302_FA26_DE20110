package fe.de200110.dao;

import fe.de200110.pojo.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
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

        try (EntityManager em = getEntityManager()) {
            return em.find(Employee.class, id);
        }
    }

    public List<Employee> findAll() {
        try (EntityManager em = getEntityManager()) {
            return em.createQuery("select e from Employee e", Employee.class).getResultList();
        }
    }

    public Employee findByEmail(String email) {

        try (EntityManager em = getEntityManager()) {
            List<Employee> result = em.createQuery("select e from Employee e where e.email = :email", Employee.class).setParameter("email", email).getResultList();

            return (result.isEmpty()) ? null : result.getFirst();
        }
    }

    public List<Employee> findBySalaryGreaterThanAndActive(BigDecimal salary) {

        try (EntityManager em = getEntityManager()) {
            return em.createQuery("select e from Employee e where e.salary >  :salary and e.active = true", Employee.class).setParameter("salary", salary).getResultList();
        }
    }

    public Employee update(Employee e) {
        EntityManager  em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee merged = em.merge(e);
            tx.commit();

            return merged;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
    public void delete(Long id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Employee e = em.find(Employee.class, id);
            if (e != null) {
                em.remove(e);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

}


