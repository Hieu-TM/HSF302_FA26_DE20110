package fu.de200110.dao;

import fu.de200110.pojo.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

import static fu.de200110.util.JPAUtil.getEntityManagerFactory;

public class ProjectDAO {

    public List<Project> findAll() {
        try (EntityManager em = getEntityManagerFactory().createEntityManager()) {
            return em.createQuery("select p from Project p", Project.class).getResultList();
        }
    }

    public Project findById(Long id) {
        try (EntityManager em = getEntityManagerFactory().createEntityManager()) {
            return em.find(Project.class, id);
        }
    }

    public void save(Project project) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(project);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public void update(Project project) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(project);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public void delete(Project project) {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(project);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
