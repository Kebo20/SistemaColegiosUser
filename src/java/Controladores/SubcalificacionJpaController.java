/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Entidades.Calificacion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Notas;
import Entidades.Subcalificacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class SubcalificacionJpaController implements Serializable {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    public SubcalificacionJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }
   
 public void editar(Subcalificacion p){
        

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
    public  List<Subcalificacion>  listarxcalificacion(int calificacion) {

        List<Subcalificacion>  lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select c from Subcalificacion c where c.idcalificacion.id = :calificacion ", Subcalificacion.class);
           
           
            q.setParameter("calificacion", calificacion);
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
    public  List<Subcalificacion>  listarxclase(int clase) {

        List<Subcalificacion>  lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select c from Subcalificacion c where c.idcalificacion.idclase.id = :clase ", Subcalificacion.class);
           
           
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
  
    public void create(Subcalificacion subcalificacion) {
        if (subcalificacion.getNotasList() == null) {
            subcalificacion.setNotasList(new ArrayList<Notas>());
        }
        try {
            em.getTransaction().begin();
            List<Notas> attachedNotasList = new ArrayList<Notas>();
            for (Notas notasListNotasToAttach : subcalificacion.getNotasList()) {
                notasListNotasToAttach = em.getReference(notasListNotasToAttach.getClass(), notasListNotasToAttach.getId());
                attachedNotasList.add(notasListNotasToAttach);
            }
            subcalificacion.setNotasList(attachedNotasList);
            em.persist(subcalificacion);
            for (Notas notasListNotas : subcalificacion.getNotasList()) {
                Subcalificacion oldIdsubcalificacionOfNotasListNotas = notasListNotas.getIdsubcalificacion();
                notasListNotas.setIdsubcalificacion(subcalificacion);
                notasListNotas = em.merge(notasListNotas);
                if (oldIdsubcalificacionOfNotasListNotas != null) {
                    oldIdsubcalificacionOfNotasListNotas.getNotasList().remove(notasListNotas);
                    oldIdsubcalificacionOfNotasListNotas = em.merge(oldIdsubcalificacionOfNotasListNotas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Subcalificacion subcalificacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Subcalificacion persistentSubcalificacion = em.find(Subcalificacion.class, subcalificacion.getId());
            List<Notas> notasListOld = persistentSubcalificacion.getNotasList();
            List<Notas> notasListNew = subcalificacion.getNotasList();
            List<String> illegalOrphanMessages = null;
            for (Notas notasListOldNotas : notasListOld) {
                if (!notasListNew.contains(notasListOldNotas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notas " + notasListOldNotas + " since its idsubcalificacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Notas> attachedNotasListNew = new ArrayList<Notas>();
            for (Notas notasListNewNotasToAttach : notasListNew) {
                notasListNewNotasToAttach = em.getReference(notasListNewNotasToAttach.getClass(), notasListNewNotasToAttach.getId());
                attachedNotasListNew.add(notasListNewNotasToAttach);
            }
            notasListNew = attachedNotasListNew;
            subcalificacion.setNotasList(notasListNew);
            subcalificacion = em.merge(subcalificacion);
            for (Notas notasListNewNotas : notasListNew) {
                if (!notasListOld.contains(notasListNewNotas)) {
                    Subcalificacion oldIdsubcalificacionOfNotasListNewNotas = notasListNewNotas.getIdsubcalificacion();
                    notasListNewNotas.setIdsubcalificacion(subcalificacion);
                    notasListNewNotas = em.merge(notasListNewNotas);
                    if (oldIdsubcalificacionOfNotasListNewNotas != null && !oldIdsubcalificacionOfNotasListNewNotas.equals(subcalificacion)) {
                        oldIdsubcalificacionOfNotasListNewNotas.getNotasList().remove(notasListNewNotas);
                        oldIdsubcalificacionOfNotasListNewNotas = em.merge(oldIdsubcalificacionOfNotasListNewNotas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = subcalificacion.getId();
                if (findSubcalificacion(id) == null) {
                    throw new NonexistentEntityException("The subcalificacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        try {
            em.getTransaction().begin();
            Subcalificacion subcalificacion;
            try {
                subcalificacion = em.getReference(Subcalificacion.class, id);
                subcalificacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subcalificacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Notas> notasListOrphanCheck = subcalificacion.getNotasList();
            for (Notas notasListOrphanCheckNotas : notasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subcalificacion (" + subcalificacion + ") cannot be destroyed since the Notas " + notasListOrphanCheckNotas + " in its notasList field has a non-nullable idsubcalificacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(subcalificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Subcalificacion> findSubcalificacionEntities() {
        return findSubcalificacionEntities(true, -1, -1);
    }

    public List<Subcalificacion> findSubcalificacionEntities(int maxResults, int firstResult) {
        return findSubcalificacionEntities(false, maxResults, firstResult);
    }

    private List<Subcalificacion> findSubcalificacionEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subcalificacion.class));
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

    public Subcalificacion findSubcalificacion(Integer id) {
        try {
            return em.find(Subcalificacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubcalificacionCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subcalificacion> rt = cq.from(Subcalificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
