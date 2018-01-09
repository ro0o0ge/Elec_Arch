/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA;

import Entity.ApprovalRequest;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Users;
import Entity.Documents;
import JPA.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author norsin
 */
public class ApprovalRequestJpaController implements Serializable {

    public ApprovalRequestJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ApprovalRequest approvalRequest) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users RUserID = approvalRequest.getRUserID();
            if (RUserID != null) {
                RUserID = em.getReference(RUserID.getClass(), RUserID.getUserId());
                approvalRequest.setRUserID(RUserID);
            }
            Documents docSER = approvalRequest.getDocSER();
            if (docSER != null) {
                docSER = em.getReference(docSER.getClass(), docSER.getDocSer());
                approvalRequest.setDocSER(docSER);
            }
            em.persist(approvalRequest);
            if (RUserID != null) {
                RUserID.getApprovalRequestList().add(approvalRequest);
                RUserID = em.merge(RUserID);
            }
            if (docSER != null) {
                docSER.getApprovalRequestList().add(approvalRequest);
                docSER = em.merge(docSER);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ApprovalRequest approvalRequest) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ApprovalRequest persistentApprovalRequest = em.find(ApprovalRequest.class, approvalRequest.getAppID());
            Users RUserIDOld = persistentApprovalRequest.getRUserID();
            Users RUserIDNew = approvalRequest.getRUserID();
            Documents docSEROld = persistentApprovalRequest.getDocSER();
            Documents docSERNew = approvalRequest.getDocSER();
            if (RUserIDNew != null) {
                RUserIDNew = em.getReference(RUserIDNew.getClass(), RUserIDNew.getUserId());
                approvalRequest.setRUserID(RUserIDNew);
            }
            if (docSERNew != null) {
                docSERNew = em.getReference(docSERNew.getClass(), docSERNew.getDocSer());
                approvalRequest.setDocSER(docSERNew);
            }
            approvalRequest = em.merge(approvalRequest);
            if (RUserIDOld != null && !RUserIDOld.equals(RUserIDNew)) {
                RUserIDOld.getApprovalRequestList().remove(approvalRequest);
                RUserIDOld = em.merge(RUserIDOld);
            }
            if (RUserIDNew != null && !RUserIDNew.equals(RUserIDOld)) {
                RUserIDNew.getApprovalRequestList().add(approvalRequest);
                RUserIDNew = em.merge(RUserIDNew);
            }
            if (docSEROld != null && !docSEROld.equals(docSERNew)) {
                docSEROld.getApprovalRequestList().remove(approvalRequest);
                docSEROld = em.merge(docSEROld);
            }
            if (docSERNew != null && !docSERNew.equals(docSEROld)) {
                docSERNew.getApprovalRequestList().add(approvalRequest);
                docSERNew = em.merge(docSERNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = approvalRequest.getAppID();
                if (findApprovalRequest(id) == null) {
                    throw new NonexistentEntityException("The approvalRequest with id " + id + " no longer exists.");
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
            ApprovalRequest approvalRequest;
            try {
                approvalRequest = em.getReference(ApprovalRequest.class, id);
                approvalRequest.getAppID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The approvalRequest with id " + id + " no longer exists.", enfe);
            }
            Users RUserID = approvalRequest.getRUserID();
            if (RUserID != null) {
                RUserID.getApprovalRequestList().remove(approvalRequest);
                RUserID = em.merge(RUserID);
            }
            Documents docSER = approvalRequest.getDocSER();
            if (docSER != null) {
                docSER.getApprovalRequestList().remove(approvalRequest);
                docSER = em.merge(docSER);
            }
            em.remove(approvalRequest);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ApprovalRequest> findApprovalRequestEntities() {
        return findApprovalRequestEntities(true, -1, -1);
    }

    public List<ApprovalRequest> findApprovalRequestEntities(int maxResults, int firstResult) {
        return findApprovalRequestEntities(false, maxResults, firstResult);
    }

    private List<ApprovalRequest> findApprovalRequestEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ApprovalRequest.class));
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

    public ApprovalRequest findApprovalRequest(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ApprovalRequest.class, id);
        } finally {
            em.close();
        }
    }

    public List<ApprovalRequest> findByAppUser(int Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("ApprovalRequest.findByAPPUser").setParameter("aPPUser", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ApprovalRequest> findDocType(int rUser, String docSER, int appUser) {

        EntityManager em = getEntityManager();
       
        try {
            Query q = em.createNamedQuery("ApprovalRequest.findforDocType").setParameter("rUserID", rUser).setParameter("docSer", docSER).setParameter("appUser", appUser);
            return q.getResultList();
        }  finally {
            em.close();
        }
    }

    public int getApprovalRequestCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ApprovalRequest> rt = cq.from(ApprovalRequest.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
