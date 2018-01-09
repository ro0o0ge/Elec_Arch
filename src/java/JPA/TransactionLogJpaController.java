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
import Entity.Documents;
import Entity.TransactionLog;
import Entity.Users;
import JPA.exceptions.NonexistentEntityException;
import JPA.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author norsin
 */
public class TransactionLogJpaController implements Serializable {

    public TransactionLogJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TransactionLog transactionLog) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documents docSer = transactionLog.getDocSer();
            if (docSer != null) {
                docSer = em.getReference(docSer.getClass(), docSer.getDocSer());
                transactionLog.setDocSer(docSer);
            }
            Users userId = transactionLog.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                transactionLog.setUserId(userId);
            }
            em.persist(transactionLog);
            if (docSer != null) {
                docSer.getTransactionLogList().add(transactionLog);
                docSer = em.merge(docSer);
            }
            if (userId != null) {
                userId.getTransactionLogList().add(transactionLog);
                userId = em.merge(userId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTransactionLog(transactionLog.getLogId()) != null) {
                throw new PreexistingEntityException("TransactionLog " + transactionLog + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TransactionLog transactionLog) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TransactionLog persistentTransactionLog = em.find(TransactionLog.class, transactionLog.getLogId());
            Documents docSerOld = persistentTransactionLog.getDocSer();
            Documents docSerNew = transactionLog.getDocSer();
            Users userIdOld = persistentTransactionLog.getUserId();
            Users userIdNew = transactionLog.getUserId();
            if (docSerNew != null) {
                docSerNew = em.getReference(docSerNew.getClass(), docSerNew.getDocSer());
                transactionLog.setDocSer(docSerNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                transactionLog.setUserId(userIdNew);
            }
            transactionLog = em.merge(transactionLog);
            if (docSerOld != null && !docSerOld.equals(docSerNew)) {
                docSerOld.getTransactionLogList().remove(transactionLog);
                docSerOld = em.merge(docSerOld);
            }
            if (docSerNew != null && !docSerNew.equals(docSerOld)) {
                docSerNew.getTransactionLogList().add(transactionLog);
                docSerNew = em.merge(docSerNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getTransactionLogList().remove(transactionLog);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getTransactionLogList().add(transactionLog);
                userIdNew = em.merge(userIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = transactionLog.getLogId();
                if (findTransactionLog(id) == null) {
                    throw new NonexistentEntityException("The transactionLog with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TransactionLog transactionLog;
            try {
                transactionLog = em.getReference(TransactionLog.class, id);
                transactionLog.getLogId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transactionLog with id " + id + " no longer exists.", enfe);
            }
            Documents docSer = transactionLog.getDocSer();
            if (docSer != null) {
                docSer.getTransactionLogList().remove(transactionLog);
                docSer = em.merge(docSer);
            }
            Users userId = transactionLog.getUserId();
            if (userId != null) {
                userId.getTransactionLogList().remove(transactionLog);
                userId = em.merge(userId);
            }
            em.remove(transactionLog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TransactionLog> findTransactionLogEntities() {
        return findTransactionLogEntities(true, -1, -1);
    }

    public List<TransactionLog> findTransactionLogEntities(int maxResults, int firstResult) {
        return findTransactionLogEntities(false, maxResults, firstResult);
    }

    private List<TransactionLog> findTransactionLogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TransactionLog.class));
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

    public TransactionLog findTransactionLog(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TransactionLog.class, id);
        } finally {
            em.close();
        }
    }

    public List<TransactionLog> GetDeletedLOG(String docSer) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("TransactionLog.findDeletedDoc");
        q = q.setParameter("docSer", docSer);
        return q.getResultList();
    }
    
    public List<TransactionLog> findAll( ) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("TransactionLog.findAll");
        return q.getResultList();
    }

    public int getTransactionLogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TransactionLog> rt = cq.from(TransactionLog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
