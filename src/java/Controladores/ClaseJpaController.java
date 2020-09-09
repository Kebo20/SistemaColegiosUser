/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Entidades.Alumno;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Curso;
import Entidades.Profesor;
import Entidades.Seccion;
import Entidades.Horario;
import java.util.ArrayList;
import java.util.List;
import Entidades.Matriculaclase;
import Entidades.Archivo;
import Entidades.Clase;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class ClaseJpaController implements Serializable {
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
    public ClaseJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }
    
    public void editar(Clase c) {
        
        try {
            
            em.getTransaction().begin();
            
            em.merge(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void create(Clase clase) {
        if (clase.getHorarioList() == null) {
            clase.setHorarioList(new ArrayList<Horario>());
        }
        if (clase.getMatriculaclaseList() == null) {
            clase.setMatriculaclaseList(new ArrayList<Matriculaclase>());
        }
        if (clase.getArchivoList() == null) {
            clase.setArchivoList(new ArrayList<Archivo>());
        }
        try {
            
            em.getTransaction().begin();
            Curso idcurso = clase.getIdcurso();
            if (idcurso != null) {
                idcurso = em.getReference(idcurso.getClass(), idcurso.getId());
                clase.setIdcurso(idcurso);
            }
            Profesor idprofesor = clase.getIdprofesor();
            if (idprofesor != null) {
                idprofesor = em.getReference(idprofesor.getClass(), idprofesor.getId());
                clase.setIdprofesor(idprofesor);
            }
            Seccion idseccion = clase.getIdseccion();
            if (idseccion != null) {
                idseccion = em.getReference(idseccion.getClass(), idseccion.getId());
                clase.setIdseccion(idseccion);
            }
            List<Horario> attachedHorarioList = new ArrayList<Horario>();
            for (Horario horarioListHorarioToAttach : clase.getHorarioList()) {
                horarioListHorarioToAttach = em.getReference(horarioListHorarioToAttach.getClass(), horarioListHorarioToAttach.getId());
                attachedHorarioList.add(horarioListHorarioToAttach);
            }
            clase.setHorarioList(attachedHorarioList);
            List<Matriculaclase> attachedMatriculaclaseList = new ArrayList<Matriculaclase>();
            for (Matriculaclase matriculaclaseListMatriculaclaseToAttach : clase.getMatriculaclaseList()) {
                matriculaclaseListMatriculaclaseToAttach = em.getReference(matriculaclaseListMatriculaclaseToAttach.getClass(), matriculaclaseListMatriculaclaseToAttach.getId());
                attachedMatriculaclaseList.add(matriculaclaseListMatriculaclaseToAttach);
            }
            clase.setMatriculaclaseList(attachedMatriculaclaseList);
            List<Archivo> attachedArchivoList = new ArrayList<Archivo>();
            for (Archivo archivoListArchivoToAttach : clase.getArchivoList()) {
                archivoListArchivoToAttach = em.getReference(archivoListArchivoToAttach.getClass(), archivoListArchivoToAttach.getId());
                attachedArchivoList.add(archivoListArchivoToAttach);
            }
            clase.setArchivoList(attachedArchivoList);
            em.persist(clase);
            if (idcurso != null) {
                idcurso.getClaseList().add(clase);
                idcurso = em.merge(idcurso);
            }
            if (idprofesor != null) {
                idprofesor.getClaseList().add(clase);
                idprofesor = em.merge(idprofesor);
            }
            if (idseccion != null) {
                idseccion.getClaseList().add(clase);
                idseccion = em.merge(idseccion);
            }
            for (Horario horarioListHorario : clase.getHorarioList()) {
                Clase oldIdclaseOfHorarioListHorario = horarioListHorario.getIdclase();
                horarioListHorario.setIdclase(clase);
                horarioListHorario = em.merge(horarioListHorario);
                if (oldIdclaseOfHorarioListHorario != null) {
                    oldIdclaseOfHorarioListHorario.getHorarioList().remove(horarioListHorario);
                    oldIdclaseOfHorarioListHorario = em.merge(oldIdclaseOfHorarioListHorario);
                }
            }
            for (Matriculaclase matriculaclaseListMatriculaclase : clase.getMatriculaclaseList()) {
                Clase oldIdclaseOfMatriculaclaseListMatriculaclase = matriculaclaseListMatriculaclase.getIdclase();
                matriculaclaseListMatriculaclase.setIdclase(clase);
                matriculaclaseListMatriculaclase = em.merge(matriculaclaseListMatriculaclase);
                if (oldIdclaseOfMatriculaclaseListMatriculaclase != null) {
                    oldIdclaseOfMatriculaclaseListMatriculaclase.getMatriculaclaseList().remove(matriculaclaseListMatriculaclase);
                    oldIdclaseOfMatriculaclaseListMatriculaclase = em.merge(oldIdclaseOfMatriculaclaseListMatriculaclase);
                }
            }
            for (Archivo archivoListArchivo : clase.getArchivoList()) {
                Clase oldIdclaseOfArchivoListArchivo = archivoListArchivo.getIdclase();
                archivoListArchivo.setIdclase(clase);
                archivoListArchivo = em.merge(archivoListArchivo);
                if (oldIdclaseOfArchivoListArchivo != null) {
                    oldIdclaseOfArchivoListArchivo.getArchivoList().remove(archivoListArchivo);
                    oldIdclaseOfArchivoListArchivo = em.merge(oldIdclaseOfArchivoListArchivo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void edit(Clase clase) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Clase persistentClase = em.find(Clase.class, clase.getId());
            Curso idcursoOld = persistentClase.getIdcurso();
            Curso idcursoNew = clase.getIdcurso();
            Profesor idprofesorOld = persistentClase.getIdprofesor();
            Profesor idprofesorNew = clase.getIdprofesor();
            Seccion idseccionOld = persistentClase.getIdseccion();
            Seccion idseccionNew = clase.getIdseccion();
            List<Horario> horarioListOld = persistentClase.getHorarioList();
            List<Horario> horarioListNew = clase.getHorarioList();
            List<Matriculaclase> matriculaclaseListOld = persistentClase.getMatriculaclaseList();
            List<Matriculaclase> matriculaclaseListNew = clase.getMatriculaclaseList();
            List<Archivo> archivoListOld = persistentClase.getArchivoList();
            List<Archivo> archivoListNew = clase.getArchivoList();
            List<String> illegalOrphanMessages = null;
            for (Horario horarioListOldHorario : horarioListOld) {
                if (!horarioListNew.contains(horarioListOldHorario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Horario " + horarioListOldHorario + " since its idclase field is not nullable.");
                }
            }
            for (Matriculaclase matriculaclaseListOldMatriculaclase : matriculaclaseListOld) {
                if (!matriculaclaseListNew.contains(matriculaclaseListOldMatriculaclase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matriculaclase " + matriculaclaseListOldMatriculaclase + " since its idclase field is not nullable.");
                }
            }
            for (Archivo archivoListOldArchivo : archivoListOld) {
                if (!archivoListNew.contains(archivoListOldArchivo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Archivo " + archivoListOldArchivo + " since its idclase field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idcursoNew != null) {
                idcursoNew = em.getReference(idcursoNew.getClass(), idcursoNew.getId());
                clase.setIdcurso(idcursoNew);
            }
            if (idprofesorNew != null) {
                idprofesorNew = em.getReference(idprofesorNew.getClass(), idprofesorNew.getId());
                clase.setIdprofesor(idprofesorNew);
            }
            if (idseccionNew != null) {
                idseccionNew = em.getReference(idseccionNew.getClass(), idseccionNew.getId());
                clase.setIdseccion(idseccionNew);
            }
            List<Horario> attachedHorarioListNew = new ArrayList<Horario>();
            for (Horario horarioListNewHorarioToAttach : horarioListNew) {
                horarioListNewHorarioToAttach = em.getReference(horarioListNewHorarioToAttach.getClass(), horarioListNewHorarioToAttach.getId());
                attachedHorarioListNew.add(horarioListNewHorarioToAttach);
            }
            horarioListNew = attachedHorarioListNew;
            clase.setHorarioList(horarioListNew);
            List<Matriculaclase> attachedMatriculaclaseListNew = new ArrayList<Matriculaclase>();
            for (Matriculaclase matriculaclaseListNewMatriculaclaseToAttach : matriculaclaseListNew) {
                matriculaclaseListNewMatriculaclaseToAttach = em.getReference(matriculaclaseListNewMatriculaclaseToAttach.getClass(), matriculaclaseListNewMatriculaclaseToAttach.getId());
                attachedMatriculaclaseListNew.add(matriculaclaseListNewMatriculaclaseToAttach);
            }
            matriculaclaseListNew = attachedMatriculaclaseListNew;
            clase.setMatriculaclaseList(matriculaclaseListNew);
            List<Archivo> attachedArchivoListNew = new ArrayList<Archivo>();
            for (Archivo archivoListNewArchivoToAttach : archivoListNew) {
                archivoListNewArchivoToAttach = em.getReference(archivoListNewArchivoToAttach.getClass(), archivoListNewArchivoToAttach.getId());
                attachedArchivoListNew.add(archivoListNewArchivoToAttach);
            }
            archivoListNew = attachedArchivoListNew;
            clase.setArchivoList(archivoListNew);
            clase = em.merge(clase);
            if (idcursoOld != null && !idcursoOld.equals(idcursoNew)) {
                idcursoOld.getClaseList().remove(clase);
                idcursoOld = em.merge(idcursoOld);
            }
            if (idcursoNew != null && !idcursoNew.equals(idcursoOld)) {
                idcursoNew.getClaseList().add(clase);
                idcursoNew = em.merge(idcursoNew);
            }
            if (idprofesorOld != null && !idprofesorOld.equals(idprofesorNew)) {
                idprofesorOld.getClaseList().remove(clase);
                idprofesorOld = em.merge(idprofesorOld);
            }
            if (idprofesorNew != null && !idprofesorNew.equals(idprofesorOld)) {
                idprofesorNew.getClaseList().add(clase);
                idprofesorNew = em.merge(idprofesorNew);
            }
            if (idseccionOld != null && !idseccionOld.equals(idseccionNew)) {
                idseccionOld.getClaseList().remove(clase);
                idseccionOld = em.merge(idseccionOld);
            }
            if (idseccionNew != null && !idseccionNew.equals(idseccionOld)) {
                idseccionNew.getClaseList().add(clase);
                idseccionNew = em.merge(idseccionNew);
            }
            for (Horario horarioListNewHorario : horarioListNew) {
                if (!horarioListOld.contains(horarioListNewHorario)) {
                    Clase oldIdclaseOfHorarioListNewHorario = horarioListNewHorario.getIdclase();
                    horarioListNewHorario.setIdclase(clase);
                    horarioListNewHorario = em.merge(horarioListNewHorario);
                    if (oldIdclaseOfHorarioListNewHorario != null && !oldIdclaseOfHorarioListNewHorario.equals(clase)) {
                        oldIdclaseOfHorarioListNewHorario.getHorarioList().remove(horarioListNewHorario);
                        oldIdclaseOfHorarioListNewHorario = em.merge(oldIdclaseOfHorarioListNewHorario);
                    }
                }
            }
            for (Matriculaclase matriculaclaseListNewMatriculaclase : matriculaclaseListNew) {
                if (!matriculaclaseListOld.contains(matriculaclaseListNewMatriculaclase)) {
                    Clase oldIdclaseOfMatriculaclaseListNewMatriculaclase = matriculaclaseListNewMatriculaclase.getIdclase();
                    matriculaclaseListNewMatriculaclase.setIdclase(clase);
                    matriculaclaseListNewMatriculaclase = em.merge(matriculaclaseListNewMatriculaclase);
                    if (oldIdclaseOfMatriculaclaseListNewMatriculaclase != null && !oldIdclaseOfMatriculaclaseListNewMatriculaclase.equals(clase)) {
                        oldIdclaseOfMatriculaclaseListNewMatriculaclase.getMatriculaclaseList().remove(matriculaclaseListNewMatriculaclase);
                        oldIdclaseOfMatriculaclaseListNewMatriculaclase = em.merge(oldIdclaseOfMatriculaclaseListNewMatriculaclase);
                    }
                }
            }
            for (Archivo archivoListNewArchivo : archivoListNew) {
                if (!archivoListOld.contains(archivoListNewArchivo)) {
                    Clase oldIdclaseOfArchivoListNewArchivo = archivoListNewArchivo.getIdclase();
                    archivoListNewArchivo.setIdclase(clase);
                    archivoListNewArchivo = em.merge(archivoListNewArchivo);
                    if (oldIdclaseOfArchivoListNewArchivo != null && !oldIdclaseOfArchivoListNewArchivo.equals(clase)) {
                        oldIdclaseOfArchivoListNewArchivo.getArchivoList().remove(archivoListNewArchivo);
                        oldIdclaseOfArchivoListNewArchivo = em.merge(oldIdclaseOfArchivoListNewArchivo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clase.getId();
                if (findClase(id) == null) {
                    throw new NonexistentEntityException("The clase with id " + id + " no longer exists.");
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
            Clase clase;
            try {
                clase = em.getReference(Clase.class, id);
                clase.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clase with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Horario> horarioListOrphanCheck = clase.getHorarioList();
            for (Horario horarioListOrphanCheckHorario : horarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clase (" + clase + ") cannot be destroyed since the Horario " + horarioListOrphanCheckHorario + " in its horarioList field has a non-nullable idclase field.");
            }
            List<Matriculaclase> matriculaclaseListOrphanCheck = clase.getMatriculaclaseList();
            for (Matriculaclase matriculaclaseListOrphanCheckMatriculaclase : matriculaclaseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clase (" + clase + ") cannot be destroyed since the Matriculaclase " + matriculaclaseListOrphanCheckMatriculaclase + " in its matriculaclaseList field has a non-nullable idclase field.");
            }
            List<Archivo> archivoListOrphanCheck = clase.getArchivoList();
            for (Archivo archivoListOrphanCheckArchivo : archivoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clase (" + clase + ") cannot be destroyed since the Archivo " + archivoListOrphanCheckArchivo + " in its archivoList field has a non-nullable idclase field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Curso idcurso = clase.getIdcurso();
            if (idcurso != null) {
                idcurso.getClaseList().remove(clase);
                idcurso = em.merge(idcurso);
            }
            Profesor idprofesor = clase.getIdprofesor();
            if (idprofesor != null) {
                idprofesor.getClaseList().remove(clase);
                idprofesor = em.merge(idprofesor);
            }
            Seccion idseccion = clase.getIdseccion();
            if (idseccion != null) {
                idseccion.getClaseList().remove(clase);
                idseccion = em.merge(idseccion);
            }
            em.remove(clase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<Clase> findClaseEntities() {
        return findClaseEntities(true, -1, -1);
    }
    
    public List<Clase> findClaseEntities(int maxResults, int firstResult) {
        return findClaseEntities(false, maxResults, firstResult);
    }
    
    private List<Clase> findClaseEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clase.class));
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
    
    public Clase findClase(Integer id) {
        try {
            return em.find(Clase.class, id);
        } finally {
            em.close();
        }
    }
    
    public int getClaseCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clase> rt = cq.from(Clase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Clase> listarxprofesor(Profesor a, int idaño) {
        
        List<Clase> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select c from Clase c where c.idprofesor.id= :profesor and c.idano.id = :año", Clase.class);
            q.setParameter("profesor", a.getId());
            q.setParameter("año", idaño);
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
    
    public List<Clase> listarxalumno(int idalumno, int idaño) {
        
        List<Clase> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select c from Clase c where c.id in (select m.idclase.id from Matriculaclase m where m.idmatricula.idalumno.id =:alumno ) and c.idano.id =:idaño", Clase.class);
            q.setParameter("alumno", idalumno);
            q.setParameter("idaño", idaño);
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
    
    public List<Clase> listarxmatricula(int idmatricula) {
        
        List<Clase> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select c from Clase c where c.id in (select m.idclase.id from Matriculaclase m where m.idmatricula.id= :matricula)", Clase.class);
            q.setParameter("matricula", idmatricula);
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
    
}
