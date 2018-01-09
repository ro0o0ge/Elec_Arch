/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JPA;

import Entity.Attachment;
import Entity.DocHomes;
import JPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author norsin
 */
public class AttachmentJpaController implements Serializable {

    public AttachmentJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Attachment attachment) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(attachment);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Attachment attachment) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            attachment = em.merge(attachment);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = attachment.getAId();
                if (findAttachment(id) == null) {
                    throw new NonexistentEntityException("The attachment with id " + id + " no longer exists.");
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
            Attachment attachment;
            try {
                attachment = em.getReference(Attachment.class, id);
                attachment.getAId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The attachment with id " + id + " no longer exists.", enfe);
            }
            em.remove(attachment);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Attachment> findAttachmentEntities() {
        return findAttachmentEntities(true, -1, -1);
    }

    public List<Attachment> findAttachmentEntities(int maxResults, int firstResult) {
        return findAttachmentEntities(false, maxResults, firstResult);
    }

    private List<Attachment> findAttachmentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Attachment.class));
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

    public Attachment findAttachment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Attachment.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Attachment> findBydocSer(String doc) {
        EntityManager em = getEntityManager();
        List<Attachment> d=new ArrayList<>();
        try {
         
            Query q = em.createNamedQuery("Attachment.findByDocSer").setParameter("docSer", doc);
            d= q.getResultList();
        } finally {
            em.close();
        }
        return d;
    }

    public int getAttachmentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Attachment> rt = cq.from(Attachment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
