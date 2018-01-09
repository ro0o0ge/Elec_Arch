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
import Entity.PagePermission;
import Entity.Users;
import JPA.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author norsin
 */
public class PagePermissionJpaController implements Serializable {

    public PagePermissionJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PagePermission pagePermission) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Groups groupID = pagePermission.getGroupID();
            if (groupID != null) {
                groupID = em.getReference(groupID.getClass(), groupID.getGroupId());
                pagePermission.setGroupID(groupID);
            }
            Users userID = pagePermission.getUserID();
            if (userID != null) {
                userID = em.getReference(userID.getClass(), userID.getUserId());
                pagePermission.setUserID(userID);
            }
            em.persist(pagePermission);
            if (groupID != null) {
                groupID.getPagePermissionList().add(pagePermission);
                groupID = em.merge(groupID);
            }
            if (userID != null) {
                userID.getPagePermissionList().add(pagePermission);
                userID = em.merge(userID);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PagePermission pagePermission) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PagePermission persistentPagePermission = em.find(PagePermission.class, pagePermission.getPId());
            Groups groupIDOld = persistentPagePermission.getGroupID();
            Groups groupIDNew = pagePermission.getGroupID();
            Users userIDOld = persistentPagePermission.getUserID();
            Users userIDNew = pagePermission.getUserID();
            if (groupIDNew != null) {
                groupIDNew = em.getReference(groupIDNew.getClass(), groupIDNew.getGroupId());
                pagePermission.setGroupID(groupIDNew);
            }
            if (userIDNew != null) {
                userIDNew = em.getReference(userIDNew.getClass(), userIDNew.getUserId());
                pagePermission.setUserID(userIDNew);
            }
            pagePermission = em.merge(pagePermission);
            if (groupIDOld != null && !groupIDOld.equals(groupIDNew)) {
                groupIDOld.getPagePermissionList().remove(pagePermission);
                groupIDOld = em.merge(groupIDOld);
            }
            if (groupIDNew != null && !groupIDNew.equals(groupIDOld)) {
                groupIDNew.getPagePermissionList().add(pagePermission);
                groupIDNew = em.merge(groupIDNew);
            }
            if (userIDOld != null && !userIDOld.equals(userIDNew)) {
                userIDOld.getPagePermissionList().remove(pagePermission);
                userIDOld = em.merge(userIDOld);
            }
            if (userIDNew != null && !userIDNew.equals(userIDOld)) {
                userIDNew.getPagePermissionList().add(pagePermission);
                userIDNew = em.merge(userIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pagePermission.getPId();
                if (findPagePermission(id) == null) {
                    throw new NonexistentEntityException("The pagePermission with id " + id + " no longer exists.");
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
            PagePermission pagePermission;
            try {
                pagePermission = em.getReference(PagePermission.class, id);
                pagePermission.getPId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pagePermission with id " + id + " no longer exists.", enfe);
            }
            Groups groupID = pagePermission.getGroupID();
            if (groupID != null) {
                groupID.getPagePermissionList().remove(pagePermission);
                groupID = em.merge(groupID);
            }
            Users userID = pagePermission.getUserID();
            if (userID != null) {
                userID.getPagePermissionList().remove(pagePermission);
                userID = em.merge(userID);
            }
            em.remove(pagePermission);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PagePermission> DocTypeDataDef(String Id) {
        EntityManager em = getEntityManager();
        
        // =================
        List<PagePermission> pagesList=new ArrayList<>();
        for(PagePermission p: this.findPagePermissionEntities()){
            if(p.getPageName().getAppPageName().equals(Id)){
                pagesList.add(p);
            }
        }
        return pagesList;
        // ===================
        

    }

    public List<PagePermission> findPagePermissionEntities() {
        return findPagePermissionEntities(true, -1, -1);
    }

    public List<PagePermission> findPagePermissionEntities(int maxResults, int firstResult) {
        return findPagePermissionEntities(false, maxResults, firstResult);
    }

    private List<PagePermission> findPagePermissionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PagePermission.class));
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

    public PagePermission findPagePermission(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PagePermission.class, id);
        } finally {
            em.close();
        }
    }

    public List<PagePermission> findByUserId(Integer Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("PagePermission.findByUserID").setParameter("user", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<PagePermission> findByGroupId(int Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("PagePermission.findByGroupID").setParameter("group", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getPagePermissionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PagePermission> rt = cq.from(PagePermission.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public boolean userExist(Integer uID, String PName) {
        
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("PagePermission.findByPageNameUser");
        q = q.setParameter("user", uID).setParameter("pageName", PName);
        boolean flag = false;
//        q=q.setParameter("pageName", PName);
        try {
            q.getSingleResult();
//            List<PagePermission> pages = q.getResultList();
//            for (PagePermission p : pages) {
//                if (p.getPageName().getAppPageName().equals(PName)) {
//
//                    return true;
//
//                }
//            }
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    public PagePermission findByUser(Integer uID, String PName) {
        EntityManager em = getEntityManager();
        PagePermission pPermission=null;//new PagePermission();
        try {
            Query q = em.createNamedQuery("PagePermission.findByPageNameUser");
            q=q.setParameter("user", uID).setParameter("pageName", PName);
            pPermission=(PagePermission) q.getSingleResult();
//            List<PagePermission> pageList=q.getResultList();
//            for(PagePermission pages:pageList){
//                pPermission=new PagePermission();
//                if(pages.getPageName().getAppPageName().equals(PName)){
//                    pPermission= pages;
//                }
//            }
           
        } finally {
            em.close();
        }
        return pPermission;
    }

    public PagePermission findByGroup(Integer gID, String PName) {
        EntityManager em = getEntityManager();
         PagePermission pPermission=null;//new PagePermission();
        try {
            Query q = em.createNamedQuery("PagePermission.findByPageNameGroup");
            q.setParameter("groupID", gID).setParameter("pageName", PName);
            pPermission=(PagePermission) q.getSingleResult();
//            List<PagePermission> pageList=q.getResultList();
//            for(PagePermission pages:pageList){
//                pPermission=new PagePermission();
//                if(pages.getPageName().getAppPageName().equals(PName)){
//                    pPermission= pages;
//                }
//            }
//            return (PagePermission) q.getResultList().get(0);
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
         return pPermission;
    }

    public boolean groupExist(int gID, String PName) {
        
        
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("PagePermission.findByGroupID");
        q.setParameter("group", gID);//.setParameter("pageName", PName);
        try {
             
             List<PagePermission> pages =  q.getResultList();
            for (PagePermission p : pages) {
                if (p.getPageName().getAppPageName().equals(PName)) {

                    return true;

                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }

    }

}
