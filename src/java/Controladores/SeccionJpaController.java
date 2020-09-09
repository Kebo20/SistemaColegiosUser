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
import Entidades.Grado;
import Entidades.Clase;
import java.util.ArrayList;
import java.util.List;
import Entidades.Matricula;
import Entidades.Seccion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class SeccionJpaController implements Serializable {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public SeccionJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }

    public void create(Seccion seccion) {
        if (seccion.getClaseList() == null) {
            seccion.setClaseList(new ArrayList<Clase>());
        }
        if (seccion.getMatriculaList() == null) {
            seccion.setMatriculaList(new ArrayList<Matricula>());
        }
        try {
            em.getTransaction().begin();
            Grado idgrado = seccion.getIdgrado();
            if (idgrado != null) {
                idgrado = em.getReference(idgrado.getClass(), idgrado.getId());
                seccion.setIdgrado(idgrado);
            }
            List<Clase> attachedClaseList = new ArrayList<Clase>();
            for (Clase claseListClaseToAttach : seccion.getClaseList()) {
                claseListClaseToAttach = em.getReference(claseListClaseToAttach.getClass(), claseListClaseToAttach.getId());
                attachedClaseList.add(claseListClaseToAttach);
            }
            seccion.setClaseList(attachedClaseList);
            List<Matricula> attachedMatriculaList = new ArrayList<Matricula>();
            for (Matricula matriculaListMatriculaToAttach : seccion.getMatriculaList()) {
                matriculaListMatriculaToAttach = em.getReference(matriculaListMatriculaToAttach.getClass(), matriculaListMatriculaToAttach.getId());
                attachedMatriculaList.add(matriculaListMatriculaToAttach);
            }
            seccion.setMatriculaList(attachedMatriculaList);
            em.persist(seccion);
            if (idgrado != null) {
                idgrado.getSeccionList().add(seccion);
                idgrado = em.merge(idgrado);
            }
            for (Clase claseListClase : seccion.getClaseList()) {
                Seccion oldIdseccionOfClaseListClase = claseListClase.getIdseccion();
                claseListClase.setIdseccion(seccion);
                claseListClase = em.merge(claseListClase);
                if (oldIdseccionOfClaseListClase != null) {
                    oldIdseccionOfClaseListClase.getClaseList().remove(claseListClase);
                    oldIdseccionOfClaseListClase = em.merge(oldIdseccionOfClaseListClase);
                }
            }
            for (Matricula matriculaListMatricula : seccion.getMatriculaList()) {
                Seccion oldIdseccionOfMatriculaListMatricula = matriculaListMatricula.getIdseccion();
                matriculaListMatricula.setIdseccion(seccion);
                matriculaListMatricula = em.merge(matriculaListMatricula);
                if (oldIdseccionOfMatriculaListMatricula != null) {
                    oldIdseccionOfMatriculaListMatricula.getMatriculaList().remove(matriculaListMatricula);
                    oldIdseccionOfMatriculaListMatricula = em.merge(oldIdseccionOfMatriculaListMatricula);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Seccion seccion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Seccion persistentSeccion = em.find(Seccion.class, seccion.getId());
            Grado idgradoOld = persistentSeccion.getIdgrado();
            Grado idgradoNew = seccion.getIdgrado();
            List<Clase> claseListOld = persistentSeccion.getClaseList();
            List<Clase> claseListNew = seccion.getClaseList();
            List<Matricula> matriculaListOld = persistentSeccion.getMatriculaList();
            List<Matricula> matriculaListNew = seccion.getMatriculaList();
            List<String> illegalOrphanMessages = null;
            for (Clase claseListOldClase : claseListOld) {
                if (!claseListNew.contains(claseListOldClase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Clase " + claseListOldClase + " since its idseccion field is not nullable.");
                }
            }
            for (Matricula matriculaListOldMatricula : matriculaListOld) {
                if (!matriculaListNew.contains(matriculaListOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaListOldMatricula + " since its idseccion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idgradoNew != null) {
                idgradoNew = em.getReference(idgradoNew.getClass(), idgradoNew.getId());
                seccion.setIdgrado(idgradoNew);
            }
            List<Clase> attachedClaseListNew = new ArrayList<Clase>();
            for (Clase claseListNewClaseToAttach : claseListNew) {
                claseListNewClaseToAttach = em.getReference(claseListNewClaseToAttach.getClass(), claseListNewClaseToAttach.getId());
                attachedClaseListNew.add(claseListNewClaseToAttach);
            }
            claseListNew = attachedClaseListNew;
            seccion.setClaseList(claseListNew);
            List<Matricula> attachedMatriculaListNew = new ArrayList<Matricula>();
            for (Matricula matriculaListNewMatriculaToAttach : matriculaListNew) {
                matriculaListNewMatriculaToAttach = em.getReference(matriculaListNewMatriculaToAttach.getClass(), matriculaListNewMatriculaToAttach.getId());
                attachedMatriculaListNew.add(matriculaListNewMatriculaToAttach);
            }
            matriculaListNew = attachedMatriculaListNew;
            seccion.setMatriculaList(matriculaListNew);
            seccion = em.merge(seccion);
            if (idgradoOld != null && !idgradoOld.equals(idgradoNew)) {
                idgradoOld.getSeccionList().remove(seccion);
                idgradoOld = em.merge(idgradoOld);
            }
            if (idgradoNew != null && !idgradoNew.equals(idgradoOld)) {
                idgradoNew.getSeccionList().add(seccion);
                idgradoNew = em.merge(idgradoNew);
            }
            for (Clase claseListNewClase : claseListNew) {
                if (!claseListOld.contains(claseListNewClase)) {
                    Seccion oldIdseccionOfClaseListNewClase = claseListNewClase.getIdseccion();
                    claseListNewClase.setIdseccion(seccion);
                    claseListNewClase = em.merge(claseListNewClase);
                    if (oldIdseccionOfClaseListNewClase != null && !oldIdseccionOfClaseListNewClase.equals(seccion)) {
                        oldIdseccionOfClaseListNewClase.getClaseList().remove(claseListNewClase);
                        oldIdseccionOfClaseListNewClase = em.merge(oldIdseccionOfClaseListNewClase);
                    }
                }
            }
            for (Matricula matriculaListNewMatricula : matriculaListNew) {
                if (!matriculaListOld.contains(matriculaListNewMatricula)) {
                    Seccion oldIdseccionOfMatriculaListNewMatricula = matriculaListNewMatricula.getIdseccion();
                    matriculaListNewMatricula.setIdseccion(seccion);
                    matriculaListNewMatricula = em.merge(matriculaListNewMatricula);
                    if (oldIdseccionOfMatriculaListNewMatricula != null && !oldIdseccionOfMatriculaListNewMatricula.equals(seccion)) {
                        oldIdseccionOfMatriculaListNewMatricula.getMatriculaList().remove(matriculaListNewMatricula);
                        oldIdseccionOfMatriculaListNewMatricula = em.merge(oldIdseccionOfMatriculaListNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = seccion.getId();
                if (findSeccion(id) == null) {
                    throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.");
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
            Seccion seccion;
            try {
                seccion = em.getReference(Seccion.class, id);
                seccion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Clase> claseListOrphanCheck = seccion.getClaseList();
            for (Clase claseListOrphanCheckClase : claseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Seccion (" + seccion + ") cannot be destroyed since the Clase " + claseListOrphanCheckClase + " in its claseList field has a non-nullable idseccion field.");
            }
            List<Matricula> matriculaListOrphanCheck = seccion.getMatriculaList();
            for (Matricula matriculaListOrphanCheckMatricula : matriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Seccion (" + seccion + ") cannot be destroyed since the Matricula " + matriculaListOrphanCheckMatricula + " in its matriculaList field has a non-nullable idseccion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Grado idgrado = seccion.getIdgrado();
            if (idgrado != null) {
                idgrado.getSeccionList().remove(seccion);
                idgrado = em.merge(idgrado);
            }
            em.remove(seccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Seccion> findSeccionEntities() {
        return findSeccionEntities(true, -1, -1);
    }

    public List<Seccion> findSeccionEntities(int maxResults, int firstResult) {
        return findSeccionEntities(false, maxResults, firstResult);
    }

    private List<Seccion> findSeccionEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Seccion.class));
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

    public Seccion findSeccion(Integer id) {
        try {
            return em.find(Seccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeccionCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Seccion> rt = cq.from(Seccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
