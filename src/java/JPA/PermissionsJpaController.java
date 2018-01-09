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
import Entity.Users;
import Entity.Groups;
import Entity.DocTypes;
import Entity.Permissions;
import JPA.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author norsin
 */
public class PermissionsJpaController implements Serializable {

    public PermissionsJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permissions permissions) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users userId = permissions.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                permissions.setUserId(userId);
            }
            Groups groupId = permissions.getGroupId();
            if (groupId != null) {
                groupId = em.getReference(groupId.getClass(), groupId.getGroupId());
                permissions.setGroupId(groupId);
            }
            DocTypes doctypeId = permissions.getDoctypeId();
            if (doctypeId != null) {
                doctypeId = em.getReference(doctypeId.getClass(), doctypeId.getDoctypeId());
                permissions.setDoctypeId(doctypeId);
            }
            em.persist(permissions);
            if (userId != null) {
                userId.getPermissionsList().add(permissions);
                userId = em.merge(userId);
            }
            if (groupId != null) {
                groupId.getPermissionsList().add(permissions);
                groupId = em.merge(groupId);
            }
            if (doctypeId != null) {
                doctypeId.getPermissionsList().add(permissions);
                doctypeId = em.merge(doctypeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permissions permissions) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permissions persistentPermissions = em.find(Permissions.class, permissions.getPermID());
            Users userIdOld = persistentPermissions.getUserId();
            Users userIdNew = permissions.getUserId();
            Groups groupIdOld = persistentPermissions.getGroupId();
            Groups groupIdNew = permissions.getGroupId();
            DocTypes doctypeIdOld = persistentPermissions.getDoctypeId();
            DocTypes doctypeIdNew = permissions.getDoctypeId();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                permissions.setUserId(userIdNew);
            }
            if (groupIdNew != null) {
                groupIdNew = em.getReference(groupIdNew.getClass(), groupIdNew.getGroupId());
                permissions.setGroupId(groupIdNew);
            }
            if (doctypeIdNew != null) {
                doctypeIdNew = em.getReference(doctypeIdNew.getClass(), doctypeIdNew.getDoctypeId());
                permissions.setDoctypeId(doctypeIdNew);
            }
            permissions = em.merge(permissions);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getPermissionsList().remove(permissions);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getPermissionsList().add(permissions);
                userIdNew = em.merge(userIdNew);
            }
            if (groupIdOld != null && !groupIdOld.equals(groupIdNew)) {
                groupIdOld.getPermissionsList().remove(permissions);
                groupIdOld = em.merge(groupIdOld);
            }
            if (groupIdNew != null && !groupIdNew.equals(groupIdOld)) {
                groupIdNew.getPermissionsList().add(permissions);
                groupIdNew = em.merge(groupIdNew);
            }
            if (doctypeIdOld != null && !doctypeIdOld.equals(doctypeIdNew)) {
                doctypeIdOld.getPermissionsList().remove(permissions);
                doctypeIdOld = em.merge(doctypeIdOld);
            }
            if (doctypeIdNew != null && !doctypeIdNew.equals(doctypeIdOld)) {
                doctypeIdNew.getPermissionsList().add(permissions);
                doctypeIdNew = em.merge(doctypeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = permissions.getPermID();
                if (findPermissions(id) == null) {
                    throw new NonexistentEntityException("The permissions with id " + id + " no longer exists.");
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
            Permissions permissions;
            try {
                permissions = em.getReference(Permissions.class, id);
                permissions.getPermID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permissions with id " + id + " no longer exists.", enfe);
            }
            Users userId = permissions.getUserId();
            if (userId != null) {
                userId.getPermissionsList().remove(permissions);
                userId = em.merge(userId);
            }
            Groups groupId = permissions.getGroupId();
            if (groupId != null) {
                groupId.getPermissionsList().remove(permissions);
                groupId = em.merge(groupId);
            }
            DocTypes doctypeId = permissions.getDoctypeId();
            if (doctypeId != null) {
                doctypeId.getPermissionsList().remove(permissions);
                doctypeId = em.merge(doctypeId);
            }
            em.remove(permissions);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // =============== GroupExist ==============
    public boolean groupIsExist(String fName, int docType, int gID) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Permissions.findByFieldNameDocTypeGroupID");

        q = q.setParameter("fieldName", fName).setParameter("doctypeId", docType).setParameter("groupId", gID);
        try {
            q.getResultList().get(0);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean groupIsExist(int docType, int gID) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Permissions.findByDocTypeGroupID");

        q = q.setParameter("doctypeId", docType).setParameter("groupId", gID);
        try {
            q.getResultList().get(0);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // ============== UserExist ================
    public boolean userIsExist(int docType, int uID) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Permissions.findByDocTypeUserID");

        q = q.setParameter("doctypeId", docType).setParameter("userId", uID);
        try {
            q.getResultList().get(0);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean userIsExist(String fName, int docType, int uID) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Permissions.findByFieldNameDocTypeUserID");

        q = q.setParameter("fieldName", fName).setParameter("doctypeId", docType).setParameter("userId", uID);
        try {
            q.getResultList().get(0);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<Permissions> findPermissionsEntities() {
        return findPermissionsEntities(true, -1, -1);
    }

    public List<Permissions> findPermissionsEntities(int maxResults, int firstResult) {
        return findPermissionsEntities(false, maxResults, firstResult);
    }

    private List<Permissions> findPermissionsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permissions.class));
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

    public Permissions findPermissions(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permissions.class, id);
        } finally {
            em.close();
        }
    }

    public List<Permissions> getByGroupId(int Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Permissions.findByGroupId").setParameter("groupId", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Permissions> getByUserId(int Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Permissions.findByUserId").setParameter("userId", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getPermissionsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permissions> rt = cq.from(Permissions.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Permissions> findByGroupUserDocId(int docId, int groupId, int userId) {
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("Permissions.findByGroupUserDocId");
        q = q.setParameter("userId", userId).setParameter("groupId", groupId).setParameter("docTypeId", docId);

        return q.getResultList();
    }

    public List<String> findByUserId(int docId, int userId) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Permissions.findByGroupUserDocId");
        try {

            q = q.setParameter("userId", userId).setParameter("docTypeId", docId);
        } catch (Exception e) {
            return null;
        }

        return q.getResultList();
    }

    public Permissions findByUserId(int docId, int userId, String FieldName) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Permissions.findByFieldNameDocTypeUserID");
        try {

            q = q.setParameter("userId", userId).setParameter("doctypeId", docId).setParameter("fieldName", FieldName);
        } catch (Exception e) {
            return null;
        }

        return (Permissions) q.getSingleResult();
    }

    // ===========================
    public Permissions findByGroupId(int docId, int groupId, String FieldName) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Permissions.findByFieldNameDocTypeGroupID");
        try {

            q = q.setParameter("groupId", groupId).setParameter("doctypeId", docId).setParameter("fieldName", FieldName);
        } catch (Exception e) {
            return null;
        }

        return (Permissions) q.getSingleResult();
    }

    // ==============================
    public List<Permissions> finddeleteByUserId(int docId, int userId) {
        EntityManager em = getEntityManager();
        List<Permissions> p = null;
        Query q = em.createNamedQuery("Permissions.findByDocTypeUserID");
        try {

            q = q.setParameter("userId", userId).setParameter("doctypeId", docId);
            p = (List<Permissions>) q.getResultList();
        } catch (Exception e) {
            return null;
        }

        return p;
    }

    // ================ BY GroupID ================
    public List<Permissions> findByGroupUserDocIdObject(int groupId, String fieldName) {
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("Permissions.findByGroupFieldname");
        q = q.setParameter("groupId", groupId).setParameter("fieldName", fieldName);

        return q.getResultList();
    }

    // ============= Without GroupID =================
    public List<Permissions> findByGroupUserDocIdObject(String fieldName, int userId) {
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("Permissions.findByUserFieldname");
        q = q.setParameter("userId", userId).setParameter("fieldName", fieldName);

        return q.getResultList();
    }

    // ================ BY GroupID ================
    public Permissions findBydocTypeId(int groupId, int docTypeId, int id) {
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("Permissions.findByGroupDocId");
        q = q.setParameter("groupId", groupId).setParameter("docTypeId", docTypeId);

        return (Permissions) q.getSingleResult();
    }

    public List<Permissions> findByGdocTypeId(int groupId, int docTypeId) {
        EntityManager em = getEntityManager();
        List<Permissions> p = null;
        Query q = em.createNamedQuery("Permissions.findByDocTypeGroupID");
        q = q.setParameter("groupId", groupId).setParameter("docTypeId", docTypeId);
        try {
            p = q.getResultList();
        }catch(Exception e){
            
        }
       return p;
    }

    // ============= Without GroupID =================
    public Permissions findBydocTypeId(int docTypeId, int userId) {
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("Permissions.findByUserWOFieldname");
        q = q.setParameter("userId", userId).setParameter("docTypeId", docTypeId);

        return (Permissions) q.getSingleResult();
    }

}
