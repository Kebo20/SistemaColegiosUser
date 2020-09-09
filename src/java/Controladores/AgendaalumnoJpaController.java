/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Entidades.Agendaalumno;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Matriculaclase;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author kevin
 */
public class AgendaalumnoJpaController implements Serializable {

    public AgendaalumnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Agendaalumno agendaalumno) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Matriculaclase idmatriculaclase = agendaalumno.getIdmatriculaclase();
            if (idmatriculaclase != null) {
                idmatriculaclase = em.getReference(idmatriculaclase.getClass(), idmatriculaclase.getId());
                agendaalumno.setIdmatriculaclase(idmatriculaclase);
            }
            em.persist(agendaalumno);
            if (idmatriculaclase != null) {
                idmatriculaclase.getAgendaalumnoList().add(agendaalumno);
                idmatriculaclase = em.merge(idmatriculaclase);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Agendaalumno agendaalumno) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agendaalumno persistentAgendaalumno = em.find(Agendaalumno.class, agendaalumno.getId());
            Matriculaclase idmatriculaclaseOld = persistentAgendaalumno.getIdmatriculaclase();
            Matriculaclase idmatriculaclaseNew = agendaalumno.getIdmatriculaclase();
            if (idmatriculaclaseNew != null) {
                idmatriculaclaseNew = em.getReference(idmatriculaclaseNew.getClass(), idmatriculaclaseNew.getId());
                agendaalumno.setIdmatriculaclase(idmatriculaclaseNew);
            }
            agendaalumno = em.merge(agendaalumno);
            if (idmatriculaclaseOld != null && !idmatriculaclaseOld.equals(idmatriculaclaseNew)) {
                idmatriculaclaseOld.getAgendaalumnoList().remove(agendaalumno);
                idmatriculaclaseOld = em.merge(idmatriculaclaseOld);
            }
            if (idmatriculaclaseNew != null && !idmatriculaclaseNew.equals(idmatriculaclaseOld)) {
                idmatriculaclaseNew.getAgendaalumnoList().add(agendaalumno);
                idmatriculaclaseNew = em.merge(idmatriculaclaseNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = agendaalumno.getId();
                if (findAgendaalumno(id) == null) {
                    throw new NonexistentEntityException("The agendaalumno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agendaalumno agendaalumno;
            try {
                agendaalumno = em.getReference(Agendaalumno.class, id);
                agendaalumno.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The agendaalumno with id " + id + " no longer exists.", enfe);
            }
            Matriculaclase idmatriculaclase = agendaalumno.getIdmatriculaclase();
            if (idmatriculaclase != null) {
                idmatriculaclase.getAgendaalumnoList().remove(agendaalumno);
                idmatriculaclase = em.merge(idmatriculaclase);
            }
            em.remove(agendaalumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Agendaalumno> findAgendaalumnoEntities() {
        return findAgendaalumnoEntities(true, -1, -1);
    }

    public List<Agendaalumno> findAgendaalumnoEntities(int maxResults, int firstResult) {
        return findAgendaalumnoEntities(false, maxResults, firstResult);
    }

    private List<Agendaalumno> findAgendaalumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Agendaalumno.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Agendaalumno findAgendaalumno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Agendaalumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAgendaalumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Agendaalumno> rt = cq.from(Agendaalumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
