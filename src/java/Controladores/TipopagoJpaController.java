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
import Entidades.Detallepago;
import Entidades.Tipopago;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class TipopagoJpaController implements Serializable {



 private static EntityManagerFactory emf;
    private static EntityManager em;

    public TipopagoJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }

    public void editar(Tipopago p) {

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

    public void create(Tipopago tipopago) {
        if (tipopago.getDetallepagoList() == null) {
            tipopago.setDetallepagoList(new ArrayList<Detallepago>());
        }
        try {
            em.getTransaction().begin();
            List<Detallepago> attachedDetallepagoList = new ArrayList<Detallepago>();
            for (Detallepago detallepagoListDetallepagoToAttach : tipopago.getDetallepagoList()) {
                detallepagoListDetallepagoToAttach = em.getReference(detallepagoListDetallepagoToAttach.getClass(), detallepagoListDetallepagoToAttach.getId());
                attachedDetallepagoList.add(detallepagoListDetallepagoToAttach);
            }
            tipopago.setDetallepagoList(attachedDetallepagoList);
            em.persist(tipopago);
            for (Detallepago detallepagoListDetallepago : tipopago.getDetallepagoList()) {
                Tipopago oldIdtipopagoOfDetallepagoListDetallepago = detallepagoListDetallepago.getIdtipopago();
                detallepagoListDetallepago.setIdtipopago(tipopago);
                detallepagoListDetallepago = em.merge(detallepagoListDetallepago);
                if (oldIdtipopagoOfDetallepagoListDetallepago != null) {
                    oldIdtipopagoOfDetallepagoListDetallepago.getDetallepagoList().remove(detallepagoListDetallepago);
                    oldIdtipopagoOfDetallepagoListDetallepago = em.merge(oldIdtipopagoOfDetallepagoListDetallepago);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipopago tipopago) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Tipopago persistentTipopago = em.find(Tipopago.class, tipopago.getId());
            List<Detallepago> detallepagoListOld = persistentTipopago.getDetallepagoList();
            List<Detallepago> detallepagoListNew = tipopago.getDetallepagoList();
            List<String> illegalOrphanMessages = null;
            for (Detallepago detallepagoListOldDetallepago : detallepagoListOld) {
                if (!detallepagoListNew.contains(detallepagoListOldDetallepago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallepago " + detallepagoListOldDetallepago + " since its idtipopago field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Detallepago> attachedDetallepagoListNew = new ArrayList<Detallepago>();
            for (Detallepago detallepagoListNewDetallepagoToAttach : detallepagoListNew) {
                detallepagoListNewDetallepagoToAttach = em.getReference(detallepagoListNewDetallepagoToAttach.getClass(), detallepagoListNewDetallepagoToAttach.getId());
                attachedDetallepagoListNew.add(detallepagoListNewDetallepagoToAttach);
            }
            detallepagoListNew = attachedDetallepagoListNew;
            tipopago.setDetallepagoList(detallepagoListNew);
            tipopago = em.merge(tipopago);
            for (Detallepago detallepagoListNewDetallepago : detallepagoListNew) {
                if (!detallepagoListOld.contains(detallepagoListNewDetallepago)) {
                    Tipopago oldIdtipopagoOfDetallepagoListNewDetallepago = detallepagoListNewDetallepago.getIdtipopago();
                    detallepagoListNewDetallepago.setIdtipopago(tipopago);
                    detallepagoListNewDetallepago = em.merge(detallepagoListNewDetallepago);
                    if (oldIdtipopagoOfDetallepagoListNewDetallepago != null && !oldIdtipopagoOfDetallepagoListNewDetallepago.equals(tipopago)) {
                        oldIdtipopagoOfDetallepagoListNewDetallepago.getDetallepagoList().remove(detallepagoListNewDetallepago);
                        oldIdtipopagoOfDetallepagoListNewDetallepago = em.merge(oldIdtipopagoOfDetallepagoListNewDetallepago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipopago.getId();
                if (findTipopago(id) == null) {
                    throw new NonexistentEntityException("The tipopago with id " + id + " no longer exists.");
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
            Tipopago tipopago;
            try {
                tipopago = em.getReference(Tipopago.class, id);
                tipopago.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipopago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallepago> detallepagoListOrphanCheck = tipopago.getDetallepagoList();
            for (Detallepago detallepagoListOrphanCheckDetallepago : detallepagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipopago (" + tipopago + ") cannot be destroyed since the Detallepago " + detallepagoListOrphanCheckDetallepago + " in its detallepagoList field has a non-nullable idtipopago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipopago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipopago> findTipopagoEntities() {
        return findTipopagoEntities(true, -1, -1);
    }

    public List<Tipopago> findTipopagoEntities(int maxResults, int firstResult) {
        return findTipopagoEntities(false, maxResults, firstResult);
    }

    private List<Tipopago> findTipopagoEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipopago.class));
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

    public Tipopago findTipopago(Integer id) {
        try {
            return em.find(Tipopago.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipopagoCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipopago> rt = cq.from(Tipopago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
