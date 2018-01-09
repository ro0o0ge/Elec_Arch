/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JPA;

import Entity.DocOwners;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Documents;
import JPA.exceptions.IllegalOrphanException;
import JPA.exceptions.NonexistentEntityException;
import JPA.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author norsin
 */
public class DocOwnersJpaController implements Serializable {

    public DocOwnersJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DocOwners docOwners) throws PreexistingEntityException, Exception {
        if (docOwners.getDocumentsList() == null) {
            docOwners.setDocumentsList(new ArrayList<Documents>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Documents> attachedDocumentsList = new ArrayList<Documents>();
            for (Documents documentsListDocumentsToAttach : docOwners.getDocumentsList()) {
                documentsListDocumentsToAttach = em.getReference(documentsListDocumentsToAttach.getClass(), documentsListDocumentsToAttach.getDocSer());
                attachedDocumentsList.add(documentsListDocumentsToAttach);
            }
            docOwners.setDocumentsList(attachedDocumentsList);
            em.persist(docOwners);
            for (Documents documentsListDocuments : docOwners.getDocumentsList()) {
                DocOwners oldOwnerIdOfDocumentsListDocuments = documentsListDocuments.getOwnerId();
                documentsListDocuments.setOwnerId(docOwners);
                documentsListDocuments = em.merge(documentsListDocuments);
                if (oldOwnerIdOfDocumentsListDocuments != null) {
                    oldOwnerIdOfDocumentsListDocuments.getDocumentsList().remove(documentsListDocuments);
                    oldOwnerIdOfDocumentsListDocuments = em.merge(oldOwnerIdOfDocumentsListDocuments);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDocOwners(docOwners.getOwnerId()) != null) {
                throw new PreexistingEntityException("DocOwners " + docOwners + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DocOwners docOwners) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocOwners persistentDocOwners = em.find(DocOwners.class, docOwners.getOwnerId());
            List<Documents> documentsListOld = persistentDocOwners.getDocumentsList();
            List<Documents> documentsListNew = docOwners.getDocumentsList();
            List<String> illegalOrphanMessages = null;
            for (Documents documentsListOldDocuments : documentsListOld) {
                if (!documentsListNew.contains(documentsListOldDocuments)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documents " + documentsListOldDocuments + " since its ownerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Documents> attachedDocumentsListNew = new ArrayList<Documents>();
            for (Documents documentsListNewDocumentsToAttach : documentsListNew) {
                documentsListNewDocumentsToAttach = em.getReference(documentsListNewDocumentsToAttach.getClass(), documentsListNewDocumentsToAttach.getDocSer());
                attachedDocumentsListNew.add(documentsListNewDocumentsToAttach);
            }
            documentsListNew = attachedDocumentsListNew;
            docOwners.setDocumentsList(documentsListNew);
            docOwners = em.merge(docOwners);
            for (Documents documentsListNewDocuments : documentsListNew) {
                if (!documentsListOld.contains(documentsListNewDocuments)) {
                    DocOwners oldOwnerIdOfDocumentsListNewDocuments = documentsListNewDocuments.getOwnerId();
                    documentsListNewDocuments.setOwnerId(docOwners);
                    documentsListNewDocuments = em.merge(documentsListNewDocuments);
                    if (oldOwnerIdOfDocumentsListNewDocuments != null && !oldOwnerIdOfDocumentsListNewDocuments.equals(docOwners)) {
                        oldOwnerIdOfDocumentsListNewDocuments.getDocumentsList().remove(documentsListNewDocuments);
                        oldOwnerIdOfDocumentsListNewDocuments = em.merge(oldOwnerIdOfDocumentsListNewDocuments);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = docOwners.getOwnerId();
                if (findDocOwners(id) == null) {
                    throw new NonexistentEntityException("The docOwners with id " + id + " no longer exists.");
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
            DocOwners docOwners;
            try {
                docOwners = em.getReference(DocOwners.class, id);
                docOwners.getOwnerId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docOwners with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Documents> documentsListOrphanCheck = docOwners.getDocumentsList();
            for (Documents documentsListOrphanCheckDocuments : documentsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DocOwners (" + docOwners + ") cannot be destroyed since the Documents " + documentsListOrphanCheckDocuments + " in its documentsList field has a non-nullable ownerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(docOwners);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DocOwners> findDocOwnersEntities() {
        return findDocOwnersEntities(true, -1, -1);
    }

    public List<DocOwners> findDocOwnersEntities(int maxResults, int firstResult) {
        return findDocOwnersEntities(false, maxResults, firstResult);
    }

    private List<DocOwners> findDocOwnersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocOwners.class));
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

    public DocOwners findDocOwners(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocOwners.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocOwnersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocOwners> rt = cq.from(DocOwners.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
