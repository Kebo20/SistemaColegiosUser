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
import Entidades.Ano;
import Entidades.Matricula;
import Entidades.Seccion;
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
public class MatriculaJpaController implements Serializable {

    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    public MatriculaJpaController( ) {
        
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
        
    }
   

      public List<Matricula> listarxalumno(int idalumno) {

        List<Matricula> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select m from Matricula m where m.idalumno.id = :alumno ",Matricula.class);
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
    
    public void create(Matricula matricula) {
        if (matricula.getMatriculaclaseList() == null) {
            matricula.setMatriculaclaseList(new ArrayList<Matriculaclase>());
        }
        try {
            em.getTransaction().begin();
            Alumno idalumno = matricula.getIdalumno();
            if (idalumno != null) {
                idalumno = em.getReference(idalumno.getClass(), idalumno.getId());
                matricula.setIdalumno(idalumno);
            }
            Ano idano = matricula.getIdano();
            if (idano != null) {
                idano = em.getReference(idano.getClass(), idano.getId());
                matricula.setIdano(idano);
            }
            Seccion idseccion = matricula.getIdseccion();
            if (idseccion != null) {
                idseccion = em.getReference(idseccion.getClass(), idseccion.getId());
                matricula.setIdseccion(idseccion);
            }
            List<Matriculaclase> attachedMatriculaclaseList = new ArrayList<Matriculaclase>();
            for (Matriculaclase matriculaclaseListMatriculaclaseToAttach : matricula.getMatriculaclaseList()) {
                matriculaclaseListMatriculaclaseToAttach = em.getReference(matriculaclaseListMatriculaclaseToAttach.getClass(), matriculaclaseListMatriculaclaseToAttach.getId());
                attachedMatriculaclaseList.add(matriculaclaseListMatriculaclaseToAttach);
            }
            matricula.setMatriculaclaseList(attachedMatriculaclaseList);
            em.persist(matricula);
            if (idalumno != null) {
                idalumno.getMatriculaList().add(matricula);
                idalumno = em.merge(idalumno);
            }
            if (idano != null) {
                idano.getMatriculaList().add(matricula);
                idano = em.merge(idano);
            }
            if (idseccion != null) {
                idseccion.getMatriculaList().add(matricula);
                idseccion = em.merge(idseccion);
            }
            for (Matriculaclase matriculaclaseListMatriculaclase : matricula.getMatriculaclaseList()) {
                Matricula oldIdmatriculaOfMatriculaclaseListMatriculaclase = matriculaclaseListMatriculaclase.getIdmatricula();
                matriculaclaseListMatriculaclase.setIdmatricula(matricula);
                matriculaclaseListMatriculaclase = em.merge(matriculaclaseListMatriculaclase);
                if (oldIdmatriculaOfMatriculaclaseListMatriculaclase != null) {
                    oldIdmatriculaOfMatriculaclaseListMatriculaclase.getMatriculaclaseList().remove(matriculaclaseListMatriculaclase);
                    oldIdmatriculaOfMatriculaclaseListMatriculaclase = em.merge(oldIdmatriculaOfMatriculaclaseListMatriculaclase);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Matricula matricula) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Matricula persistentMatricula = em.find(Matricula.class, matricula.getId());
            Alumno idalumnoOld = persistentMatricula.getIdalumno();
            Alumno idalumnoNew = matricula.getIdalumno();
            Ano idanoOld = persistentMatricula.getIdano();
            Ano idanoNew = matricula.getIdano();
            Seccion idseccionOld = persistentMatricula.getIdseccion();
            Seccion idseccionNew = matricula.getIdseccion();
            List<Matriculaclase> matriculaclaseListOld = persistentMatricula.getMatriculaclaseList();
            List<Matriculaclase> matriculaclaseListNew = matricula.getMatriculaclaseList();
            List<String> illegalOrphanMessages = null;
            for (Matriculaclase matriculaclaseListOldMatriculaclase : matriculaclaseListOld) {
                if (!matriculaclaseListNew.contains(matriculaclaseListOldMatriculaclase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matriculaclase " + matriculaclaseListOldMatriculaclase + " since its idmatricula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idalumnoNew != null) {
                idalumnoNew = em.getReference(idalumnoNew.getClass(), idalumnoNew.getId());
                matricula.setIdalumno(idalumnoNew);
            }
            if (idanoNew != null) {
                idanoNew = em.getReference(idanoNew.getClass(), idanoNew.getId());
                matricula.setIdano(idanoNew);
            }
            if (idseccionNew != null) {
                idseccionNew = em.getReference(idseccionNew.getClass(), idseccionNew.getId());
                matricula.setIdseccion(idseccionNew);
            }
            List<Matriculaclase> attachedMatriculaclaseListNew = new ArrayList<Matriculaclase>();
            for (Matriculaclase matriculaclaseListNewMatriculaclaseToAttach : matriculaclaseListNew) {
                matriculaclaseListNewMatriculaclaseToAttach = em.getReference(matriculaclaseListNewMatriculaclaseToAttach.getClass(), matriculaclaseListNewMatriculaclaseToAttach.getId());
                attachedMatriculaclaseListNew.add(matriculaclaseListNewMatriculaclaseToAttach);
            }
            matriculaclaseListNew = attachedMatriculaclaseListNew;
            matricula.setMatriculaclaseList(matriculaclaseListNew);
            matricula = em.merge(matricula);
            if (idalumnoOld != null && !idalumnoOld.equals(idalumnoNew)) {
                idalumnoOld.getMatriculaList().remove(matricula);
                idalumnoOld = em.merge(idalumnoOld);
            }
            if (idalumnoNew != null && !idalumnoNew.equals(idalumnoOld)) {
                idalumnoNew.getMatriculaList().add(matricula);
                idalumnoNew = em.merge(idalumnoNew);
            }
            if (idanoOld != null && !idanoOld.equals(idanoNew)) {
                idanoOld.getMatriculaList().remove(matricula);
                idanoOld = em.merge(idanoOld);
            }
            if (idanoNew != null && !idanoNew.equals(idanoOld)) {
                idanoNew.getMatriculaList().add(matricula);
                idanoNew = em.merge(idanoNew);
            }
            if (idseccionOld != null && !idseccionOld.equals(idseccionNew)) {
                idseccionOld.getMatriculaList().remove(matricula);
                idseccionOld = em.merge(idseccionOld);
            }
            if (idseccionNew != null && !idseccionNew.equals(idseccionOld)) {
                idseccionNew.getMatriculaList().add(matricula);
                idseccionNew = em.merge(idseccionNew);
            }
            for (Matriculaclase matriculaclaseListNewMatriculaclase : matriculaclaseListNew) {
                if (!matriculaclaseListOld.contains(matriculaclaseListNewMatriculaclase)) {
                    Matricula oldIdmatriculaOfMatriculaclaseListNewMatriculaclase = matriculaclaseListNewMatriculaclase.getIdmatricula();
                    matriculaclaseListNewMatriculaclase.setIdmatricula(matricula);
                    matriculaclaseListNewMatriculaclase = em.merge(matriculaclaseListNewMatriculaclase);
                    if (oldIdmatriculaOfMatriculaclaseListNewMatriculaclase != null && !oldIdmatriculaOfMatriculaclaseListNewMatriculaclase.equals(matricula)) {
                        oldIdmatriculaOfMatriculaclaseListNewMatriculaclase.getMatriculaclaseList().remove(matriculaclaseListNewMatriculaclase);
                        oldIdmatriculaOfMatriculaclaseListNewMatriculaclase = em.merge(oldIdmatriculaOfMatriculaclaseListNewMatriculaclase);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = matricula.getId();
                if (findMatricula(id) == null) {
                    throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.");
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
            Matricula matricula;
            try {
                matricula = em.getReference(Matricula.class, id);
                matricula.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Matriculaclase> matriculaclaseListOrphanCheck = matricula.getMatriculaclaseList();
            for (Matriculaclase matriculaclaseListOrphanCheckMatriculaclase : matriculaclaseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Matricula (" + matricula + ") cannot be destroyed since the Matriculaclase " + matriculaclaseListOrphanCheckMatriculaclase + " in its matriculaclaseList field has a non-nullable idmatricula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Alumno idalumno = matricula.getIdalumno();
            if (idalumno != null) {
                idalumno.getMatriculaList().remove(matricula);
                idalumno = em.merge(idalumno);
            }
            Ano idano = matricula.getIdano();
            if (idano != null) {
                idano.getMatriculaList().remove(matricula);
                idano = em.merge(idano);
            }
            Seccion idseccion = matricula.getIdseccion();
            if (idseccion != null) {
                idseccion.getMatriculaList().remove(matricula);
                idseccion = em.merge(idseccion);
            }
            em.remove(matricula);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Matricula> findMatriculaEntities() {
        return findMatriculaEntities(true, -1, -1);
    }

    public List<Matricula> findMatriculaEntities(int maxResults, int firstResult) {
        return findMatriculaEntities(false, maxResults, firstResult);
    }

    private List<Matricula> findMatriculaEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Matricula.class));
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

    public Matricula findMatricula(Integer id) {
         Matricula matricula=null;
         EntityManager em1= null;
        try {
            em1=emf.createEntityManager();
            matricula= em1.find(Matricula.class, id);
            em1.refresh(matricula);
            return matricula;
        } finally {
            em1.close();
        }
    }

    public int getMatriculaCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Matricula> rt = cq.from(Matricula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
