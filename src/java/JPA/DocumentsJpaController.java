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
import Entity.DocOwners;
import Entity.DocHomes;
import Entity.Users;
import Entity.DocumentData;
import java.util.ArrayList;
import java.util.List;
import Entity.TransactionLog;
import Entity.ApprovalRequest;
import Entity.Documents;
import JPA.exceptions.IllegalOrphanException;
import JPA.exceptions.NonexistentEntityException;
import JPA.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author norsin
 */
public class DocumentsJpaController implements Serializable {

    public DocumentsJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Documents documents) throws PreexistingEntityException, Exception {
        if (documents.getDocumentDataList() == null) {
            documents.setDocumentDataList(new ArrayList<DocumentData>());
        }
        if (documents.getTransactionLogList() == null) {
            documents.setTransactionLogList(new ArrayList<TransactionLog>());
        }
        if (documents.getApprovalRequestList() == null) {
            documents.setApprovalRequestList(new ArrayList<ApprovalRequest>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocTypes doctypeId = documents.getDoctypeId();
            if (doctypeId != null) {
                doctypeId = em.getReference(doctypeId.getClass(), doctypeId.getDoctypeId());
                documents.setDoctypeId(doctypeId);
            }
            DocOwners ownerId = documents.getOwnerId();
            if (ownerId != null) {
                ownerId = em.getReference(ownerId.getClass(), ownerId.getOwnerId());
                documents.setOwnerId(ownerId);
            }
            DocHomes homeId = documents.getHomeId();
            if (homeId != null) {
                homeId = em.getReference(homeId.getClass(), homeId.getHomeId());
                documents.setHomeId(homeId);
            }
            Users userId = documents.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                documents.setUserId(userId);
            }
            List<DocumentData> attachedDocumentDataList = new ArrayList<DocumentData>();
            for (DocumentData documentDataListDocumentDataToAttach : documents.getDocumentDataList()) {
                documentDataListDocumentDataToAttach = em.getReference(documentDataListDocumentDataToAttach.getClass(), documentDataListDocumentDataToAttach.getDataID());
                attachedDocumentDataList.add(documentDataListDocumentDataToAttach);
            }
            documents.setDocumentDataList(attachedDocumentDataList);
            List<TransactionLog> attachedTransactionLogList = new ArrayList<TransactionLog>();
            for (TransactionLog transactionLogListTransactionLogToAttach : documents.getTransactionLogList()) {
                transactionLogListTransactionLogToAttach = em.getReference(transactionLogListTransactionLogToAttach.getClass(), transactionLogListTransactionLogToAttach.getLogId());
                attachedTransactionLogList.add(transactionLogListTransactionLogToAttach);
            }
            documents.setTransactionLogList(attachedTransactionLogList);
            List<ApprovalRequest> attachedApprovalRequestList = new ArrayList<ApprovalRequest>();
            for (ApprovalRequest approvalRequestListApprovalRequestToAttach : documents.getApprovalRequestList()) {
                approvalRequestListApprovalRequestToAttach = em.getReference(approvalRequestListApprovalRequestToAttach.getClass(), approvalRequestListApprovalRequestToAttach.getAppID());
                attachedApprovalRequestList.add(approvalRequestListApprovalRequestToAttach);
            }
            documents.setApprovalRequestList(attachedApprovalRequestList);
            em.persist(documents);
            if (doctypeId != null) {
                doctypeId.getDocumentsList().add(documents);
                doctypeId = em.merge(doctypeId);
            }
            if (ownerId != null) {
                ownerId.getDocumentsList().add(documents);
                ownerId = em.merge(ownerId);
            }
            if (homeId != null) {
                homeId.getDocumentsList().add(documents);
                homeId = em.merge(homeId);
            }
            if (userId != null) {
                userId.getDocumentsList().add(documents);
                userId = em.merge(userId);
            }
            for (DocumentData documentDataListDocumentData : documents.getDocumentDataList()) {
                Documents oldDocSerOfDocumentDataListDocumentData = documentDataListDocumentData.getDocSer();
                documentDataListDocumentData.setDocSer(documents);
                documentDataListDocumentData = em.merge(documentDataListDocumentData);
                if (oldDocSerOfDocumentDataListDocumentData != null) {
                    oldDocSerOfDocumentDataListDocumentData.getDocumentDataList().remove(documentDataListDocumentData);
                    oldDocSerOfDocumentDataListDocumentData = em.merge(oldDocSerOfDocumentDataListDocumentData);
                }
            }
            for (TransactionLog transactionLogListTransactionLog : documents.getTransactionLogList()) {
                Documents oldDocSerOfTransactionLogListTransactionLog = transactionLogListTransactionLog.getDocSer();
                transactionLogListTransactionLog.setDocSer(documents);
                transactionLogListTransactionLog = em.merge(transactionLogListTransactionLog);
                if (oldDocSerOfTransactionLogListTransactionLog != null) {
                    oldDocSerOfTransactionLogListTransactionLog.getTransactionLogList().remove(transactionLogListTransactionLog);
                    oldDocSerOfTransactionLogListTransactionLog = em.merge(oldDocSerOfTransactionLogListTransactionLog);
                }
            }
            for (ApprovalRequest approvalRequestListApprovalRequest : documents.getApprovalRequestList()) {
                Documents oldDocSEROfApprovalRequestListApprovalRequest = approvalRequestListApprovalRequest.getDocSER();
                approvalRequestListApprovalRequest.setDocSER(documents);
                approvalRequestListApprovalRequest = em.merge(approvalRequestListApprovalRequest);
                if (oldDocSEROfApprovalRequestListApprovalRequest != null) {
                    oldDocSEROfApprovalRequestListApprovalRequest.getApprovalRequestList().remove(approvalRequestListApprovalRequest);
                    oldDocSEROfApprovalRequestListApprovalRequest = em.merge(oldDocSEROfApprovalRequestListApprovalRequest);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDocuments(documents.getDocSer()) != null) {
                throw new PreexistingEntityException("Documents " + documents + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editDelete(Documents documents) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(documents);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documents documents) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documents persistentDocuments = em.find(Documents.class, documents.getDocSer());
            DocTypes doctypeIdOld = persistentDocuments.getDoctypeId();
            DocTypes doctypeIdNew = documents.getDoctypeId();
            DocOwners ownerIdOld = persistentDocuments.getOwnerId();
            DocOwners ownerIdNew = documents.getOwnerId();
            DocHomes homeIdOld = persistentDocuments.getHomeId();
            DocHomes homeIdNew = documents.getHomeId();
            Users userIdOld = persistentDocuments.getUserId();
            Users userIdNew = documents.getUserId();
            List<DocumentData> documentDataListOld = persistentDocuments.getDocumentDataList();
            List<DocumentData> documentDataListNew = documents.getDocumentDataList();
            List<TransactionLog> transactionLogListOld = persistentDocuments.getTransactionLogList();
            List<TransactionLog> transactionLogListNew = documents.getTransactionLogList();
            List<ApprovalRequest> approvalRequestListOld = persistentDocuments.getApprovalRequestList();
            List<ApprovalRequest> approvalRequestListNew = documents.getApprovalRequestList();
            List<String> illegalOrphanMessages = null;
            for (DocumentData documentDataListOldDocumentData : documentDataListOld) {
                if (!documentDataListNew.contains(documentDataListOldDocumentData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DocumentData " + documentDataListOldDocumentData + " since its docSer field is not nullable.");
                }
            }
            for (ApprovalRequest approvalRequestListOldApprovalRequest : approvalRequestListOld) {
                if (!approvalRequestListNew.contains(approvalRequestListOldApprovalRequest)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ApprovalRequest " + approvalRequestListOldApprovalRequest + " since its docSER field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (doctypeIdNew != null) {
                doctypeIdNew = em.getReference(doctypeIdNew.getClass(), doctypeIdNew.getDoctypeId());
                documents.setDoctypeId(doctypeIdNew);
            }
            if (ownerIdNew != null) {
                ownerIdNew = em.getReference(ownerIdNew.getClass(), ownerIdNew.getOwnerId());
                documents.setOwnerId(ownerIdNew);
            }
            if (homeIdNew != null) {
                homeIdNew = em.getReference(homeIdNew.getClass(), homeIdNew.getHomeId());
                documents.setHomeId(homeIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                documents.setUserId(userIdNew);
            }
            List<DocumentData> attachedDocumentDataListNew = new ArrayList<DocumentData>();
            for (DocumentData documentDataListNewDocumentDataToAttach : documentDataListNew) {
                documentDataListNewDocumentDataToAttach = em.getReference(documentDataListNewDocumentDataToAttach.getClass(), documentDataListNewDocumentDataToAttach.getDataID());
                attachedDocumentDataListNew.add(documentDataListNewDocumentDataToAttach);
            }
            documentDataListNew = attachedDocumentDataListNew;
            documents.setDocumentDataList(documentDataListNew);
            List<TransactionLog> attachedTransactionLogListNew = new ArrayList<TransactionLog>();
            for (TransactionLog transactionLogListNewTransactionLogToAttach : transactionLogListNew) {
                transactionLogListNewTransactionLogToAttach = em.getReference(transactionLogListNewTransactionLogToAttach.getClass(), transactionLogListNewTransactionLogToAttach.getLogId());
                attachedTransactionLogListNew.add(transactionLogListNewTransactionLogToAttach);
            }
            transactionLogListNew = attachedTransactionLogListNew;
            documents.setTransactionLogList(transactionLogListNew);
            List<ApprovalRequest> attachedApprovalRequestListNew = new ArrayList<ApprovalRequest>();
            for (ApprovalRequest approvalRequestListNewApprovalRequestToAttach : approvalRequestListNew) {
                approvalRequestListNewApprovalRequestToAttach = em.getReference(approvalRequestListNewApprovalRequestToAttach.getClass(), approvalRequestListNewApprovalRequestToAttach.getAppID());
                attachedApprovalRequestListNew.add(approvalRequestListNewApprovalRequestToAttach);
            }
            approvalRequestListNew = attachedApprovalRequestListNew;
            documents.setApprovalRequestList(approvalRequestListNew);
            documents = em.merge(documents);
            if (doctypeIdOld != null && !doctypeIdOld.equals(doctypeIdNew)) {
                doctypeIdOld.getDocumentsList().remove(documents);
                doctypeIdOld = em.merge(doctypeIdOld);
            }
            if (doctypeIdNew != null && !doctypeIdNew.equals(doctypeIdOld)) {
                doctypeIdNew.getDocumentsList().add(documents);
                doctypeIdNew = em.merge(doctypeIdNew);
            }
            if (ownerIdOld != null && !ownerIdOld.equals(ownerIdNew)) {
                ownerIdOld.getDocumentsList().remove(documents);
                ownerIdOld = em.merge(ownerIdOld);
            }
            if (ownerIdNew != null && !ownerIdNew.equals(ownerIdOld)) {
                ownerIdNew.getDocumentsList().add(documents);
                ownerIdNew = em.merge(ownerIdNew);
            }
            if (homeIdOld != null && !homeIdOld.equals(homeIdNew)) {
                homeIdOld.getDocumentsList().remove(documents);
                homeIdOld = em.merge(homeIdOld);
            }
            if (homeIdNew != null && !homeIdNew.equals(homeIdOld)) {
                homeIdNew.getDocumentsList().add(documents);
                homeIdNew = em.merge(homeIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getDocumentsList().remove(documents);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getDocumentsList().add(documents);
                userIdNew = em.merge(userIdNew);
            }
            for (DocumentData documentDataListNewDocumentData : documentDataListNew) {
                if (!documentDataListOld.contains(documentDataListNewDocumentData)) {
                    Documents oldDocSerOfDocumentDataListNewDocumentData = documentDataListNewDocumentData.getDocSer();
                    documentDataListNewDocumentData.setDocSer(documents);
                    documentDataListNewDocumentData = em.merge(documentDataListNewDocumentData);
                    if (oldDocSerOfDocumentDataListNewDocumentData != null && !oldDocSerOfDocumentDataListNewDocumentData.equals(documents)) {
                        oldDocSerOfDocumentDataListNewDocumentData.getDocumentDataList().remove(documentDataListNewDocumentData);
                        oldDocSerOfDocumentDataListNewDocumentData = em.merge(oldDocSerOfDocumentDataListNewDocumentData);
                    }
                }
            }
            for (TransactionLog transactionLogListOldTransactionLog : transactionLogListOld) {
                if (!transactionLogListNew.contains(transactionLogListOldTransactionLog)) {
                    transactionLogListOldTransactionLog.setDocSer(null);
                    transactionLogListOldTransactionLog = em.merge(transactionLogListOldTransactionLog);
                }
            }
            for (TransactionLog transactionLogListNewTransactionLog : transactionLogListNew) {
                if (!transactionLogListOld.contains(transactionLogListNewTransactionLog)) {
                    Documents oldDocSerOfTransactionLogListNewTransactionLog = transactionLogListNewTransactionLog.getDocSer();
                    transactionLogListNewTransactionLog.setDocSer(documents);
                    transactionLogListNewTransactionLog = em.merge(transactionLogListNewTransactionLog);
                    if (oldDocSerOfTransactionLogListNewTransactionLog != null && !oldDocSerOfTransactionLogListNewTransactionLog.equals(documents)) {
                        oldDocSerOfTransactionLogListNewTransactionLog.getTransactionLogList().remove(transactionLogListNewTransactionLog);
                        oldDocSerOfTransactionLogListNewTransactionLog = em.merge(oldDocSerOfTransactionLogListNewTransactionLog);
                    }
                }
            }
            for (ApprovalRequest approvalRequestListNewApprovalRequest : approvalRequestListNew) {
                if (!approvalRequestListOld.contains(approvalRequestListNewApprovalRequest)) {
                    Documents oldDocSEROfApprovalRequestListNewApprovalRequest = approvalRequestListNewApprovalRequest.getDocSER();
                    approvalRequestListNewApprovalRequest.setDocSER(documents);
                    approvalRequestListNewApprovalRequest = em.merge(approvalRequestListNewApprovalRequest);
                    if (oldDocSEROfApprovalRequestListNewApprovalRequest != null && !oldDocSEROfApprovalRequestListNewApprovalRequest.equals(documents)) {
                        oldDocSEROfApprovalRequestListNewApprovalRequest.getApprovalRequestList().remove(approvalRequestListNewApprovalRequest);
                        oldDocSEROfApprovalRequestListNewApprovalRequest = em.merge(oldDocSEROfApprovalRequestListNewApprovalRequest);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = documents.getDocSer();
                if (findDocuments(id) == null) {
                    throw new NonexistentEntityException("The documents with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documents documents;
            try {
                documents = em.getReference(Documents.class, id);
                documents.getDocSer();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documents with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DocumentData> documentDataListOrphanCheck = documents.getDocumentDataList();
            for (DocumentData documentDataListOrphanCheckDocumentData : documentDataListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Documents (" + documents + ") cannot be destroyed since the DocumentData " + documentDataListOrphanCheckDocumentData + " in its documentDataList field has a non-nullable docSer field.");
            }
            List<ApprovalRequest> approvalRequestListOrphanCheck = documents.getApprovalRequestList();
            for (ApprovalRequest approvalRequestListOrphanCheckApprovalRequest : approvalRequestListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Documents (" + documents + ") cannot be destroyed since the ApprovalRequest " + approvalRequestListOrphanCheckApprovalRequest + " in its approvalRequestList field has a non-nullable docSER field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            DocTypes doctypeId = documents.getDoctypeId();
            if (doctypeId != null) {
                doctypeId.getDocumentsList().remove(documents);
                doctypeId = em.merge(doctypeId);
            }
            DocOwners ownerId = documents.getOwnerId();
            if (ownerId != null) {
                ownerId.getDocumentsList().remove(documents);
                ownerId = em.merge(ownerId);
            }
            DocHomes homeId = documents.getHomeId();
            if (homeId != null) {
                homeId.getDocumentsList().remove(documents);
                homeId = em.merge(homeId);
            }
            Users userId = documents.getUserId();
            if (userId != null) {
                userId.getDocumentsList().remove(documents);
                userId = em.merge(userId);
            }
            List<TransactionLog> transactionLogList = documents.getTransactionLogList();
            for (TransactionLog transactionLogListTransactionLog : transactionLogList) {
                transactionLogListTransactionLog.setDocSer(null);
                transactionLogListTransactionLog = em.merge(transactionLogListTransactionLog);
            }
            em.remove(documents);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Documents> findDocByOwner(int ownerId) {
        EntityManager em = null;

        em = getEntityManager();

        Query q = em.createNamedQuery("Documents.findByOwnerId");
        q.setParameter("ownerId", ownerId);

        //  q.getResultList()
        return q.getResultList();
    }

    public List<Documents> findDocumentsEntities() {
        return findDocumentsEntities(true, -1, -1);
    }

    public List<Documents> findDocumentsEntities(int maxResults, int firstResult) {
        return findDocumentsEntities(false, maxResults, firstResult);
    }

    private List<Documents> findDocumentsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documents.class));
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

    public Documents findByID(String ID) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Documents.findByDocSer");
        q.setParameter("docSer", ID);
        return (Documents) q.getResultList().get(0);
    }

    public List<Documents> findByDeleted() {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Documents.findByDeleted");
        return q.getResultList();
    }

    public List<Documents> findall() {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Documents.findAll");
        return q.getResultList();
    }

    public Documents findDocuments(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documents.class, id);
        } finally {
            em.close();
        }
    }

    public List<Documents> getDocsByOwner(int Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Documents.findByOwnerId").setParameter("ownerId", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getDocumentsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documents> rt = cq.from(Documents.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
