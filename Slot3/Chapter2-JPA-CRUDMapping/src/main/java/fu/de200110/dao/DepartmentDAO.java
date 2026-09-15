package fu.de200110.dao;


import fu.de200110.pojo.Department;
import fu.de200110.pojo.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

import static fu.de200110.util.JPAUtil.getEntityManagerFactory;

public class DepartmentDAO {

    public List<Department> findAll() {
        try (EntityManager em = getEntityManagerFactory().createEntityManager()) {
            return em.createQuery("select d from Department d", Department.class).getResultList();
        }

    }
    public Department findById(int id) {
        try (EntityManager em = getEntityManagerFactory().createEntityManager()) {
            return em.find(Department.class, id);
        }
    }
     public void save(Department department) {
         EntityManager em = getEntityManagerFactory().createEntityManager();
         EntityTransaction tx =  em.getTransaction();
         try {
             tx.begin();
             em.persist(department);
             em.getTransaction().commit();
         } catch (RuntimeException e) {
             if (tx != null && tx.isActive()) {
                 tx.rollback();
             }
         } finally {
             em.close();
         }
     }
     public void update(Department department) {
         EntityManager em = getEntityManagerFactory().createEntityManager();
         EntityTransaction tx =  em.getTransaction();
         try {
             tx.begin();
             em.merge(department);
             em.getTransaction().commit();

         }  catch (RuntimeException e) {
             if (tx != null && tx.isActive()) {
                 tx.rollback();
             }
         } finally {
             em.close();
         }
     }

     public void delete(Department department) {
         EntityManager em = getEntityManagerFactory().createEntityManager();
         EntityTransaction tx =  em.getTransaction();
         try {
             tx.begin();
             em.remove(department);
             tx.commit();

         }  catch (RuntimeException e) {
             if (tx != null && tx.isActive()) {
                 tx.rollback();
             }
         }  finally {
             em.close();
         }
     }

}
