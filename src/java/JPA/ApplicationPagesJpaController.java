/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA;

import Entity.ApplicationPages;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.PagePermission;
import JPA.exceptions.IllegalOrphanException;
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
public class ApplicationPagesJpaController implements Serializable {

    public ApplicationPagesJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ApplicationPages applicationPages) {
        if (applicationPages.getPagePermissionList() == null) {
            applicationPages.setPagePermissionList(new ArrayList<PagePermission>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PagePermission> attachedPagePermissionList = new ArrayList<PagePermission>();
            for (PagePermission pagePermissionListPagePermissionToAttach : applicationPages.getPagePermissionList()) {
                pagePermissionListPagePermissionToAttach = em.getReference(pagePermissionListPagePermissionToAttach.getClass(), pagePermissionListPagePermissionToAttach.getPId());
                attachedPagePermissionList.add(pagePermissionListPagePermissionToAttach);
            }
            applicationPages.setPagePermissionList(attachedPagePermissionList);
            em.persist(applicationPages);
            for (PagePermission pagePermissionListPagePermission : applicationPages.getPagePermissionList()) {
                ApplicationPages oldPageNameOfPagePermissionListPagePermission = pagePermissionListPagePermission.getPageName();
                pagePermissionListPagePermission.setPageName(applicationPages);
                pagePermissionListPagePermission = em.merge(pagePermissionListPagePermission);
                if (oldPageNameOfPagePermissionListPagePermission != null) {
                    oldPageNameOfPagePermissionListPagePermission.getPagePermissionList().remove(pagePermissionListPagePermission);
                    oldPageNameOfPagePermissionListPagePermission = em.merge(oldPageNameOfPagePermissionListPagePermission);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editAppPages(ApplicationPages applicationPages) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(applicationPages);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                if (findApplicationPages(applicationPages.getPageID()) == null) {
                    throw new NonexistentEntityException("The docTypes with id " + applicationPages.getPageID() + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ApplicationPages applicationPages) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ApplicationPages persistentApplicationPages = em.find(ApplicationPages.class, applicationPages.getPageID());
            List<PagePermission> pagePermissionListOld = persistentApplicationPages.getPagePermissionList();
            List<PagePermission> pagePermissionListNew = applicationPages.getPagePermissionList();
            List<String> illegalOrphanMessages = null;
            for (PagePermission pagePermissionListOldPagePermission : pagePermissionListOld) {
                if (!pagePermissionListNew.contains(pagePermissionListOldPagePermission)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PagePermission " + pagePermissionListOldPagePermission + " since its pageName field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PagePermission> attachedPagePermissionListNew = new ArrayList<PagePermission>();
            for (PagePermission pagePermissionListNewPagePermissionToAttach : pagePermissionListNew) {
                pagePermissionListNewPagePermissionToAttach = em.getReference(pagePermissionListNewPagePermissionToAttach.getClass(), pagePermissionListNewPagePermissionToAttach.getPId());
                attachedPagePermissionListNew.add(pagePermissionListNewPagePermissionToAttach);
            }
            pagePermissionListNew = attachedPagePermissionListNew;
            applicationPages.setPagePermissionList(pagePermissionListNew);
            applicationPages = em.merge(applicationPages);
            for (PagePermission pagePermissionListNewPagePermission : pagePermissionListNew) {
                if (!pagePermissionListOld.contains(pagePermissionListNewPagePermission)) {
                    ApplicationPages oldPageNameOfPagePermissionListNewPagePermission = pagePermissionListNewPagePermission.getPageName();
                    pagePermissionListNewPagePermission.setPageName(applicationPages);
                    pagePermissionListNewPagePermission = em.merge(pagePermissionListNewPagePermission);
                    if (oldPageNameOfPagePermissionListNewPagePermission != null && !oldPageNameOfPagePermissionListNewPagePermission.equals(applicationPages)) {
                        oldPageNameOfPagePermissionListNewPagePermission.getPagePermissionList().remove(pagePermissionListNewPagePermission);
                        oldPageNameOfPagePermissionListNewPagePermission = em.merge(oldPageNameOfPagePermissionListNewPagePermission);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = applicationPages.getPageID();
                if (findApplicationPages(id) == null) {
                    throw new NonexistentEntityException("The applicationPages with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ApplicationPages applicationPages;
            try {
                applicationPages = em.getReference(ApplicationPages.class, id);
                applicationPages.getPageID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The applicationPages with id " + id + " no longer exists.", enfe);
            }
//            List<String> illegalOrphanMessages = null;
//            List<PagePermission> pagePermissionListOrphanCheck = applicationPages.getPagePermissionList();
//            for (PagePermission pagePermissionListOrphanCheckPagePermission : pagePermissionListOrphanCheck) {
//                if (illegalOrphanMessages == null) {
//                    illegalOrphanMessages = new ArrayList<String>();
//                }
//                illegalOrphanMessages.add("This ApplicationPages (" + applicationPages + ") cannot be destroyed since the PagePermission " + pagePermissionListOrphanCheckPagePermission + " in its pagePermissionList field has a non-nullable pageName field.");
//            }
//            if (illegalOrphanMessages != null) {
//                throw new IllegalOrphanException(illegalOrphanMessages);
//            }
            em.remove(applicationPages);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public boolean pageNameExist(String pageName){
       ApplicationPages ap=null;
       EntityManager em=getEntityManager();
       Query q=em.createNamedQuery("ApplicationPages.findByAppPageName");
       q=q.setParameter("appPageName", pageName);
       
       try{
           q.getSingleResult();
       }catch(Exception e){
           return false;
       }
       
       return true;
    }

    public List<ApplicationPages> findApplicationPagesEntities() {
        return findApplicationPagesEntities(true, -1, -1);
    }

    public List<ApplicationPages> findApplicationPagesEntities(int maxResults, int firstResult) {
        return findApplicationPagesEntities(false, maxResults, firstResult);
    }

    private List<ApplicationPages> findApplicationPagesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ApplicationPages.class));
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

    public ApplicationPages findApplicationPages(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ApplicationPages.class, id);
        } finally {
            em.close();
        }
    }

    public int getApplicationPagesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ApplicationPages> rt = cq.from(ApplicationPages.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
