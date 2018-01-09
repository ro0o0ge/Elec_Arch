/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JPA;

import Entity.Message;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Users;
import JPA.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author GIGABYTE
 */
public class MessageJpaController implements Serializable {

    public MessageJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Message message) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users senderID = message.getSenderID();
            if (senderID != null) {
                senderID = em.getReference(senderID.getClass(), senderID.getUserId());
                message.setSenderID(senderID);
            }
            em.persist(message);
            if (senderID != null) {
                senderID.getMessageList().add(message);
                senderID = em.merge(senderID);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Message message) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Message persistentMessage = em.find(Message.class, message.getMessageID());
            Users senderIDOld = persistentMessage.getSenderID();
            Users senderIDNew = message.getSenderID();
            if (senderIDNew != null) {
                senderIDNew = em.getReference(senderIDNew.getClass(), senderIDNew.getUserId());
                message.setSenderID(senderIDNew);
            }
            message = em.merge(message);
            if (senderIDOld != null && !senderIDOld.equals(senderIDNew)) {
                senderIDOld.getMessageList().remove(message);
                senderIDOld = em.merge(senderIDOld);
            }
            if (senderIDNew != null && !senderIDNew.equals(senderIDOld)) {
                senderIDNew.getMessageList().add(message);
                senderIDNew = em.merge(senderIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = message.getMessageID();
                if (findMessage(id) == null) {
                    throw new NonexistentEntityException("The message with id " + id + " no longer exists.");
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
            Message message;
            try {
                message = em.getReference(Message.class, id);
                message.getMessageID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The message with id " + id + " no longer exists.", enfe);
            }
            Users senderID = message.getSenderID();
            if (senderID != null) {
                senderID.getMessageList().remove(message);
                senderID = em.merge(senderID);
            }
            em.remove(message);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Message> findMessageEntities() {
        return findMessageEntities(true, -1, -1);
    }

    public List<Message> findMessageEntities(int maxResults, int firstResult) {
        return findMessageEntities(false, maxResults, firstResult);
    }

    private List<Message> findMessageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Message.class));
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

    public Message findMessage(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Message.class, id);
        } finally {
            em.close();
        }
    }

    public int getMessageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Message> rt = cq.from(Message.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
