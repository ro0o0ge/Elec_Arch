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
import Entity.Groups;
import Entity.UserGroup;
import Entity.Users;
import JPA.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author norsin
 */
public class UserGroupJpaController implements Serializable {

    public UserGroupJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserGroup userGroup) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Groups groupId = userGroup.getGroupId();
            if (groupId != null) {
                groupId = em.getReference(groupId.getClass(), groupId.getGroupId());
                userGroup.setGroupId(groupId);
            }
            Users userId = userGroup.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                userGroup.setUserId(userId);
            }
            em.persist(userGroup);
            if (groupId != null) {
                groupId.getUserGroupList().add(userGroup);
                groupId = em.merge(groupId);
            }
            if (userId != null) {
                userId.getUserGroupList().add(userGroup);
                userId = em.merge(userId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserGroup userGroup) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserGroup persistentUserGroup = em.find(UserGroup.class, userGroup.getUgId());
            Groups groupIdOld = persistentUserGroup.getGroupId();
            Groups groupIdNew = userGroup.getGroupId();
            Users userIdOld = persistentUserGroup.getUserId();
            Users userIdNew = userGroup.getUserId();
            if (groupIdNew != null) {
                groupIdNew = em.getReference(groupIdNew.getClass(), groupIdNew.getGroupId());
                userGroup.setGroupId(groupIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                userGroup.setUserId(userIdNew);
            }
            userGroup = em.merge(userGroup);
            if (groupIdOld != null && !groupIdOld.equals(groupIdNew)) {
                groupIdOld.getUserGroupList().remove(userGroup);
                groupIdOld = em.merge(groupIdOld);
            }
            if (groupIdNew != null && !groupIdNew.equals(groupIdOld)) {
                groupIdNew.getUserGroupList().add(userGroup);
                groupIdNew = em.merge(groupIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getUserGroupList().remove(userGroup);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getUserGroupList().add(userGroup);
                userIdNew = em.merge(userIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userGroup.getUgId();
                if (findUserGroup(id) == null) {
                    throw new NonexistentEntityException("The userGroup with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroyU(int UID) throws NonexistentEntityException {
        EntityManager em = null;
        em = getEntityManager();
        Query q = null;
        try {
            q = em.createNamedQuery("UserGroup.findByUserId");
            q.setParameter("uId", UID);
        } catch (Exception e) {

        }

        List<UserGroup> UserG = (List<UserGroup>) q.getResultList();
        for (UserGroup UG : UserG) {
            del(UG.getUgId());
        }
    }

    public void destroyG(int UID) throws NonexistentEntityException {
        EntityManager em = null;
        em = getEntityManager();
        Query q = null;
        try {
            q = em.createNamedQuery("UserGroup.findByGroupId");
            q.setParameter("gId", UID);
        } catch (Exception e) {

        }

        List<UserGroup> UserG = (List<UserGroup>) q.getResultList();
        for (UserGroup UG : UserG) {
            del(UG.getUgId());
        }
    }

    public void del(int id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserGroup userGroup = em.getReference(UserGroup.class, id);
            em.remove(userGroup);
            em.getTransaction().commit();
        } catch (Exception e) {
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

            UserGroup userGroup;
            try {
                userGroup = em.getReference(UserGroup.class, id);
                userGroup.getUgId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userGroup with id " + id + " no longer exists.", enfe);
            }
            Groups groupId = userGroup.getGroupId();
            if (groupId != null) {
                groupId.getUserGroupList().remove(userGroup);
                groupId = em.merge(groupId);
            }
            Users userId = userGroup.getUserId();
            if (userId != null) {
                userId.getUserGroupList().remove(userGroup);
                userId = em.merge(userId);
            }
            em.remove(userGroup);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserGroup> findUserGroupEntities() {
        return findUserGroupEntities(true, -1, -1);
    }

    public List<UserGroup> findUserGroupEntities(int maxResults, int firstResult) {
        return findUserGroupEntities(false, maxResults, firstResult);
    }

    private List<UserGroup> findUserGroupEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserGroup.class));
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

    public List<UserGroup> getByUserID(String Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("UserGroup.findByUserId").setParameter("uId", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Groups> getGroupsForUser(int Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("UserGroup.UserInGroup").setParameter("userId", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Groups> getNotGroupsForUser(int Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("UserGroup.UserNotInGroup").setParameter("userId", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Users> getUserInGroup(int Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("UserGroup.GroupsContainUser").setParameter("groupId", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Users> getUserNotInGroup(int Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("UserGroup.GroupsHaveNotUser").setParameter("groupId", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public UserGroup findUserGroup(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserGroup.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserGroupCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserGroup> rt = cq.from(UserGroup.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
