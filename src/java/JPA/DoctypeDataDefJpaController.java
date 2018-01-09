/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.DocTypes;
import Entity.DoctypeDataDef;
import JPA.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author norsin
 */
public class DoctypeDataDefJpaController implements Serializable {

    public DoctypeDataDefJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DoctypeDataDef doctypeDataDef) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            DocTypes doctypeId = doctypeDataDef.getDoctypeId();
            if (doctypeId != null) {
                doctypeId = em.getReference(doctypeId.getClass(), doctypeId.getDoctypeId());
                doctypeDataDef.setDoctypeId(doctypeId);
            }
            em.persist(doctypeDataDef);
            if (doctypeId != null) {
                doctypeId.getDoctypeDataDefList().add(doctypeDataDef);
                doctypeId = em.merge(doctypeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DoctypeDataDef doctypeDataDef) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DoctypeDataDef persistentDoctypeDataDef = em.find(DoctypeDataDef.class, doctypeDataDef.getDataDefID());
            DocTypes doctypeIdOld = persistentDoctypeDataDef.getDoctypeId();
            DocTypes doctypeIdNew = doctypeDataDef.getDoctypeId();
            if (doctypeIdNew != null) {
                doctypeIdNew = em.getReference(doctypeIdNew.getClass(), doctypeIdNew.getDoctypeId());
                doctypeDataDef.setDoctypeId(doctypeIdNew);
            }
            doctypeDataDef = em.merge(doctypeDataDef);
            if (doctypeIdOld != null && !doctypeIdOld.equals(doctypeIdNew)) {
                doctypeIdOld.getDoctypeDataDefList().remove(doctypeDataDef);
                doctypeIdOld = em.merge(doctypeIdOld);
            }
            if (doctypeIdNew != null && !doctypeIdNew.equals(doctypeIdOld)) {
                doctypeIdNew.getDoctypeDataDefList().add(doctypeDataDef);
                doctypeIdNew = em.merge(doctypeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = doctypeDataDef.getDataDefID();
                if (findDoctypeDataDef(id) == null) {
                    throw new NonexistentEntityException("The doctypeDataDef with id " + id + " no longer exists.");
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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DoctypeDataDef doctypeDataDef;
            try {
                doctypeDataDef = em.getReference(DoctypeDataDef.class, id);
                doctypeDataDef.getDataDefID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The doctypeDataDef with id " + id + " no longer exists.", enfe);
            }
            DocTypes doctypeId = doctypeDataDef.getDoctypeId();
            if (doctypeId != null) {
                doctypeId.getDoctypeDataDefList().remove(doctypeDataDef);
                doctypeId = em.merge(doctypeId);
            }
            em.remove(doctypeDataDef);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DoctypeDataDef> findDoctypeDataDefEntities() {
        return findDoctypeDataDefEntities(true, -1, -1);
    }

    public List<DoctypeDataDef> findDoctypeDataDefEntities(int maxResults, int firstResult) {
        return findDoctypeDataDefEntities(false, maxResults, firstResult);
    }

    private List<DoctypeDataDef> findDoctypeDataDefEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DoctypeDataDef.class));
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

    public DoctypeDataDef findDoctypeDataDef(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DoctypeDataDef.class, id);
        } finally {
            em.close();
        }
    }

    public DoctypeDataDef findByLength(String length) {
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("DoctypeDataDef.findByFieldLength");
        DoctypeDataDef data = null;
        try {
            q = q.setParameter("fieldLength", length);
            data = (DoctypeDataDef) q.getSingleResult();
            
        } finally {
            em.close();
        }

        return data;
    }

    public DoctypeDataDef findDoctypeDataDef(String fieldName) {
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("DoctypeDataDef.findByFieldName");
        DoctypeDataDef data = null;
        try {
            return (DoctypeDataDef) q.setParameter("fieldName", fieldName).getResultList().get(0);

        } finally {
            em.close();
        }

//        return data;
    }

    public List<DoctypeDataDef> DocTypeDataDef(int Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DoctypeDataDef.findByDocTypeID").setParameter("DocTypeID", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getDoctypeDataDefCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DoctypeDataDef> rt = cq.from(DoctypeDataDef.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public boolean isExist(DocTypes doctypeID, String fieldName) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("DoctypeDataDef.findByDocTypeIDFieldName");
        q = q.setParameter("doctypeID", doctypeID).setParameter("fieldName", fieldName);

        try {
//            List<DoctypeDataDef> docList=q.getResultList();
//            for(DoctypeDataDef data:docList){
//                if(data.getFieldName().equals(fieldName)){
//                    return true;
//                }
            q.getSingleResult();
            return true;

        } catch (Exception e) {
            return false;
        }

    }

}
