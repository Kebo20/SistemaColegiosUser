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
import Entidades.Alumnoapoderado;
import java.util.ArrayList;
import java.util.List;
import Entidades.Asistencia;
import Entidades.Pago;
import Entidades.Matricula;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class AlumnoJpaController implements Serializable {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public AlumnoJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }
 public void editar(Alumno p){
        

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
 
  public  List<Alumno>  alumnosxclase(int clase) {

        List<Alumno> lista = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select a from Alumno a where a.id = (select m.idmatricula.idalumno.id from Matriculaclase m where m.idclase.id= :clase) ", Alumno.class);
           
           
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
    public void create(Alumno alumno) {
        if (alumno.getAlumnoapoderadoList() == null) {
            alumno.setAlumnoapoderadoList(new ArrayList<Alumnoapoderado>());
        }
        if (alumno.getAsistenciaList() == null) {
            alumno.setAsistenciaList(new ArrayList<Asistencia>());
        }
        if (alumno.getPagoList() == null) {
            alumno.setPagoList(new ArrayList<Pago>());
        }
        if (alumno.getMatriculaList() == null) {
            alumno.setMatriculaList(new ArrayList<Matricula>());
        }
        try {
            em.getTransaction().begin();
            List<Alumnoapoderado> attachedAlumnoapoderadoList = new ArrayList<Alumnoapoderado>();
            for (Alumnoapoderado alumnoapoderadoListAlumnoapoderadoToAttach : alumno.getAlumnoapoderadoList()) {
                alumnoapoderadoListAlumnoapoderadoToAttach = em.getReference(alumnoapoderadoListAlumnoapoderadoToAttach.getClass(), alumnoapoderadoListAlumnoapoderadoToAttach.getId());
                attachedAlumnoapoderadoList.add(alumnoapoderadoListAlumnoapoderadoToAttach);
            }
            alumno.setAlumnoapoderadoList(attachedAlumnoapoderadoList);
            List<Asistencia> attachedAsistenciaList = new ArrayList<Asistencia>();
            for (Asistencia asistenciaListAsistenciaToAttach : alumno.getAsistenciaList()) {
                asistenciaListAsistenciaToAttach = em.getReference(asistenciaListAsistenciaToAttach.getClass(), asistenciaListAsistenciaToAttach.getId());
                attachedAsistenciaList.add(asistenciaListAsistenciaToAttach);
            }
            alumno.setAsistenciaList(attachedAsistenciaList);
            List<Pago> attachedPagoList = new ArrayList<Pago>();
            for (Pago pagoListPagoToAttach : alumno.getPagoList()) {
                pagoListPagoToAttach = em.getReference(pagoListPagoToAttach.getClass(), pagoListPagoToAttach.getId());
                attachedPagoList.add(pagoListPagoToAttach);
            }
            alumno.setPagoList(attachedPagoList);
            List<Matricula> attachedMatriculaList = new ArrayList<Matricula>();
            for (Matricula matriculaListMatriculaToAttach : alumno.getMatriculaList()) {
                matriculaListMatriculaToAttach = em.getReference(matriculaListMatriculaToAttach.getClass(), matriculaListMatriculaToAttach.getId());
                attachedMatriculaList.add(matriculaListMatriculaToAttach);
            }
            alumno.setMatriculaList(attachedMatriculaList);
            em.persist(alumno);
            for (Alumnoapoderado alumnoapoderadoListAlumnoapoderado : alumno.getAlumnoapoderadoList()) {
                Alumno oldIdalumnoOfAlumnoapoderadoListAlumnoapoderado = alumnoapoderadoListAlumnoapoderado.getIdalumno();
                alumnoapoderadoListAlumnoapoderado.setIdalumno(alumno);
                alumnoapoderadoListAlumnoapoderado = em.merge(alumnoapoderadoListAlumnoapoderado);
                if (oldIdalumnoOfAlumnoapoderadoListAlumnoapoderado != null) {
                    oldIdalumnoOfAlumnoapoderadoListAlumnoapoderado.getAlumnoapoderadoList().remove(alumnoapoderadoListAlumnoapoderado);
                    oldIdalumnoOfAlumnoapoderadoListAlumnoapoderado = em.merge(oldIdalumnoOfAlumnoapoderadoListAlumnoapoderado);
                }
            }
            for (Asistencia asistenciaListAsistencia : alumno.getAsistenciaList()) {
                Alumno oldIdalumnoOfAsistenciaListAsistencia = asistenciaListAsistencia.getIdalumno();
                asistenciaListAsistencia.setIdalumno(alumno);
                asistenciaListAsistencia = em.merge(asistenciaListAsistencia);
                if (oldIdalumnoOfAsistenciaListAsistencia != null) {
                    oldIdalumnoOfAsistenciaListAsistencia.getAsistenciaList().remove(asistenciaListAsistencia);
                    oldIdalumnoOfAsistenciaListAsistencia = em.merge(oldIdalumnoOfAsistenciaListAsistencia);
                }
            }
            for (Pago pagoListPago : alumno.getPagoList()) {
                Alumno oldIdalumnoOfPagoListPago = pagoListPago.getIdalumno();
                pagoListPago.setIdalumno(alumno);
                pagoListPago = em.merge(pagoListPago);
                if (oldIdalumnoOfPagoListPago != null) {
                    oldIdalumnoOfPagoListPago.getPagoList().remove(pagoListPago);
                    oldIdalumnoOfPagoListPago = em.merge(oldIdalumnoOfPagoListPago);
                }
            }
            for (Matricula matriculaListMatricula : alumno.getMatriculaList()) {
                Alumno oldIdalumnoOfMatriculaListMatricula = matriculaListMatricula.getIdalumno();
                matriculaListMatricula.setIdalumno(alumno);
                matriculaListMatricula = em.merge(matriculaListMatricula);
                if (oldIdalumnoOfMatriculaListMatricula != null) {
                    oldIdalumnoOfMatriculaListMatricula.getMatriculaList().remove(matriculaListMatricula);
                    oldIdalumnoOfMatriculaListMatricula = em.merge(oldIdalumnoOfMatriculaListMatricula);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alumno alumno) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Alumno persistentAlumno = em.find(Alumno.class, alumno.getId());
            List<Alumnoapoderado> alumnoapoderadoListOld = persistentAlumno.getAlumnoapoderadoList();
            List<Alumnoapoderado> alumnoapoderadoListNew = alumno.getAlumnoapoderadoList();
            List<Asistencia> asistenciaListOld = persistentAlumno.getAsistenciaList();
            List<Asistencia> asistenciaListNew = alumno.getAsistenciaList();
            List<Pago> pagoListOld = persistentAlumno.getPagoList();
            List<Pago> pagoListNew = alumno.getPagoList();
            List<Matricula> matriculaListOld = persistentAlumno.getMatriculaList();
            List<Matricula> matriculaListNew = alumno.getMatriculaList();
            List<String> illegalOrphanMessages = null;
            for (Alumnoapoderado alumnoapoderadoListOldAlumnoapoderado : alumnoapoderadoListOld) {
                if (!alumnoapoderadoListNew.contains(alumnoapoderadoListOldAlumnoapoderado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alumnoapoderado " + alumnoapoderadoListOldAlumnoapoderado + " since its idalumno field is not nullable.");
                }
            }
            for (Asistencia asistenciaListOldAsistencia : asistenciaListOld) {
                if (!asistenciaListNew.contains(asistenciaListOldAsistencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Asistencia " + asistenciaListOldAsistencia + " since its idalumno field is not nullable.");
                }
            }
            for (Pago pagoListOldPago : pagoListOld) {
                if (!pagoListNew.contains(pagoListOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pago " + pagoListOldPago + " since its idalumno field is not nullable.");
                }
            }
            for (Matricula matriculaListOldMatricula : matriculaListOld) {
                if (!matriculaListNew.contains(matriculaListOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaListOldMatricula + " since its idalumno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Alumnoapoderado> attachedAlumnoapoderadoListNew = new ArrayList<Alumnoapoderado>();
            for (Alumnoapoderado alumnoapoderadoListNewAlumnoapoderadoToAttach : alumnoapoderadoListNew) {
                alumnoapoderadoListNewAlumnoapoderadoToAttach = em.getReference(alumnoapoderadoListNewAlumnoapoderadoToAttach.getClass(), alumnoapoderadoListNewAlumnoapoderadoToAttach.getId());
                attachedAlumnoapoderadoListNew.add(alumnoapoderadoListNewAlumnoapoderadoToAttach);
            }
            alumnoapoderadoListNew = attachedAlumnoapoderadoListNew;
            alumno.setAlumnoapoderadoList(alumnoapoderadoListNew);
            List<Asistencia> attachedAsistenciaListNew = new ArrayList<Asistencia>();
            for (Asistencia asistenciaListNewAsistenciaToAttach : asistenciaListNew) {
                asistenciaListNewAsistenciaToAttach = em.getReference(asistenciaListNewAsistenciaToAttach.getClass(), asistenciaListNewAsistenciaToAttach.getId());
                attachedAsistenciaListNew.add(asistenciaListNewAsistenciaToAttach);
            }
            asistenciaListNew = attachedAsistenciaListNew;
            alumno.setAsistenciaList(asistenciaListNew);
            List<Pago> attachedPagoListNew = new ArrayList<Pago>();
            for (Pago pagoListNewPagoToAttach : pagoListNew) {
                pagoListNewPagoToAttach = em.getReference(pagoListNewPagoToAttach.getClass(), pagoListNewPagoToAttach.getId());
                attachedPagoListNew.add(pagoListNewPagoToAttach);
            }
            pagoListNew = attachedPagoListNew;
            alumno.setPagoList(pagoListNew);
            List<Matricula> attachedMatriculaListNew = new ArrayList<Matricula>();
            for (Matricula matriculaListNewMatriculaToAttach : matriculaListNew) {
                matriculaListNewMatriculaToAttach = em.getReference(matriculaListNewMatriculaToAttach.getClass(), matriculaListNewMatriculaToAttach.getId());
                attachedMatriculaListNew.add(matriculaListNewMatriculaToAttach);
            }
            matriculaListNew = attachedMatriculaListNew;
            alumno.setMatriculaList(matriculaListNew);
            alumno = em.merge(alumno);
            for (Alumnoapoderado alumnoapoderadoListNewAlumnoapoderado : alumnoapoderadoListNew) {
                if (!alumnoapoderadoListOld.contains(alumnoapoderadoListNewAlumnoapoderado)) {
                    Alumno oldIdalumnoOfAlumnoapoderadoListNewAlumnoapoderado = alumnoapoderadoListNewAlumnoapoderado.getIdalumno();
                    alumnoapoderadoListNewAlumnoapoderado.setIdalumno(alumno);
                    alumnoapoderadoListNewAlumnoapoderado = em.merge(alumnoapoderadoListNewAlumnoapoderado);
                    if (oldIdalumnoOfAlumnoapoderadoListNewAlumnoapoderado != null && !oldIdalumnoOfAlumnoapoderadoListNewAlumnoapoderado.equals(alumno)) {
                        oldIdalumnoOfAlumnoapoderadoListNewAlumnoapoderado.getAlumnoapoderadoList().remove(alumnoapoderadoListNewAlumnoapoderado);
                        oldIdalumnoOfAlumnoapoderadoListNewAlumnoapoderado = em.merge(oldIdalumnoOfAlumnoapoderadoListNewAlumnoapoderado);
                    }
                }
            }
            for (Asistencia asistenciaListNewAsistencia : asistenciaListNew) {
                if (!asistenciaListOld.contains(asistenciaListNewAsistencia)) {
                    Alumno oldIdalumnoOfAsistenciaListNewAsistencia = asistenciaListNewAsistencia.getIdalumno();
                    asistenciaListNewAsistencia.setIdalumno(alumno);
                    asistenciaListNewAsistencia = em.merge(asistenciaListNewAsistencia);
                    if (oldIdalumnoOfAsistenciaListNewAsistencia != null && !oldIdalumnoOfAsistenciaListNewAsistencia.equals(alumno)) {
                        oldIdalumnoOfAsistenciaListNewAsistencia.getAsistenciaList().remove(asistenciaListNewAsistencia);
                        oldIdalumnoOfAsistenciaListNewAsistencia = em.merge(oldIdalumnoOfAsistenciaListNewAsistencia);
                    }
                }
            }
            for (Pago pagoListNewPago : pagoListNew) {
                if (!pagoListOld.contains(pagoListNewPago)) {
                    Alumno oldIdalumnoOfPagoListNewPago = pagoListNewPago.getIdalumno();
                    pagoListNewPago.setIdalumno(alumno);
                    pagoListNewPago = em.merge(pagoListNewPago);
                    if (oldIdalumnoOfPagoListNewPago != null && !oldIdalumnoOfPagoListNewPago.equals(alumno)) {
                        oldIdalumnoOfPagoListNewPago.getPagoList().remove(pagoListNewPago);
                        oldIdalumnoOfPagoListNewPago = em.merge(oldIdalumnoOfPagoListNewPago);
                    }
                }
            }
            for (Matricula matriculaListNewMatricula : matriculaListNew) {
                if (!matriculaListOld.contains(matriculaListNewMatricula)) {
                    Alumno oldIdalumnoOfMatriculaListNewMatricula = matriculaListNewMatricula.getIdalumno();
                    matriculaListNewMatricula.setIdalumno(alumno);
                    matriculaListNewMatricula = em.merge(matriculaListNewMatricula);
                    if (oldIdalumnoOfMatriculaListNewMatricula != null && !oldIdalumnoOfMatriculaListNewMatricula.equals(alumno)) {
                        oldIdalumnoOfMatriculaListNewMatricula.getMatriculaList().remove(matriculaListNewMatricula);
                        oldIdalumnoOfMatriculaListNewMatricula = em.merge(oldIdalumnoOfMatriculaListNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alumno.getId();
                if (findAlumno(id) == null) {
                    throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.");
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
            Alumno alumno;
            try {
                alumno = em.getReference(Alumno.class, id);
                alumno.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Alumnoapoderado> alumnoapoderadoListOrphanCheck = alumno.getAlumnoapoderadoList();
            for (Alumnoapoderado alumnoapoderadoListOrphanCheckAlumnoapoderado : alumnoapoderadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Alumnoapoderado " + alumnoapoderadoListOrphanCheckAlumnoapoderado + " in its alumnoapoderadoList field has a non-nullable idalumno field.");
            }
            List<Asistencia> asistenciaListOrphanCheck = alumno.getAsistenciaList();
            for (Asistencia asistenciaListOrphanCheckAsistencia : asistenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Asistencia " + asistenciaListOrphanCheckAsistencia + " in its asistenciaList field has a non-nullable idalumno field.");
            }
            List<Pago> pagoListOrphanCheck = alumno.getPagoList();
            for (Pago pagoListOrphanCheckPago : pagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Pago " + pagoListOrphanCheckPago + " in its pagoList field has a non-nullable idalumno field.");
            }
            List<Matricula> matriculaListOrphanCheck = alumno.getMatriculaList();
            for (Matricula matriculaListOrphanCheckMatricula : matriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Matricula " + matriculaListOrphanCheckMatricula + " in its matriculaList field has a non-nullable idalumno field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(alumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alumno> findAlumnoEntities() {
        return findAlumnoEntities(true, -1, -1);
    }

    public List<Alumno> findAlumnoEntities(int maxResults, int firstResult) {
        return findAlumnoEntities(false, maxResults, firstResult);
    }

    private List<Alumno> findAlumnoEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumno.class));
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

    public Alumno findAlumno(Integer id) {
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumno> rt = cq.from(Alumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
      public Alumno Login(Alumno a) {

        List<Alumno> lista = null;
        Alumno alumno = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select a from Alumno a where a.usuario = :usuario and a.contraseña = :contraseña ", Alumno.class);
            q.setParameter("usuario", a.getUsuario());
            q.setParameter("contraseña", a.getContraseña()  );
            lista = q.getResultList();
            alumno=lista.get(0);
            
            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return alumno;
    }

}
