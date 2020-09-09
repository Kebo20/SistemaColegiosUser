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
import Entidades.Alumno;
import Entidades.Asistencia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class AsistenciaJpaController implements Serializable {

    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    public AsistenciaJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }
   
     public void editar(Asistencia a){
        

        try {
            
            em.getTransaction().begin();
            
            em.merge(a);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
     
     
     public  List<Asistencia>  listarxclasefecha(String fecha,int clase) {

        List<Asistencia> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select a from Asistencia a where a.fecha = :fecha and a.idalumno.id in (select m.idmatricula.idalumno.id from Matriculaclase m where m.idclase.id = :clase) ", Asistencia.class);
           
            q.setParameter("fecha", fecha);
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
     
     public  List<Asistencia>  listarxalumno(int idalumno) {

        List<Asistencia> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select a from Asistencia a where a.idalumno.id=:alumno ", Asistencia.class);
           
            q.setParameter("alumno", idalumno);
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
   

    public void create(Asistencia asistencia) throws PreexistingEntityException, Exception {
        try {
            em.getTransaction().begin();
            Alumno idalumno = asistencia.getIdalumno();
            if (idalumno != null) {
                idalumno = em.getReference(idalumno.getClass(), idalumno.getId());
                asistencia.setIdalumno(idalumno);
            }
            em.persist(asistencia);
            if (idalumno != null) {
                idalumno.getAsistenciaList().add(asistencia);
                idalumno = em.merge(idalumno);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAsistencia(asistencia.getId()) != null) {
                throw new PreexistingEntityException("Asistencia " + asistencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asistencia asistencia) throws NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Asistencia persistentAsistencia = em.find(Asistencia.class, asistencia.getId());
            Alumno idalumnoOld = persistentAsistencia.getIdalumno();
            Alumno idalumnoNew = asistencia.getIdalumno();
            if (idalumnoNew != null) {
                idalumnoNew = em.getReference(idalumnoNew.getClass(), idalumnoNew.getId());
                asistencia.setIdalumno(idalumnoNew);
            }
            asistencia = em.merge(asistencia);
            if (idalumnoOld != null && !idalumnoOld.equals(idalumnoNew)) {
                idalumnoOld.getAsistenciaList().remove(asistencia);
                idalumnoOld = em.merge(idalumnoOld);
            }
            if (idalumnoNew != null && !idalumnoNew.equals(idalumnoOld)) {
                idalumnoNew.getAsistenciaList().add(asistencia);
                idalumnoNew = em.merge(idalumnoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = asistencia.getId();
                if (findAsistencia(id) == null) {
                    throw new NonexistentEntityException("The asistencia with id " + id + " no longer exists.");
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
            Asistencia asistencia;
            try {
                asistencia = em.getReference(Asistencia.class, id);
                asistencia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asistencia with id " + id + " no longer exists.", enfe);
            }
            Alumno idalumno = asistencia.getIdalumno();
            if (idalumno != null) {
                idalumno.getAsistenciaList().remove(asistencia);
                idalumno = em.merge(idalumno);
            }
            em.remove(asistencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asistencia> findAsistenciaEntities() {
        return findAsistenciaEntities(true, -1, -1);
    }

    public List<Asistencia> findAsistenciaEntities(int maxResults, int firstResult) {
        return findAsistenciaEntities(false, maxResults, firstResult);
    }

    private List<Asistencia> findAsistenciaEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asistencia.class));
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

    public Asistencia findAsistencia(String id) {
        try {
            return em.find(Asistencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsistenciaCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asistencia> rt = cq.from(Asistencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
