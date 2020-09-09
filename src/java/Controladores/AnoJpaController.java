/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Entidades.Ano;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Periodo;
import java.util.ArrayList;
import java.util.List;
import Entidades.Matricula;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class AnoJpaController implements Serializable {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public AnoJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }

    public void editar(Ano a) {

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

    public void create(Ano ano) {
        if (ano.getPeriodoList() == null) {
            ano.setPeriodoList(new ArrayList<Periodo>());
        }
        if (ano.getMatriculaList() == null) {
            ano.setMatriculaList(new ArrayList<Matricula>());
        }

        try {

            em.getTransaction().begin();
            List<Periodo> attachedPeriodoList = new ArrayList<Periodo>();
            for (Periodo periodoListPeriodoToAttach : ano.getPeriodoList()) {
                periodoListPeriodoToAttach = em.getReference(periodoListPeriodoToAttach.getClass(), periodoListPeriodoToAttach.getId());
                attachedPeriodoList.add(periodoListPeriodoToAttach);
            }
            ano.setPeriodoList(attachedPeriodoList);
            List<Matricula> attachedMatriculaList = new ArrayList<Matricula>();
            for (Matricula matriculaListMatriculaToAttach : ano.getMatriculaList()) {
                matriculaListMatriculaToAttach = em.getReference(matriculaListMatriculaToAttach.getClass(), matriculaListMatriculaToAttach.getId());
                attachedMatriculaList.add(matriculaListMatriculaToAttach);
            }
            ano.setMatriculaList(attachedMatriculaList);
            em.persist(ano);
            for (Periodo periodoListPeriodo : ano.getPeriodoList()) {
                Ano oldIdanoOfPeriodoListPeriodo = periodoListPeriodo.getIdano();
                periodoListPeriodo.setIdano(ano);
                periodoListPeriodo = em.merge(periodoListPeriodo);
                if (oldIdanoOfPeriodoListPeriodo != null) {
                    oldIdanoOfPeriodoListPeriodo.getPeriodoList().remove(periodoListPeriodo);
                    oldIdanoOfPeriodoListPeriodo = em.merge(oldIdanoOfPeriodoListPeriodo);
                }
            }
            for (Matricula matriculaListMatricula : ano.getMatriculaList()) {
                Ano oldIdanoOfMatriculaListMatricula = matriculaListMatricula.getIdano();
                matriculaListMatricula.setIdano(ano);
                matriculaListMatricula = em.merge(matriculaListMatricula);
                if (oldIdanoOfMatriculaListMatricula != null) {
                    oldIdanoOfMatriculaListMatricula.getMatriculaList().remove(matriculaListMatricula);
                    oldIdanoOfMatriculaListMatricula = em.merge(oldIdanoOfMatriculaListMatricula);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ano ano) throws IllegalOrphanException, NonexistentEntityException, Exception {

        try {

            em.getTransaction().begin();
            Ano persistentAno = em.find(Ano.class, ano.getId());
            List<Periodo> periodoListOld = persistentAno.getPeriodoList();
            List<Periodo> periodoListNew = ano.getPeriodoList();
            List<Matricula> matriculaListOld = persistentAno.getMatriculaList();
            List<Matricula> matriculaListNew = ano.getMatriculaList();
            List<String> illegalOrphanMessages = null;
            for (Periodo periodoListOldPeriodo : periodoListOld) {
                if (!periodoListNew.contains(periodoListOldPeriodo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Periodo " + periodoListOldPeriodo + " since its idano field is not nullable.");
                }
            }
            for (Matricula matriculaListOldMatricula : matriculaListOld) {
                if (!matriculaListNew.contains(matriculaListOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaListOldMatricula + " since its idano field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Periodo> attachedPeriodoListNew = new ArrayList<Periodo>();
            for (Periodo periodoListNewPeriodoToAttach : periodoListNew) {
                periodoListNewPeriodoToAttach = em.getReference(periodoListNewPeriodoToAttach.getClass(), periodoListNewPeriodoToAttach.getId());
                attachedPeriodoListNew.add(periodoListNewPeriodoToAttach);
            }
            periodoListNew = attachedPeriodoListNew;
            ano.setPeriodoList(periodoListNew);
            List<Matricula> attachedMatriculaListNew = new ArrayList<Matricula>();
            for (Matricula matriculaListNewMatriculaToAttach : matriculaListNew) {
                matriculaListNewMatriculaToAttach = em.getReference(matriculaListNewMatriculaToAttach.getClass(), matriculaListNewMatriculaToAttach.getId());
                attachedMatriculaListNew.add(matriculaListNewMatriculaToAttach);
            }
            matriculaListNew = attachedMatriculaListNew;
            ano.setMatriculaList(matriculaListNew);
            ano = em.merge(ano);
            for (Periodo periodoListNewPeriodo : periodoListNew) {
                if (!periodoListOld.contains(periodoListNewPeriodo)) {
                    Ano oldIdanoOfPeriodoListNewPeriodo = periodoListNewPeriodo.getIdano();
                    periodoListNewPeriodo.setIdano(ano);
                    periodoListNewPeriodo = em.merge(periodoListNewPeriodo);
                    if (oldIdanoOfPeriodoListNewPeriodo != null && !oldIdanoOfPeriodoListNewPeriodo.equals(ano)) {
                        oldIdanoOfPeriodoListNewPeriodo.getPeriodoList().remove(periodoListNewPeriodo);
                        oldIdanoOfPeriodoListNewPeriodo = em.merge(oldIdanoOfPeriodoListNewPeriodo);
                    }
                }
            }
            for (Matricula matriculaListNewMatricula : matriculaListNew) {
                if (!matriculaListOld.contains(matriculaListNewMatricula)) {
                    Ano oldIdanoOfMatriculaListNewMatricula = matriculaListNewMatricula.getIdano();
                    matriculaListNewMatricula.setIdano(ano);
                    matriculaListNewMatricula = em.merge(matriculaListNewMatricula);
                    if (oldIdanoOfMatriculaListNewMatricula != null && !oldIdanoOfMatriculaListNewMatricula.equals(ano)) {
                        oldIdanoOfMatriculaListNewMatricula.getMatriculaList().remove(matriculaListNewMatricula);
                        oldIdanoOfMatriculaListNewMatricula = em.merge(oldIdanoOfMatriculaListNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ano.getId();
                if (findAno(id) == null) {
                    throw new NonexistentEntityException("The ano with id " + id + " no longer exists.");
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
            Ano ano;
            try {
                ano = em.getReference(Ano.class, id);
                ano.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ano with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Periodo> periodoListOrphanCheck = ano.getPeriodoList();
            for (Periodo periodoListOrphanCheckPeriodo : periodoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ano (" + ano + ") cannot be destroyed since the Periodo " + periodoListOrphanCheckPeriodo + " in its periodoList field has a non-nullable idano field.");
            }
            List<Matricula> matriculaListOrphanCheck = ano.getMatriculaList();
            for (Matricula matriculaListOrphanCheckMatricula : matriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ano (" + ano + ") cannot be destroyed since the Matricula " + matriculaListOrphanCheckMatricula + " in its matriculaList field has a non-nullable idano field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ano);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ano> findAnoEntities() {
        return findAnoEntities(true, -1, -1);
    }

    public List<Ano> findAnoEntities(int maxResults, int firstResult) {
        return findAnoEntities(false, maxResults, firstResult);
    }

    private List<Ano> findAnoEntities(boolean all, int maxResults, int firstResult) {

        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ano.class));
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

    public Ano findAno(Integer id) {

        try {
            return em.find(Ano.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnoCount() {

        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ano> rt = cq.from(Ano.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
