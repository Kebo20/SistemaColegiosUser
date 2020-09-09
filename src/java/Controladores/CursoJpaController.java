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
import Entidades.Area;
import Entidades.Clase;
import Entidades.Curso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class CursoJpaController implements Serializable {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public CursoJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }

    
     public List<Curso> Listarxgrado(int idgrado) {

        List<Curso> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select a from Curso a where a.idgrado.id=:grado ", Curso.class);
            q.setParameter("grado", idgrado);
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
    
     public void editar(Curso p){
        

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
    public void create(Curso curso) {
        if (curso.getClaseList() == null) {
            curso.setClaseList(new ArrayList<Clase>());
        }
        try {
            em.getTransaction().begin();
            Area idarea = curso.getIdarea();
            if (idarea != null) {
                idarea = em.getReference(idarea.getClass(), idarea.getId());
                curso.setIdarea(idarea);
            }
            List<Clase> attachedClaseList = new ArrayList<Clase>();
            for (Clase claseListClaseToAttach : curso.getClaseList()) {
                claseListClaseToAttach = em.getReference(claseListClaseToAttach.getClass(), claseListClaseToAttach.getId());
                attachedClaseList.add(claseListClaseToAttach);
            }
            curso.setClaseList(attachedClaseList);
            em.persist(curso);
            if (idarea != null) {
                idarea.getCursoList().add(curso);
                idarea = em.merge(idarea);
            }
            for (Clase claseListClase : curso.getClaseList()) {
                Curso oldIdcursoOfClaseListClase = claseListClase.getIdcurso();
                claseListClase.setIdcurso(curso);
                claseListClase = em.merge(claseListClase);
                if (oldIdcursoOfClaseListClase != null) {
                    oldIdcursoOfClaseListClase.getClaseList().remove(claseListClase);
                    oldIdcursoOfClaseListClase = em.merge(oldIdcursoOfClaseListClase);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Curso curso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Curso persistentCurso = em.find(Curso.class, curso.getId());
            Area idareaOld = persistentCurso.getIdarea();
            Area idareaNew = curso.getIdarea();
            List<Clase> claseListOld = persistentCurso.getClaseList();
            List<Clase> claseListNew = curso.getClaseList();
            List<String> illegalOrphanMessages = null;
            for (Clase claseListOldClase : claseListOld) {
                if (!claseListNew.contains(claseListOldClase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Clase " + claseListOldClase + " since its idcurso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idareaNew != null) {
                idareaNew = em.getReference(idareaNew.getClass(), idareaNew.getId());
                curso.setIdarea(idareaNew);
            }
            List<Clase> attachedClaseListNew = new ArrayList<Clase>();
            for (Clase claseListNewClaseToAttach : claseListNew) {
                claseListNewClaseToAttach = em.getReference(claseListNewClaseToAttach.getClass(), claseListNewClaseToAttach.getId());
                attachedClaseListNew.add(claseListNewClaseToAttach);
            }
            claseListNew = attachedClaseListNew;
            curso.setClaseList(claseListNew);
            curso = em.merge(curso);
            if (idareaOld != null && !idareaOld.equals(idareaNew)) {
                idareaOld.getCursoList().remove(curso);
                idareaOld = em.merge(idareaOld);
            }
            if (idareaNew != null && !idareaNew.equals(idareaOld)) {
                idareaNew.getCursoList().add(curso);
                idareaNew = em.merge(idareaNew);
            }
            for (Clase claseListNewClase : claseListNew) {
                if (!claseListOld.contains(claseListNewClase)) {
                    Curso oldIdcursoOfClaseListNewClase = claseListNewClase.getIdcurso();
                    claseListNewClase.setIdcurso(curso);
                    claseListNewClase = em.merge(claseListNewClase);
                    if (oldIdcursoOfClaseListNewClase != null && !oldIdcursoOfClaseListNewClase.equals(curso)) {
                        oldIdcursoOfClaseListNewClase.getClaseList().remove(claseListNewClase);
                        oldIdcursoOfClaseListNewClase = em.merge(oldIdcursoOfClaseListNewClase);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = curso.getId();
                if (findCurso(id) == null) {
                    throw new NonexistentEntityException("The curso with id " + id + " no longer exists.");
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
            Curso curso;
            try {
                curso = em.getReference(Curso.class, id);
                curso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The curso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Clase> claseListOrphanCheck = curso.getClaseList();
            for (Clase claseListOrphanCheckClase : claseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Curso (" + curso + ") cannot be destroyed since the Clase " + claseListOrphanCheckClase + " in its claseList field has a non-nullable idcurso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Area idarea = curso.getIdarea();
            if (idarea != null) {
                idarea.getCursoList().remove(curso);
                idarea = em.merge(idarea);
            }
            em.remove(curso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Curso> findCursoEntities() {
        return findCursoEntities(true, -1, -1);
    }

    public List<Curso> findCursoEntities(int maxResults, int firstResult) {
        return findCursoEntities(false, maxResults, firstResult);
    }

    private List<Curso> findCursoEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Curso.class));
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

    public Curso findCurso(Integer id) {
        Curso curso=null;
        try {
            
            curso=em.find(Curso.class, id);
            em.refresh(curso);
            return curso;
        } finally {
            em.close();
            
        }
    }

   
    public int getCursoCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Curso> rt = cq.from(Curso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
