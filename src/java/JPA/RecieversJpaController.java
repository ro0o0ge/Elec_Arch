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
import Entity.Message;
import Entity.Documents;
import Entity.Groups;
import Entity.Recievers;
import Entity.Users;
import JPA.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author GIGABYTE
 */
public class RecieversJpaController implements Serializable {

    public RecieversJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recievers recievers) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Message messageID = recievers.getMessageID();
            if (messageID != null) {
                messageID = em.getReference(messageID.getClass(), messageID.getMessageID());
                recievers.setMessageID(messageID);
            }
            Documents docSER = recievers.getDocSER();
            if (docSER != null) {
                docSER = em.getReference(docSER.getClass(), docSER.getDocSer());
                recievers.setDocSER(docSER);
            }
            Groups groupID = recievers.getGroupID();
            if (groupID != null) {
                groupID = em.getReference(groupID.getClass(), groupID.getGroupId());
                recievers.setGroupID(groupID);
            }
            Users userID = recievers.getUserID();
            if (userID != null) {
                userID = em.getReference(userID.getClass(), userID.getUserId());
                recievers.setUserID(userID);
            }
            em.persist(recievers);
            if (messageID != null) {
                messageID.getRecieversList().add(recievers);
                messageID = em.merge(messageID);
            }
            if (docSER != null) {
                docSER.getRecieversList().add(recievers);
                docSER = em.merge(docSER);
            }
            if (groupID != null) {
                groupID.getRecieversList().add(recievers);
                groupID = em.merge(groupID);
            }
            if (userID != null) {
                userID.getRecieversList().add(recievers);
                userID = em.merge(userID);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recievers recievers) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recievers persistentRecievers = em.find(Recievers.class, recievers.getRId());
            Message messageIDOld = persistentRecievers.getMessageID();
            Message messageIDNew = recievers.getMessageID();
            Documents docSEROld = persistentRecievers.getDocSER();
            Documents docSERNew = recievers.getDocSER();
            Groups groupIDOld = persistentRecievers.getGroupID();
            Groups groupIDNew = recievers.getGroupID();
            Users userIDOld = persistentRecievers.getUserID();
            Users userIDNew = recievers.getUserID();
            if (messageIDNew != null) {
                messageIDNew = em.getReference(messageIDNew.getClass(), messageIDNew.getMessageID());
                recievers.setMessageID(messageIDNew);
            }
            if (docSERNew != null) {
                docSERNew = em.getReference(docSERNew.getClass(), docSERNew.getDocSer());
                recievers.setDocSER(docSERNew);
            }
            if (groupIDNew != null) {
                groupIDNew = em.getReference(groupIDNew.getClass(), groupIDNew.getGroupId());
                recievers.setGroupID(groupIDNew);
            }
            if (userIDNew != null) {
                userIDNew = em.getReference(userIDNew.getClass(), userIDNew.getUserId());
                recievers.setUserID(userIDNew);
            }
            recievers = em.merge(recievers);
            if (messageIDOld != null && !messageIDOld.equals(messageIDNew)) {
                messageIDOld.getRecieversList().remove(recievers);
                messageIDOld = em.merge(messageIDOld);
            }
            if (messageIDNew != null && !messageIDNew.equals(messageIDOld)) {
                messageIDNew.getRecieversList().add(recievers);
                messageIDNew = em.merge(messageIDNew);
            }
            if (docSEROld != null && !docSEROld.equals(docSERNew)) {
                docSEROld.getRecieversList().remove(recievers);
                docSEROld = em.merge(docSEROld);
            }
            if (docSERNew != null && !docSERNew.equals(docSEROld)) {
                docSERNew.getRecieversList().add(recievers);
                docSERNew = em.merge(docSERNew);
            }
            if (groupIDOld != null && !groupIDOld.equals(groupIDNew)) {
                groupIDOld.getRecieversList().remove(recievers);
                groupIDOld = em.merge(groupIDOld);
            }
            if (groupIDNew != null && !groupIDNew.equals(groupIDOld)) {
                groupIDNew.getRecieversList().add(recievers);
                groupIDNew = em.merge(groupIDNew);
            }
            if (userIDOld != null && !userIDOld.equals(userIDNew)) {
                userIDOld.getRecieversList().remove(recievers);
                userIDOld = em.merge(userIDOld);
            }
            if (userIDNew != null && !userIDNew.equals(userIDOld)) {
                userIDNew.getRecieversList().add(recievers);
                userIDNew = em.merge(userIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recievers.getRId();
                if (findRecievers(id) == null) {
                    throw new NonexistentEntityException("The recievers with id " + id + " no longer exists.");
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
            Recievers recievers;
            try {
                recievers = em.getReference(Recievers.class, id);
                recievers.getRId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recievers with id " + id + " no longer exists.", enfe);
            }
            Message messageID = recievers.getMessageID();
            if (messageID != null) {
                messageID.getRecieversList().remove(recievers);
                messageID = em.merge(messageID);
            }
            Documents docSER = recievers.getDocSER();
            if (docSER != null) {
                docSER.getRecieversList().remove(recievers);
                docSER = em.merge(docSER);
            }
            Groups groupID = recievers.getGroupID();
            if (groupID != null) {
                groupID.getRecieversList().remove(recievers);
                groupID = em.merge(groupID);
            }
            Users userID = recievers.getUserID();
            if (userID != null) {
                userID.getRecieversList().remove(recievers);
                userID = em.merge(userID);
            }
            em.remove(recievers);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recievers> findRecieversEntities() {
        return findRecieversEntities(true, -1, -1);
    }

    public List<Recievers> findRecieversEntities(int maxResults, int firstResult) {
        return findRecieversEntities(false, maxResults, firstResult);
    }

    private List<Recievers> findRecieversEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recievers.class));
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

    public Recievers findRecievers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recievers.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecieversCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recievers> rt = cq.from(Recievers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Recievers> findRecieverByUser(int id) {
        List<Recievers> Rx = new ArrayList<>();
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("Recievers.findByUserID");
        try {
            q.setParameter("userId", id);
            Rx = q.getResultList();
        } catch (Exception e) {
        }
        return Rx;
    }

    public List<Recievers> findRecieverByGroup(int id) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Recievers.findByGroupID");
        q.setParameter("groupId", id);
        return (List<Recievers>) q.getResultList();
    }

    public List<Recievers> findBymsgId(int msgId, int userId) {
        List<Recievers> Rx = new ArrayList<>();
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("Recievers.findByMsgID");
        try {
            q = q.setParameter("msgId", msgId).setParameter("userId", userId);
            Rx = q.getResultList();
        } catch (Exception e) {

        }
        return Rx;
    }
}
