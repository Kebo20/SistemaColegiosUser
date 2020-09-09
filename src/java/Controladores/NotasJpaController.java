/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates

* and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Matriculaclase;
import Entidades.Notas;
import Entidades.Periodo;
import Entidades.Subcalificacion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author kevin
 */
public class NotasJpaController implements Serializable {

    public NotasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void editar(Notas p) {

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

    public Notas listarxsubcalificacion_periodo_matriculaclase(int periodo, int subcalificacion, int matriculaclase) {
        EntityManager em = null;
        List<Notas> lista = null;
        Notas n = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Query q = em.createQuery("select c from Notas c where c.idperiodo.id= :periodo and c.idsubcalificacion.id=:subcalificacion and c.idmatriculaclase.id=:matriculaclase", Notas.class);

            q.setParameter("subcalificacion", subcalificacion);
            q.setParameter("periodo", periodo);
            q.setParameter("matriculaclase", matriculaclase);
            lista = q.getResultList();
            n = lista.get(0);

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
               em.close();
            }
        }
        return n;
    }
    
    public List<Notas> calificacion_periodo_matriculaclase(int periodo, int calificacion, int matriculaclase) {
        EntityManager em = null;
        List<Notas> lista = null;
        Notas n = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Query q = em.createQuery("select c from Notas c where c.idperiodo.id= :periodo and c.idsubcalificacion.idcalificacion.id =:calificacion and c.idmatriculaclase.id=:matriculaclase", Double.class);

            q.setParameter("calificacion", calificacion);
            q.setParameter("periodo", periodo);
            q.setParameter("matriculaclase", matriculaclase);
            lista = q.getResultList();
            n = lista.get(0);

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
    
    public List<Notas> clase_periodo_matriculaclase(int periodo, int clase, int matriculaclase) {
        EntityManager em = null;
        List<Notas> lista = null;
        Notas n = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Query q = em.createQuery("select c from Notas c where c.idperiodo.id= :periodo and c.idsubcalificacion.idcalificacion.idclase.id =:clase and c.idmatriculaclase.id=:matriculaclase", Double.class);

            q.setParameter("clase", clase);
            q.setParameter("periodo", periodo);
            q.setParameter("matriculaclase", matriculaclase);
            lista = q.getResultList();
            n = lista.get(0);

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

    public void create(Notas notas) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Matriculaclase idmatriculaclase = notas.getIdmatriculaclase();
            if (idmatriculaclase != null) {
                idmatriculaclase = em.getReference(idmatriculaclase.getClass(), idmatriculaclase.getId());
                notas.setIdmatriculaclase(idmatriculaclase);
            }
            Periodo idperiodo = notas.getIdperiodo();
            if (idperiodo != null) {
                idperiodo = em.getReference(idperiodo.getClass(), idperiodo.getId());
                notas.setIdperiodo(idperiodo);
            }
            Subcalificacion idsubcalificacion = notas.getIdsubcalificacion();
            if (idsubcalificacion != null) {
                idsubcalificacion = em.getReference(idsubcalificacion.getClass(), idsubcalificacion.getId());
                notas.setIdsubcalificacion(idsubcalificacion);
            }
            em.persist(notas);
            if (idmatriculaclase != null) {
                idmatriculaclase.getNotasList().add(notas);
                idmatriculaclase = em.merge(idmatriculaclase);
            }
            if (idperiodo != null) {
                idperiodo.getNotasList().add(notas);
                idperiodo = em.merge(idperiodo);
            }
            if (idsubcalificacion != null) {
                idsubcalificacion.getNotasList().add(notas);
                idsubcalificacion = em.merge(idsubcalificacion);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findNotas(notas.getId()) != null) {
                throw new PreexistingEntityException("Notas " + notas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Notas notas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Notas persistentNotas = em.find(Notas.class, notas.getId());
            Matriculaclase idmatriculaclaseOld = persistentNotas.getIdmatriculaclase();
            Matriculaclase idmatriculaclaseNew = notas.getIdmatriculaclase();
            Periodo idperiodoOld = persistentNotas.getIdperiodo();
            Periodo idperiodoNew = notas.getIdperiodo();
            Subcalificacion idsubcalificacionOld = persistentNotas.getIdsubcalificacion();
            Subcalificacion idsubcalificacionNew = notas.getIdsubcalificacion();
            if (idmatriculaclaseNew != null) {
                idmatriculaclaseNew = em.getReference(idmatriculaclaseNew.getClass(), idmatriculaclaseNew.getId());
                notas.setIdmatriculaclase(idmatriculaclaseNew);
            }
            if (idperiodoNew != null) {
                idperiodoNew = em.getReference(idperiodoNew.getClass(), idperiodoNew.getId());
                notas.setIdperiodo(idperiodoNew);
            }
            if (idsubcalificacionNew != null) {
                idsubcalificacionNew = em.getReference(idsubcalificacionNew.getClass(), idsubcalificacionNew.getId());
                notas.setIdsubcalificacion(idsubcalificacionNew);
            }
            notas = em.merge(notas);
            if (idmatriculaclaseOld != null && !idmatriculaclaseOld.equals(idmatriculaclaseNew)) {
                idmatriculaclaseOld.getNotasList().remove(notas);
                idmatriculaclaseOld = em.merge(idmatriculaclaseOld);
            }
            if (idmatriculaclaseNew != null && !idmatriculaclaseNew.equals(idmatriculaclaseOld)) {
                idmatriculaclaseNew.getNotasList().add(notas);
                idmatriculaclaseNew = em.merge(idmatriculaclaseNew);
            }
            if (idperiodoOld != null && !idperiodoOld.equals(idperiodoNew)) {
                idperiodoOld.getNotasList().remove(notas);
                idperiodoOld = em.merge(idperiodoOld);
            }
            if (idperiodoNew != null && !idperiodoNew.equals(idperiodoOld)) {
                idperiodoNew.getNotasList().add(notas);
                idperiodoNew = em.merge(idperiodoNew);
            }
            if (idsubcalificacionOld != null && !idsubcalificacionOld.equals(idsubcalificacionNew)) {
                idsubcalificacionOld.getNotasList().remove(notas);
                idsubcalificacionOld = em.merge(idsubcalificacionOld);
            }
            if (idsubcalificacionNew != null && !idsubcalificacionNew.equals(idsubcalificacionOld)) {
                idsubcalificacionNew.getNotasList().add(notas);
                idsubcalificacionNew = em.merge(idsubcalificacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = notas.getId();
                if (findNotas(id) == null) {
                    throw new NonexistentEntityException("The notas with id " + id + " no longer exists.");
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
            Notas notas;
            try {
                notas = em.getReference(Notas.class, id);
                notas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The notas with id " + id + " no longer exists.", enfe);
            }
            Matriculaclase idmatriculaclase = notas.getIdmatriculaclase();
            if (idmatriculaclase != null) {
                idmatriculaclase.getNotasList().remove(notas);
                idmatriculaclase = em.merge(idmatriculaclase);
            }
            Periodo idperiodo = notas.getIdperiodo();
            if (idperiodo != null) {
                idperiodo.getNotasList().remove(notas);
                idperiodo = em.merge(idperiodo);
            }
            Subcalificacion idsubcalificacion = notas.getIdsubcalificacion();
            if (idsubcalificacion != null) {
                idsubcalificacion.getNotasList().remove(notas);
                idsubcalificacion = em.merge(idsubcalificacion);
            }
            em.remove(notas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Notas> findNotasEntities() {
        return findNotasEntities(true, -1, -1);
    }

    public List<Notas> findNotasEntities(int maxResults, int firstResult) {
        return findNotasEntities(false, maxResults, firstResult);
    }

    private List<Notas> findNotasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Notas.class));
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

    public Notas findNotas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Notas.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Notas> rt = cq.from(Notas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
