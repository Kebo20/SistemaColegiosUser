/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Entidades.Agendaclase;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Clase;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author kevin
 */
public class AgendaclaseJpaController implements Serializable {

    public AgendaclaseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void editar(Agendaclase p) {

        EntityManager em = null;
        try {
            em = getEntityManager();
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

    public  List<Agendaclase>  listarxclase(int clase) {

        List<Agendaclase> lista = null;
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Query q = em.createQuery("select a from Agendaclase a  where a.idclase.id=:clase ", Agendaclase.class);
           
           
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
    
    
    public void create(Agendaclase agendaclase) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clase idclase = agendaclase.getIdclase();
            if (idclase != null) {
                idclase = em.getReference(idclase.getClass(), idclase.getId());
                agendaclase.setIdclase(idclase);
            }
            em.persist(agendaclase);
            if (idclase != null) {
                idclase.getAgendaclaseList().add(agendaclase);
                idclase = em.merge(idclase);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Agendaclase agendaclase) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agendaclase persistentAgendaclase = em.find(Agendaclase.class, agendaclase.getId());
            Clase idclaseOld = persistentAgendaclase.getIdclase();
            Clase idclaseNew = agendaclase.getIdclase();
            if (idclaseNew != null) {
                idclaseNew = em.getReference(idclaseNew.getClass(), idclaseNew.getId());
                agendaclase.setIdclase(idclaseNew);
            }
            agendaclase = em.merge(agendaclase);
            if (idclaseOld != null && !idclaseOld.equals(idclaseNew)) {
                idclaseOld.getAgendaclaseList().remove(agendaclase);
                idclaseOld = em.merge(idclaseOld);
            }
            if (idclaseNew != null && !idclaseNew.equals(idclaseOld)) {
                idclaseNew.getAgendaclaseList().add(agendaclase);
                idclaseNew = em.merge(idclaseNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = agendaclase.getId();
                if (findAgendaclase(id) == null) {
                    throw new NonexistentEntityException("The agendaclase with id " + id + " no longer exists.");
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
            Agendaclase agendaclase;
            try {
                agendaclase = em.getReference(Agendaclase.class, id);
                agendaclase.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The agendaclase with id " + id + " no longer exists.", enfe);
            }
            Clase idclase = agendaclase.getIdclase();
            if (idclase != null) {
                idclase.getAgendaclaseList().remove(agendaclase);
                idclase = em.merge(idclase);
            }
            em.remove(agendaclase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Agendaclase> findAgendaclaseEntities() {
        return findAgendaclaseEntities(true, -1, -1);
    }

    public List<Agendaclase> findAgendaclaseEntities(int maxResults, int firstResult) {
        return findAgendaclaseEntities(false, maxResults, firstResult);
    }

    private List<Agendaclase> findAgendaclaseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Agendaclase.class));
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

    public Agendaclase findAgendaclase(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Agendaclase.class, id);
        } finally {
            em.close();
        }
    }

    public int getAgendaclaseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Agendaclase> rt = cq.from(Agendaclase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
