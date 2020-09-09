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
import Entidades.Alumno;
import Entidades.Detallepago;
import Entidades.Pago;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class PagoJpaController implements Serializable {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public PagoJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }

    public void create(Pago pago) {
        if (pago.getDetallepagoList() == null) {
            pago.setDetallepagoList(new ArrayList<Detallepago>());
        }
        try {
            em.getTransaction().begin();
            Alumno idalumno = pago.getIdalumno();
            if (idalumno != null) {
                idalumno = em.getReference(idalumno.getClass(), idalumno.getId());
                pago.setIdalumno(idalumno);
            }
            List<Detallepago> attachedDetallepagoList = new ArrayList<Detallepago>();
            for (Detallepago detallepagoListDetallepagoToAttach : pago.getDetallepagoList()) {
                detallepagoListDetallepagoToAttach = em.getReference(detallepagoListDetallepagoToAttach.getClass(), detallepagoListDetallepagoToAttach.getId());
                attachedDetallepagoList.add(detallepagoListDetallepagoToAttach);
            }
            pago.setDetallepagoList(attachedDetallepagoList);
            em.persist(pago);
            if (idalumno != null) {
                idalumno.getPagoList().add(pago);
                idalumno = em.merge(idalumno);
            }
            for (Detallepago detallepagoListDetallepago : pago.getDetallepagoList()) {
                Pago oldIdpagoOfDetallepagoListDetallepago = detallepagoListDetallepago.getIdpago();
                detallepagoListDetallepago.setIdpago(pago);
                detallepagoListDetallepago = em.merge(detallepagoListDetallepago);
                if (oldIdpagoOfDetallepagoListDetallepago != null) {
                    oldIdpagoOfDetallepagoListDetallepago.getDetallepagoList().remove(detallepagoListDetallepago);
                    oldIdpagoOfDetallepagoListDetallepago = em.merge(oldIdpagoOfDetallepagoListDetallepago);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pago pago) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getId());
            Alumno idalumnoOld = persistentPago.getIdalumno();
            Alumno idalumnoNew = pago.getIdalumno();
            List<Detallepago> detallepagoListOld = persistentPago.getDetallepagoList();
            List<Detallepago> detallepagoListNew = pago.getDetallepagoList();
            List<String> illegalOrphanMessages = null;
            for (Detallepago detallepagoListOldDetallepago : detallepagoListOld) {
                if (!detallepagoListNew.contains(detallepagoListOldDetallepago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallepago " + detallepagoListOldDetallepago + " since its idpago field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idalumnoNew != null) {
                idalumnoNew = em.getReference(idalumnoNew.getClass(), idalumnoNew.getId());
                pago.setIdalumno(idalumnoNew);
            }
            List<Detallepago> attachedDetallepagoListNew = new ArrayList<Detallepago>();
            for (Detallepago detallepagoListNewDetallepagoToAttach : detallepagoListNew) {
                detallepagoListNewDetallepagoToAttach = em.getReference(detallepagoListNewDetallepagoToAttach.getClass(), detallepagoListNewDetallepagoToAttach.getId());
                attachedDetallepagoListNew.add(detallepagoListNewDetallepagoToAttach);
            }
            detallepagoListNew = attachedDetallepagoListNew;
            pago.setDetallepagoList(detallepagoListNew);
            pago = em.merge(pago);
            if (idalumnoOld != null && !idalumnoOld.equals(idalumnoNew)) {
                idalumnoOld.getPagoList().remove(pago);
                idalumnoOld = em.merge(idalumnoOld);
            }
            if (idalumnoNew != null && !idalumnoNew.equals(idalumnoOld)) {
                idalumnoNew.getPagoList().add(pago);
                idalumnoNew = em.merge(idalumnoNew);
            }
            for (Detallepago detallepagoListNewDetallepago : detallepagoListNew) {
                if (!detallepagoListOld.contains(detallepagoListNewDetallepago)) {
                    Pago oldIdpagoOfDetallepagoListNewDetallepago = detallepagoListNewDetallepago.getIdpago();
                    detallepagoListNewDetallepago.setIdpago(pago);
                    detallepagoListNewDetallepago = em.merge(detallepagoListNewDetallepago);
                    if (oldIdpagoOfDetallepagoListNewDetallepago != null && !oldIdpagoOfDetallepagoListNewDetallepago.equals(pago)) {
                        oldIdpagoOfDetallepagoListNewDetallepago.getDetallepagoList().remove(detallepagoListNewDetallepago);
                        oldIdpagoOfDetallepagoListNewDetallepago = em.merge(oldIdpagoOfDetallepagoListNewDetallepago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pago.getId();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void eliminar(Pago autor) {

        try {
            em.getTransaction().begin();
            Pago oautor = em.find(Pago.class, autor.getId());
            em.remove(oautor);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        try {
            em.getTransaction().begin();
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallepago> detallepagoListOrphanCheck = pago.getDetallepagoList();
            for (Detallepago detallepagoListOrphanCheckDetallepago : detallepagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pago (" + pago + ") cannot be destroyed since the Detallepago " + detallepagoListOrphanCheckDetallepago + " in its detallepagoList field has a non-nullable idpago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Alumno idalumno = pago.getIdalumno();
            if (idalumno != null) {
                idalumno.getPagoList().remove(pago);
                idalumno = em.merge(idalumno);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(Integer id) {
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Pago pagoxnorden(String norden) {

        Pago p = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select p from Pago p where p.numeroComprobante = :norden", Pago.class);
            q.setParameter("norden", norden);
            p = (Pago) q.getResultList().get(0);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
        return p;
    }

    public  List<Pago> listarxalumno(int idalumno) {

        List<Pago> lista=null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select p from Pago p where p.idalumno.id  = :alumno", Pago.class);
            q.setParameter("alumno", idalumno);
           lista =  q.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
        return lista;
    }

}
