/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Clase;
import Entidades.Matricula;
import Entidades.Agendaalumno;
import Entidades.Matriculaclase;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class MatriculaclaseJpaController implements Serializable {

    
     private static EntityManagerFactory emf;
    private static EntityManager em;

    public MatriculaclaseJpaController() {

        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }
       public List<Matriculaclase> listarxclase(int clase) {

        List<Matriculaclase> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select m from Matriculaclase m where m.idclase.id=:clase",Matriculaclase.class);
            q.setParameter("clase",  clase );
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

    public void create(Matriculaclase matriculaclase) throws PreexistingEntityException, Exception {
        if (matriculaclase.getAgendaalumnoList() == null) {
            matriculaclase.setAgendaalumnoList(new ArrayList<Agendaalumno>());
        }
        try {
            em.getTransaction().begin();
            Clase idclase = matriculaclase.getIdclase();
            if (idclase != null) {
                idclase = em.getReference(idclase.getClass(), idclase.getId());
                matriculaclase.setIdclase(idclase);
            }
            Matricula idmatricula = matriculaclase.getIdmatricula();
            if (idmatricula != null) {
                idmatricula = em.getReference(idmatricula.getClass(), idmatricula.getId());
                matriculaclase.setIdmatricula(idmatricula);
            }
            List<Agendaalumno> attachedAgendaalumnoList = new ArrayList<Agendaalumno>();
            for (Agendaalumno agendaalumnoListAgendaalumnoToAttach : matriculaclase.getAgendaalumnoList()) {
                agendaalumnoListAgendaalumnoToAttach = em.getReference(agendaalumnoListAgendaalumnoToAttach.getClass(), agendaalumnoListAgendaalumnoToAttach.getId());
                attachedAgendaalumnoList.add(agendaalumnoListAgendaalumnoToAttach);
            }
            matriculaclase.setAgendaalumnoList(attachedAgendaalumnoList);
            em.persist(matriculaclase);
            if (idclase != null) {
                idclase.getMatriculaclaseList().add(matriculaclase);
                idclase = em.merge(idclase);
            }
            if (idmatricula != null) {
                idmatricula.getMatriculaclaseList().add(matriculaclase);
                idmatricula = em.merge(idmatricula);
            }
            for (Agendaalumno agendaalumnoListAgendaalumno : matriculaclase.getAgendaalumnoList()) {
                Matriculaclase oldIdmatriculaclaseOfAgendaalumnoListAgendaalumno = agendaalumnoListAgendaalumno.getIdmatriculaclase();
                agendaalumnoListAgendaalumno.setIdmatriculaclase(matriculaclase);
                agendaalumnoListAgendaalumno = em.merge(agendaalumnoListAgendaalumno);
                if (oldIdmatriculaclaseOfAgendaalumnoListAgendaalumno != null) {
                    oldIdmatriculaclaseOfAgendaalumnoListAgendaalumno.getAgendaalumnoList().remove(agendaalumnoListAgendaalumno);
                    oldIdmatriculaclaseOfAgendaalumnoListAgendaalumno = em.merge(oldIdmatriculaclaseOfAgendaalumnoListAgendaalumno);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMatriculaclase(matriculaclase.getId()) != null) {
                throw new PreexistingEntityException("Matriculaclase " + matriculaclase + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Matriculaclase matriculaclase) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Matriculaclase persistentMatriculaclase = em.find(Matriculaclase.class, matriculaclase.getId());
            Clase idclaseOld = persistentMatriculaclase.getIdclase();
            Clase idclaseNew = matriculaclase.getIdclase();
            Matricula idmatriculaOld = persistentMatriculaclase.getIdmatricula();
            Matricula idmatriculaNew = matriculaclase.getIdmatricula();
            List<Agendaalumno> agendaalumnoListOld = persistentMatriculaclase.getAgendaalumnoList();
            List<Agendaalumno> agendaalumnoListNew = matriculaclase.getAgendaalumnoList();
            List<String> illegalOrphanMessages = null;
            for (Agendaalumno agendaalumnoListOldAgendaalumno : agendaalumnoListOld) {
                if (!agendaalumnoListNew.contains(agendaalumnoListOldAgendaalumno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Agendaalumno " + agendaalumnoListOldAgendaalumno + " since its idmatriculaclase field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idclaseNew != null) {
                idclaseNew = em.getReference(idclaseNew.getClass(), idclaseNew.getId());
                matriculaclase.setIdclase(idclaseNew);
            }
            if (idmatriculaNew != null) {
                idmatriculaNew = em.getReference(idmatriculaNew.getClass(), idmatriculaNew.getId());
                matriculaclase.setIdmatricula(idmatriculaNew);
            }
            List<Agendaalumno> attachedAgendaalumnoListNew = new ArrayList<Agendaalumno>();
            for (Agendaalumno agendaalumnoListNewAgendaalumnoToAttach : agendaalumnoListNew) {
                agendaalumnoListNewAgendaalumnoToAttach = em.getReference(agendaalumnoListNewAgendaalumnoToAttach.getClass(), agendaalumnoListNewAgendaalumnoToAttach.getId());
                attachedAgendaalumnoListNew.add(agendaalumnoListNewAgendaalumnoToAttach);
            }
            agendaalumnoListNew = attachedAgendaalumnoListNew;
            matriculaclase.setAgendaalumnoList(agendaalumnoListNew);
            matriculaclase = em.merge(matriculaclase);
            if (idclaseOld != null && !idclaseOld.equals(idclaseNew)) {
                idclaseOld.getMatriculaclaseList().remove(matriculaclase);
                idclaseOld = em.merge(idclaseOld);
            }
            if (idclaseNew != null && !idclaseNew.equals(idclaseOld)) {
                idclaseNew.getMatriculaclaseList().add(matriculaclase);
                idclaseNew = em.merge(idclaseNew);
            }
            if (idmatriculaOld != null && !idmatriculaOld.equals(idmatriculaNew)) {
                idmatriculaOld.getMatriculaclaseList().remove(matriculaclase);
                idmatriculaOld = em.merge(idmatriculaOld);
            }
            if (idmatriculaNew != null && !idmatriculaNew.equals(idmatriculaOld)) {
                idmatriculaNew.getMatriculaclaseList().add(matriculaclase);
                idmatriculaNew = em.merge(idmatriculaNew);
            }
            for (Agendaalumno agendaalumnoListNewAgendaalumno : agendaalumnoListNew) {
                if (!agendaalumnoListOld.contains(agendaalumnoListNewAgendaalumno)) {
                    Matriculaclase oldIdmatriculaclaseOfAgendaalumnoListNewAgendaalumno = agendaalumnoListNewAgendaalumno.getIdmatriculaclase();
                    agendaalumnoListNewAgendaalumno.setIdmatriculaclase(matriculaclase);
                    agendaalumnoListNewAgendaalumno = em.merge(agendaalumnoListNewAgendaalumno);
                    if (oldIdmatriculaclaseOfAgendaalumnoListNewAgendaalumno != null && !oldIdmatriculaclaseOfAgendaalumnoListNewAgendaalumno.equals(matriculaclase)) {
                        oldIdmatriculaclaseOfAgendaalumnoListNewAgendaalumno.getAgendaalumnoList().remove(agendaalumnoListNewAgendaalumno);
                        oldIdmatriculaclaseOfAgendaalumnoListNewAgendaalumno = em.merge(oldIdmatriculaclaseOfAgendaalumnoListNewAgendaalumno);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = matriculaclase.getId();
                if (findMatriculaclase(id) == null) {
                    throw new NonexistentEntityException("The matriculaclase with id " + id + " no longer exists.");
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
            Matriculaclase matriculaclase;
            try {
                matriculaclase = em.getReference(Matriculaclase.class, id);
                matriculaclase.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The matriculaclase with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Agendaalumno> agendaalumnoListOrphanCheck = matriculaclase.getAgendaalumnoList();
            for (Agendaalumno agendaalumnoListOrphanCheckAgendaalumno : agendaalumnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Matriculaclase (" + matriculaclase + ") cannot be destroyed since the Agendaalumno " + agendaalumnoListOrphanCheckAgendaalumno + " in its agendaalumnoList field has a non-nullable idmatriculaclase field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Clase idclase = matriculaclase.getIdclase();
            if (idclase != null) {
                idclase.getMatriculaclaseList().remove(matriculaclase);
                idclase = em.merge(idclase);
            }
            Matricula idmatricula = matriculaclase.getIdmatricula();
            if (idmatricula != null) {
                idmatricula.getMatriculaclaseList().remove(matriculaclase);
                idmatricula = em.merge(idmatricula);
            }
            em.remove(matriculaclase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Matriculaclase> findMatriculaclaseEntities() {
        return findMatriculaclaseEntities(true, -1, -1);
    }

    public List<Matriculaclase> findMatriculaclaseEntities(int maxResults, int firstResult) {
        return findMatriculaclaseEntities(false, maxResults, firstResult);
    }

    private List<Matriculaclase> findMatriculaclaseEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Matriculaclase.class));
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

    public Matriculaclase findMatriculaclase(Integer id) {
        try {
            return em.find(Matriculaclase.class, id);
        } finally {
            em.close();
        }
    }

    public int getMatriculaclaseCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Matriculaclase> rt = cq.from(Matriculaclase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
