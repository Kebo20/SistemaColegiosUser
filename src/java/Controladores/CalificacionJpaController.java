/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Entidades.Calificacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Clase;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class CalificacionJpaController implements Serializable {
private static EntityManagerFactory emf;
    private static EntityManager em;

    public CalificacionJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }


    
     public  List<Calificacion>  listarxclase(int clase) {

        List<Calificacion> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select c from Calificacion c where c.idclase.id =:clase ", Calificacion.class);
           
           
            q.setParameter("clase", clase);
            lista = q.getResultList();
           
            
            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }


    public void editar(Calificacion p){
        

        try {
            
            em.getTransaction().begin();
            
            em.merge(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public void create(Calificacion calificacion) {
        try {
            em.getTransaction().begin();
            Clase idclase = calificacion.getIdclase();
            if (idclase != null) {
                idclase = em.getReference(idclase.getClass(), idclase.getId());
                calificacion.setIdclase(idclase);
            }
            em.persist(calificacion);
            if (idclase != null) {
                idclase.getCalificacionList().add(calificacion);
                idclase = em.merge(idclase);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Calificacion calificacion) throws NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Calificacion persistentCalificacion = em.find(Calificacion.class, calificacion.getId());
            Clase idclaseOld = persistentCalificacion.getIdclase();
            Clase idclaseNew = calificacion.getIdclase();
            if (idclaseNew != null) {
                idclaseNew = em.getReference(idclaseNew.getClass(), idclaseNew.getId());
                calificacion.setIdclase(idclaseNew);
            }
            calificacion = em.merge(calificacion);
            if (idclaseOld != null && !idclaseOld.equals(idclaseNew)) {
                idclaseOld.getCalificacionList().remove(calificacion);
                idclaseOld = em.merge(idclaseOld);
            }
            if (idclaseNew != null && !idclaseNew.equals(idclaseOld)) {
                idclaseNew.getCalificacionList().add(calificacion);
                idclaseNew = em.merge(idclaseNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = calificacion.getId();
                if (findCalificacion(id) == null) {
                    throw new NonexistentEntityException("The calificacion with id " + id + " no longer exists.");
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
        try {
            em.getTransaction().begin();
            Calificacion calificacion;
            try {
                calificacion = em.getReference(Calificacion.class, id);
                calificacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The calificacion with id " + id + " no longer exists.", enfe);
            }
            Clase idclase = calificacion.getIdclase();
            if (idclase != null) {
                idclase.getCalificacionList().remove(calificacion);
                idclase = em.merge(idclase);
            }
            em.remove(calificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Calificacion> findCalificacionEntities() {
        return findCalificacionEntities(true, -1, -1);
    }

    public List<Calificacion> findCalificacionEntities(int maxResults, int firstResult) {
        return findCalificacionEntities(false, maxResults, firstResult);
    }

    private List<Calificacion> findCalificacionEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Calificacion.class));
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

    public Calificacion findCalificacion(Integer id) {
        try {
            return em.find(Calificacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getCalificacionCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Calificacion> rt = cq.from(Calificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
