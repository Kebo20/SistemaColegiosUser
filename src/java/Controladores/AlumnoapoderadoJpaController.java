/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Alumno;
import Entidades.Alumnoapoderado;
import Entidades.Apoderado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class AlumnoapoderadoJpaController implements Serializable {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public AlumnoapoderadoJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }

    
    
     public  List<Alumnoapoderado>  alumnosxapoderado(int apoderado) {

        List<Alumnoapoderado> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select a from Alumnoapoderado a where a.idapoderado.id=:apoderado ", Alumnoapoderado.class);
           
           
            q.setParameter("apoderado", apoderado);
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
    public void create(Alumnoapoderado alumnoapoderado) {
        try {
            em.getTransaction().begin();
            Alumno idalumno = alumnoapoderado.getIdalumno();
            if (idalumno != null) {
                idalumno = em.getReference(idalumno.getClass(), idalumno.getId());
                alumnoapoderado.setIdalumno(idalumno);
            }
            Apoderado idapoderado = alumnoapoderado.getIdapoderado();
            if (idapoderado != null) {
                idapoderado = em.getReference(idapoderado.getClass(), idapoderado.getId());
                alumnoapoderado.setIdapoderado(idapoderado);
            }
            em.persist(alumnoapoderado);
            if (idalumno != null) {
                idalumno.getAlumnoapoderadoList().add(alumnoapoderado);
                idalumno = em.merge(idalumno);
            }
            if (idapoderado != null) {
                idapoderado.getAlumnoapoderadoList().add(alumnoapoderado);
                idapoderado = em.merge(idapoderado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alumnoapoderado alumnoapoderado) throws NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Alumnoapoderado persistentAlumnoapoderado = em.find(Alumnoapoderado.class, alumnoapoderado.getId());
            Alumno idalumnoOld = persistentAlumnoapoderado.getIdalumno();
            Alumno idalumnoNew = alumnoapoderado.getIdalumno();
            Apoderado idapoderadoOld = persistentAlumnoapoderado.getIdapoderado();
            Apoderado idapoderadoNew = alumnoapoderado.getIdapoderado();
            if (idalumnoNew != null) {
                idalumnoNew = em.getReference(idalumnoNew.getClass(), idalumnoNew.getId());
                alumnoapoderado.setIdalumno(idalumnoNew);
            }
            if (idapoderadoNew != null) {
                idapoderadoNew = em.getReference(idapoderadoNew.getClass(), idapoderadoNew.getId());
                alumnoapoderado.setIdapoderado(idapoderadoNew);
            }
            alumnoapoderado = em.merge(alumnoapoderado);
            if (idalumnoOld != null && !idalumnoOld.equals(idalumnoNew)) {
                idalumnoOld.getAlumnoapoderadoList().remove(alumnoapoderado);
                idalumnoOld = em.merge(idalumnoOld);
            }
            if (idalumnoNew != null && !idalumnoNew.equals(idalumnoOld)) {
                idalumnoNew.getAlumnoapoderadoList().add(alumnoapoderado);
                idalumnoNew = em.merge(idalumnoNew);
            }
            if (idapoderadoOld != null && !idapoderadoOld.equals(idapoderadoNew)) {
                idapoderadoOld.getAlumnoapoderadoList().remove(alumnoapoderado);
                idapoderadoOld = em.merge(idapoderadoOld);
            }
            if (idapoderadoNew != null && !idapoderadoNew.equals(idapoderadoOld)) {
                idapoderadoNew.getAlumnoapoderadoList().add(alumnoapoderado);
                idapoderadoNew = em.merge(idapoderadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alumnoapoderado.getId();
                if (findAlumnoapoderado(id) == null) {
                    throw new NonexistentEntityException("The alumnoapoderado with id " + id + " no longer exists.");
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
            Alumnoapoderado alumnoapoderado;
            try {
                alumnoapoderado = em.getReference(Alumnoapoderado.class, id);
                alumnoapoderado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumnoapoderado with id " + id + " no longer exists.", enfe);
            }
            Alumno idalumno = alumnoapoderado.getIdalumno();
            if (idalumno != null) {
                idalumno.getAlumnoapoderadoList().remove(alumnoapoderado);
                idalumno = em.merge(idalumno);
            }
            Apoderado idapoderado = alumnoapoderado.getIdapoderado();
            if (idapoderado != null) {
                idapoderado.getAlumnoapoderadoList().remove(alumnoapoderado);
                idapoderado = em.merge(idapoderado);
            }
            em.remove(alumnoapoderado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alumnoapoderado> findAlumnoapoderadoEntities() {
        return findAlumnoapoderadoEntities(true, -1, -1);
    }

    public List<Alumnoapoderado> findAlumnoapoderadoEntities(int maxResults, int firstResult) {
        return findAlumnoapoderadoEntities(false, maxResults, firstResult);
    }

    private List<Alumnoapoderado> findAlumnoapoderadoEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumnoapoderado.class));
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

    public Alumnoapoderado findAlumnoapoderado(Integer id) {
        try {
            return em.find(Alumnoapoderado.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoapoderadoCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumnoapoderado> rt = cq.from(Alumnoapoderado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
