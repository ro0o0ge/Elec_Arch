/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA;

import Entity.DocumentData;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class DocumentDataJpaController implements Serializable {

    public DocumentDataJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DocumentData documentData) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documents docSer = documentData.getDocSer();
            if (docSer != null) {
                docSer = em.getReference(docSer.getClass(), docSer.getDocSer());
                documentData.setDocSer(docSer);
            }
            em.persist(documentData);
            if (docSer != null) {
                docSer.getDocumentDataList().add(documentData);
                docSer = em.merge(docSer);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void editDate(DocumentData documentData) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
          
            documentData = em.merge(documentData);
            
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documentData.getDataID();
                if (findDocumentData(id) == null) {
                    throw new NonexistentEntityException("The documentData with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public void edit(DocumentData documentData) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocumentData persistentDocumentData = em.find(DocumentData.class, documentData.getDataID());
            Documents docSerOld = persistentDocumentData.getDocSer();
            Documents docSerNew = documentData.getDocSer();
            if (docSerNew != null) {
                docSerNew = em.getReference(docSerNew.getClass(), docSerNew.getDocSer());
                documentData.setDocSer(docSerNew);
            }
            documentData = em.merge(documentData);
            if (docSerOld != null && !docSerOld.equals(docSerNew)) {
                docSerOld.getDocumentDataList().remove(documentData);
                docSerOld = em.merge(docSerOld);
            }
            if (docSerNew != null && !docSerNew.equals(docSerOld)) {
                docSerNew.getDocumentDataList().add(documentData);
                docSerNew = em.merge(docSerNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documentData.getDataID();
                if (findDocumentData(id) == null) {
                    throw new NonexistentEntityException("The documentData with id " + id + " no longer exists.");
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
            DocumentData documentData;
            try {
                documentData = em.getReference(DocumentData.class, id);
                documentData.getDataID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documentData with id " + id + " no longer exists.", enfe);
            }
            Documents docSer = documentData.getDocSer();
            if (docSer != null) {
                docSer.getDocumentDataList().remove(documentData);
                docSer = em.merge(docSer);
            }
            em.remove(documentData);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DocumentData> findDocumentDataEntities() {
        return findDocumentDataEntities(true, -1, -1);
    }

    public List<DocumentData> findDocumentDataEntities(int maxResults, int firstResult) {
        return findDocumentDataEntities(false, maxResults, firstResult);
    }

    private List<DocumentData> findDocumentDataEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocumentData.class));
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

    public DocumentData findDocumentData(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocumentData.class, id);
        } finally {
            em.close();
        }
    }

    public List<DocumentData> findDocumentData(String ID) {
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("DocumentData.findByDocSer");
        q.setParameter("docSer", ID);

        return q.getResultList();
    }

    public DocumentData findDocumentByDocSerFieldName(String ID, String fieldName) {
        
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("DocumentData.findByDocSerFieldName");
        DocumentData d;
        try {
            q.setParameter("docSer", ID).setParameter("fieldName", fieldName);
            d = (DocumentData) q.getSingleResult();
        } catch (Exception e) {

            return null;
        }
        return d;

    }

    public int getDocumentDataCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocumentData> rt = cq.from(DocumentData.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
