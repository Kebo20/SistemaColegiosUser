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
import Entidades.Alumnoapoderado;
import Entidades.Apoderado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kevin
 */
public class ApoderadoJpaController implements Serializable {

    
     private static EntityManagerFactory emf;
    private static EntityManager em;

    public ApoderadoJpaController() {
        emf = Persistence.createEntityManagerFactory("colegiosPU");
        em = emf.createEntityManager();
    }
    
    public void create(Apoderado apoderado) {
        if (apoderado.getAlumnoapoderadoList() == null) {
            apoderado.setAlumnoapoderadoList(new ArrayList<Alumnoapoderado>());
        }
        try {

            em.getTransaction().begin();
            List<Alumnoapoderado> attachedAlumnoapoderadoList = new ArrayList<Alumnoapoderado>();
            for (Alumnoapoderado alumnoapoderadoListAlumnoapoderadoToAttach : apoderado.getAlumnoapoderadoList()) {
                alumnoapoderadoListAlumnoapoderadoToAttach = em.getReference(alumnoapoderadoListAlumnoapoderadoToAttach.getClass(), alumnoapoderadoListAlumnoapoderadoToAttach.getId());
                attachedAlumnoapoderadoList.add(alumnoapoderadoListAlumnoapoderadoToAttach);
            }
            apoderado.setAlumnoapoderadoList(attachedAlumnoapoderadoList);
            em.persist(apoderado);
            for (Alumnoapoderado alumnoapoderadoListAlumnoapoderado : apoderado.getAlumnoapoderadoList()) {
                Apoderado oldIdapoderadoOfAlumnoapoderadoListAlumnoapoderado = alumnoapoderadoListAlumnoapoderado.getIdapoderado();
                alumnoapoderadoListAlumnoapoderado.setIdapoderado(apoderado);
                alumnoapoderadoListAlumnoapoderado = em.merge(alumnoapoderadoListAlumnoapoderado);
                if (oldIdapoderadoOfAlumnoapoderadoListAlumnoapoderado != null) {
                    oldIdapoderadoOfAlumnoapoderadoListAlumnoapoderado.getAlumnoapoderadoList().remove(alumnoapoderadoListAlumnoapoderado);
                    oldIdapoderadoOfAlumnoapoderadoListAlumnoapoderado = em.merge(oldIdapoderadoOfAlumnoapoderadoListAlumnoapoderado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Apoderado apoderado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            em.getTransaction().begin();
            Apoderado persistentApoderado = em.find(Apoderado.class, apoderado.getId());
            List<Alumnoapoderado> alumnoapoderadoListOld = persistentApoderado.getAlumnoapoderadoList();
            List<Alumnoapoderado> alumnoapoderadoListNew = apoderado.getAlumnoapoderadoList();
            List<String> illegalOrphanMessages = null;
            for (Alumnoapoderado alumnoapoderadoListOldAlumnoapoderado : alumnoapoderadoListOld) {
                if (!alumnoapoderadoListNew.contains(alumnoapoderadoListOldAlumnoapoderado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alumnoapoderado " + alumnoapoderadoListOldAlumnoapoderado + " since its idapoderado field is not nullable.");
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
            apoderado.setAlumnoapoderadoList(alumnoapoderadoListNew);
            apoderado = em.merge(apoderado);
            for (Alumnoapoderado alumnoapoderadoListNewAlumnoapoderado : alumnoapoderadoListNew) {
                if (!alumnoapoderadoListOld.contains(alumnoapoderadoListNewAlumnoapoderado)) {
                    Apoderado oldIdapoderadoOfAlumnoapoderadoListNewAlumnoapoderado = alumnoapoderadoListNewAlumnoapoderado.getIdapoderado();
                    alumnoapoderadoListNewAlumnoapoderado.setIdapoderado(apoderado);
                    alumnoapoderadoListNewAlumnoapoderado = em.merge(alumnoapoderadoListNewAlumnoapoderado);
                    if (oldIdapoderadoOfAlumnoapoderadoListNewAlumnoapoderado != null && !oldIdapoderadoOfAlumnoapoderadoListNewAlumnoapoderado.equals(apoderado)) {
                        oldIdapoderadoOfAlumnoapoderadoListNewAlumnoapoderado.getAlumnoapoderadoList().remove(alumnoapoderadoListNewAlumnoapoderado);
                        oldIdapoderadoOfAlumnoapoderadoListNewAlumnoapoderado = em.merge(oldIdapoderadoOfAlumnoapoderadoListNewAlumnoapoderado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = apoderado.getId();
                if (findApoderado(id) == null) {
                    throw new NonexistentEntityException("The apoderado with id " + id + " no longer exists.");
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
            Apoderado apoderado;
            try {
                apoderado = em.getReference(Apoderado.class, id);
                apoderado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The apoderado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Alumnoapoderado> alumnoapoderadoListOrphanCheck = apoderado.getAlumnoapoderadoList();
            for (Alumnoapoderado alumnoapoderadoListOrphanCheckAlumnoapoderado : alumnoapoderadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Apoderado (" + apoderado + ") cannot be destroyed since the Alumnoapoderado " + alumnoapoderadoListOrphanCheckAlumnoapoderado + " in its alumnoapoderadoList field has a non-nullable idapoderado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(apoderado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Apoderado> findApoderadoEntities() {
        return findApoderadoEntities(true, -1, -1);
    }

    public List<Apoderado> findApoderadoEntities(int maxResults, int firstResult) {
        return findApoderadoEntities(false, maxResults, firstResult);
    }

    private List<Apoderado> findApoderadoEntities(boolean all, int maxResults, int firstResult) {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Apoderado.class));
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

    public Apoderado findApoderado(Integer id) {
        try {
            return em.find(Apoderado.class, id);
        } finally {
            em.close();
        }
    }

    public int getApoderadoCount() {
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Apoderado> rt = cq.from(Apoderado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
      public Apoderado Login(Apoderado a) {

        List<Apoderado> lista = null;
        Apoderado apoderado = null;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select a from Apoderado a where a.usuario = :usuario and a.contraseña = :contraseña ", Apoderado.class);
            q.setParameter("usuario", a.getUsuario());
            q.setParameter("contraseña", a.getContraseña()  );
            lista = q.getResultList();
            apoderado=lista.get(0);
            
            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return apoderado;
    }
      public boolean validarMatricula(int idmatricula,int idapoderado) {

       List<Apoderado> lista = null;
       boolean rpta;
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("select a from Alumnoapoderado a where a.idapoderado.id =:apoderado and a.idalumno.id in ( select m.idmatricula.idalumno.id from Matriculaclase m where m.idmatricula.id= :matricula )", Alumnoapoderado.class);
            q.setParameter("matricula", idmatricula);
            q.setParameter("apoderado", idapoderado );
            lista = q.getResultList();
            if (lista.size()>0) {
                            rpta=true;

            }else{
                            rpta=false;

            }
            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
             rpta=false;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return rpta;
    }
    
}
