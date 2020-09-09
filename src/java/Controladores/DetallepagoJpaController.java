/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Detallepago;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Pago;
import Entidades.Tipopago;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class DetallepagoJpaController implements Serializable {

 private static EntityManagerFactory emf;
    private static EntityManager em;
    public DetallepagoJpaController() {
         emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }
 

       
     public void insertar(Detallepago a){
        

        try {
            
            em.getTransaction().begin();
            
            em.persist(a);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

     public List<Detallepago> ListarPago(int id) {

        List<Detallepago> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select d from Detallepago d where d.idpago.id = :idpago ", Detallepago.class);
            q.setParameter("idpago", id);
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

    public void create(Detallepago detallepago) throws PreexistingEntityException, Exception {
        
        try {
            em.getTransaction().begin();
            Pago idpago = detallepago.getIdpago();
            if (idpago != null) {
                idpago = em.getReference(idpago.getClass(), idpago.getId());
                detallepago.setIdpago(idpago);
            }
            Tipopago idtipopago = detallepago.getIdtipopago();
            if (idtipopago != null) {
                idtipopago = em.getReference(idtipopago.getClass(), idtipopago.getId());
                detallepago.setIdtipopago(idtipopago);
            }
            em.persist(detallepago);
            if (idpago != null) {
                idpago.getDetallepagoList().add(detallepago);
                idpago = em.merge(idpago);
            }
            if (idtipopago != null) {
                idtipopago.getDetallepagoList().add(detallepago);
                idtipopago = em.merge(idtipopago);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetallepago(detallepago.getId()) != null) {
                throw new PreexistingEntityException("Detallepago " + detallepago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallepago detallepago) throws NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Detallepago persistentDetallepago = em.find(Detallepago.class, detallepago.getId());
            Pago idpagoOld = persistentDetallepago.getIdpago();
            Pago idpagoNew = detallepago.getIdpago();
            Tipopago idtipopagoOld = persistentDetallepago.getIdtipopago();
            Tipopago idtipopagoNew = detallepago.getIdtipopago();
            if (idpagoNew != null) {
                idpagoNew = em.getReference(idpagoNew.getClass(), idpagoNew.getId());
                detallepago.setIdpago(idpagoNew);
            }
            if (idtipopagoNew != null) {
                idtipopagoNew = em.getReference(idtipopagoNew.getClass(), idtipopagoNew.getId());
                detallepago.setIdtipopago(idtipopagoNew);
            }
            detallepago = em.merge(detallepago);
            if (idpagoOld != null && !idpagoOld.equals(idpagoNew)) {
                idpagoOld.getDetallepagoList().remove(detallepago);
                idpagoOld = em.merge(idpagoOld);
            }
            if (idpagoNew != null && !idpagoNew.equals(idpagoOld)) {
                idpagoNew.getDetallepagoList().add(detallepago);
                idpagoNew = em.merge(idpagoNew);
            }
            if (idtipopagoOld != null && !idtipopagoOld.equals(idtipopagoNew)) {
                idtipopagoOld.getDetallepagoList().remove(detallepago);
                idtipopagoOld = em.merge(idtipopagoOld);
            }
            if (idtipopagoNew != null && !idtipopagoNew.equals(idtipopagoOld)) {
                idtipopagoNew.getDetallepagoList().add(detallepago);
                idtipopagoNew = em.merge(idtipopagoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detallepago.getId();
                if (findDetallepago(id) == null) {
                    throw new NonexistentEntityException("The detallepago with id " + id + " no longer exists.");
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
            Detallepago detallepago;
            try {
                detallepago = em.getReference(Detallepago.class, id);
                detallepago.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallepago with id " + id + " no longer exists.", enfe);
            }
            Pago idpago = detallepago.getIdpago();
            if (idpago != null) {
                idpago.getDetallepagoList().remove(detallepago);
                idpago = em.merge(idpago);
            }
            Tipopago idtipopago = detallepago.getIdtipopago();
            if (idtipopago != null) {
                idtipopago.getDetallepagoList().remove(detallepago);
                idtipopago = em.merge(idtipopago);
            }
            em.remove(detallepago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detallepago> findDetallepagoEntities() {
        return findDetallepagoEntities(true, -1, -1);
    }

    public List<Detallepago> findDetallepagoEntities(int maxResults, int firstResult) {
        return findDetallepagoEntities(false, maxResults, firstResult);
    }

    private List<Detallepago> findDetallepagoEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallepago.class));
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

    public Detallepago findDetallepago(Integer id) {
        try {
            return em.find(Detallepago.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallepagoCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallepago> rt = cq.from(Detallepago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
