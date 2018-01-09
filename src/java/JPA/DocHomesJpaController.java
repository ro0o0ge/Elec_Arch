/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA;

import Entity.DocHomes;
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
public class DocHomesJpaController implements Serializable {

    public DocHomesJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DocHomes docHomes) throws PreexistingEntityException, Exception {
        if (docHomes.getDocumentsList() == null) {
            docHomes.setDocumentsList(new ArrayList<Documents>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Documents> attachedDocumentsList = new ArrayList<Documents>();
            for (Documents documentsListDocumentsToAttach : docHomes.getDocumentsList()) {
                documentsListDocumentsToAttach = em.getReference(documentsListDocumentsToAttach.getClass(), documentsListDocumentsToAttach.getDocSer());
                attachedDocumentsList.add(documentsListDocumentsToAttach);
            }
            docHomes.setDocumentsList(attachedDocumentsList);
            em.persist(docHomes);
            for (Documents documentsListDocuments : docHomes.getDocumentsList()) {
                DocHomes oldHomeIdOfDocumentsListDocuments = documentsListDocuments.getHomeId();
                documentsListDocuments.setHomeId(docHomes);
                documentsListDocuments = em.merge(documentsListDocuments);
                if (oldHomeIdOfDocumentsListDocuments != null) {
                    oldHomeIdOfDocumentsListDocuments.getDocumentsList().remove(documentsListDocuments);
                    oldHomeIdOfDocumentsListDocuments = em.merge(oldHomeIdOfDocumentsListDocuments);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDocHomes(docHomes.getHomeId()) != null) {
                throw new PreexistingEntityException("DocHomes " + docHomes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editDocHome(DocHomes docHomes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(docHomes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = docHomes.getHomeId();
                if (findDocHomes(id) == null) {
                    throw new NonexistentEntityException("The docHomes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DocHomes docHomes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocHomes persistentDocHomes = em.find(DocHomes.class, docHomes.getHomeId());
            List<Documents> documentsListOld = persistentDocHomes.getDocumentsList();
            List<Documents> documentsListNew = docHomes.getDocumentsList();
            List<String> illegalOrphanMessages = null;
            for (Documents documentsListOldDocuments : documentsListOld) {
                if (!documentsListNew.contains(documentsListOldDocuments)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documents " + documentsListOldDocuments + " since its homeId field is not nullable.");
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
            docHomes.setDocumentsList(documentsListNew);
            docHomes = em.merge(docHomes);
            for (Documents documentsListNewDocuments : documentsListNew) {
                if (!documentsListOld.contains(documentsListNewDocuments)) {
                    DocHomes oldHomeIdOfDocumentsListNewDocuments = documentsListNewDocuments.getHomeId();
                    documentsListNewDocuments.setHomeId(docHomes);
                    documentsListNewDocuments = em.merge(documentsListNewDocuments);
                    if (oldHomeIdOfDocumentsListNewDocuments != null && !oldHomeIdOfDocumentsListNewDocuments.equals(docHomes)) {
                        oldHomeIdOfDocumentsListNewDocuments.getDocumentsList().remove(documentsListNewDocuments);
                        oldHomeIdOfDocumentsListNewDocuments = em.merge(oldHomeIdOfDocumentsListNewDocuments);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = docHomes.getHomeId();
                if (findDocHomes(id) == null) {
                    throw new NonexistentEntityException("The docHomes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public  List<String> destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        List<String> illegalOrphanMessages = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocHomes docHomes;
            try {
                docHomes = em.getReference(DocHomes.class, id);
                docHomes.getHomeId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docHomes with id " + id + " no longer exists.", enfe);
            }
            
            List<Documents> documentsListOrphanCheck = docHomes.getDocumentsList();
            for (Documents documentsListOrphanCheckDocuments : documentsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DocHomes (\n" + docHomes.getHomePath() + "\n) cannot be destroyed since the Documents \n" + documentsListOrphanCheckDocuments.getDocSer() + "\n in its documentsList field has a non-nullable homeId field.");
            }
            if (illegalOrphanMessages != null) {
                return illegalOrphanMessages;
            }
            em.remove(docHomes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            
            return illegalOrphanMessages;
        }
    }

    public List<DocHomes> findDocHomesEntities() {
        return findDocHomesEntities(true, -1, -1);
    }

    public List<DocHomes> findDocHomesEntities(int maxResults, int firstResult) {
        return findDocHomesEntities(false, maxResults, firstResult);
    }

    private List<DocHomes> findDocHomesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocHomes.class));
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
    
    public boolean homePathExist(String path){
        EntityManager em=getEntityManager();
        Query q=em.createNamedQuery("DocHomes.findByHomePath");
        q.setParameter("homePath",path);
        try{
           q.getSingleResult();
        } catch(Exception e){
                    return false;
                    }
        return true;
    }

    public DocHomes findDocHomes(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocHomes.class, id);
        } finally {
            em.close();
        }
    }

    public List<DocHomes> getDocHomes_findCurrentHome() {
        EntityManager em = getEntityManager();
        List<DocHomes> d=new ArrayList<>();
        try {
            Query q = em.createNamedQuery("DocHomes.findByCurrentPath").setParameter("currentPath", "true");
            d= q.getResultList();
        } finally {
            em.close();
        }
        return d;
    }

    public int getDocHomesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocHomes> rt = cq.from(DocHomes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
