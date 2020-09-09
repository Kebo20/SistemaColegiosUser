/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Nivel;
import Entidades.Area;
import Entidades.Grado;
import java.util.ArrayList;
import java.util.List;
import Entidades.Seccion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class GradoJpaController implements Serializable {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public GradoJpaController() {

        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }

    
     public void editar(Grado p){
        

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
    public void create(Grado grado) {
       
        if (grado.getSeccionList() == null) {
            grado.setSeccionList(new ArrayList<Seccion>());
        }
        try {
            em.getTransaction().begin();
            Nivel idnivel = grado.getIdnivel();
            if (idnivel != null) {
                idnivel = em.getReference(idnivel.getClass(), idnivel.getId());
                grado.setIdnivel(idnivel);
            }
            List<Area> attachedAreaList = new ArrayList<Area>();
         
            List<Seccion> attachedSeccionList = new ArrayList<Seccion>();
            for (Seccion seccionListSeccionToAttach : grado.getSeccionList()) {
                seccionListSeccionToAttach = em.getReference(seccionListSeccionToAttach.getClass(), seccionListSeccionToAttach.getId());
                attachedSeccionList.add(seccionListSeccionToAttach);
            }
            grado.setSeccionList(attachedSeccionList);
            em.persist(grado);
            if (idnivel != null) {
                idnivel.getGradoList().add(grado);
                idnivel = em.merge(idnivel);
            }
           
            for (Seccion seccionListSeccion : grado.getSeccionList()) {
                Grado oldIdgradoOfSeccionListSeccion = seccionListSeccion.getIdgrado();
                seccionListSeccion.setIdgrado(grado);
                seccionListSeccion = em.merge(seccionListSeccion);
                if (oldIdgradoOfSeccionListSeccion != null) {
                    oldIdgradoOfSeccionListSeccion.getSeccionList().remove(seccionListSeccion);
                    oldIdgradoOfSeccionListSeccion = em.merge(oldIdgradoOfSeccionListSeccion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grado grado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Grado persistentGrado = em.find(Grado.class, grado.getId());
            Nivel idnivelOld = persistentGrado.getIdnivel();
            Nivel idnivelNew = grado.getIdnivel();
           
            List<Seccion> seccionListOld = persistentGrado.getSeccionList();
            List<Seccion> seccionListNew = grado.getSeccionList();
            List<String> illegalOrphanMessages = null;
           
            for (Seccion seccionListOldSeccion : seccionListOld) {
                if (!seccionListNew.contains(seccionListOldSeccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Seccion " + seccionListOldSeccion + " since its idgrado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idnivelNew != null) {
                idnivelNew = em.getReference(idnivelNew.getClass(), idnivelNew.getId());
                grado.setIdnivel(idnivelNew);
            }
            
            List<Seccion> attachedSeccionListNew = new ArrayList<Seccion>();
            for (Seccion seccionListNewSeccionToAttach : seccionListNew) {
                seccionListNewSeccionToAttach = em.getReference(seccionListNewSeccionToAttach.getClass(), seccionListNewSeccionToAttach.getId());
                attachedSeccionListNew.add(seccionListNewSeccionToAttach);
            }
            seccionListNew = attachedSeccionListNew;
            grado.setSeccionList(seccionListNew);
            grado = em.merge(grado);
            if (idnivelOld != null && !idnivelOld.equals(idnivelNew)) {
                idnivelOld.getGradoList().remove(grado);
                idnivelOld = em.merge(idnivelOld);
            }
            if (idnivelNew != null && !idnivelNew.equals(idnivelOld)) {
                idnivelNew.getGradoList().add(grado);
                idnivelNew = em.merge(idnivelNew);
            }
           
            for (Seccion seccionListNewSeccion : seccionListNew) {
                if (!seccionListOld.contains(seccionListNewSeccion)) {
                    Grado oldIdgradoOfSeccionListNewSeccion = seccionListNewSeccion.getIdgrado();
                    seccionListNewSeccion.setIdgrado(grado);
                    seccionListNewSeccion = em.merge(seccionListNewSeccion);
                    if (oldIdgradoOfSeccionListNewSeccion != null && !oldIdgradoOfSeccionListNewSeccion.equals(grado)) {
                        oldIdgradoOfSeccionListNewSeccion.getSeccionList().remove(seccionListNewSeccion);
                        oldIdgradoOfSeccionListNewSeccion = em.merge(oldIdgradoOfSeccionListNewSeccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grado.getId();
                if (findGrado(id) == null) {
                    throw new NonexistentEntityException("The grado with id " + id + " no longer exists.");
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
            Grado grado;
            try {
                grado = em.getReference(Grado.class, id);
                grado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
         
            List<Seccion> seccionListOrphanCheck = grado.getSeccionList();
            for (Seccion seccionListOrphanCheckSeccion : seccionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grado (" + grado + ") cannot be destroyed since the Seccion " + seccionListOrphanCheckSeccion + " in its seccionList field has a non-nullable idgrado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Nivel idnivel = grado.getIdnivel();
            if (idnivel != null) {
                idnivel.getGradoList().remove(grado);
                idnivel = em.merge(idnivel);
            }
            em.remove(grado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grado> findGradoEntities() {
        return findGradoEntities(true, -1, -1);
    }

    public List<Grado> findGradoEntities(int maxResults, int firstResult) {
        return findGradoEntities(false, maxResults, firstResult);
    }

    private List<Grado> findGradoEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grado.class));
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

    public Grado findGrado(Integer id) {
        try {
            return em.find(Grado.class, id);
        } finally {
            em.close();
        }
    }

    public int getGradoCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grado> rt = cq.from(Grado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
