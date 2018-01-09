/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JPA;

import Entity.Groups;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.UserGroup;
import java.util.ArrayList;
import java.util.List;
import Entity.PagePermission;
import Entity.Permissions;
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
public class GroupsJpaController implements Serializable {

    public GroupsJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Groups groups) throws PreexistingEntityException, Exception {
        if (groups.getUserGroupList() == null) {
            groups.setUserGroupList(new ArrayList<UserGroup>());
        }
        if (groups.getPagePermissionList() == null) {
            groups.setPagePermissionList(new ArrayList<PagePermission>());
        }
        if (groups.getPermissionsList() == null) {
            groups.setPermissionsList(new ArrayList<Permissions>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<UserGroup> attachedUserGroupList = new ArrayList<UserGroup>();
            for (UserGroup userGroupListUserGroupToAttach : groups.getUserGroupList()) {
                userGroupListUserGroupToAttach = em.getReference(userGroupListUserGroupToAttach.getClass(), userGroupListUserGroupToAttach.getUgId());
                attachedUserGroupList.add(userGroupListUserGroupToAttach);
            }
            groups.setUserGroupList(attachedUserGroupList);
            List<PagePermission> attachedPagePermissionList = new ArrayList<PagePermission>();
            for (PagePermission pagePermissionListPagePermissionToAttach : groups.getPagePermissionList()) {
                pagePermissionListPagePermissionToAttach = em.getReference(pagePermissionListPagePermissionToAttach.getClass(), pagePermissionListPagePermissionToAttach.getPId());
                attachedPagePermissionList.add(pagePermissionListPagePermissionToAttach);
            }
            groups.setPagePermissionList(attachedPagePermissionList);
            List<Permissions> attachedPermissionsList = new ArrayList<Permissions>();
            for (Permissions permissionsListPermissionsToAttach : groups.getPermissionsList()) {
                permissionsListPermissionsToAttach = em.getReference(permissionsListPermissionsToAttach.getClass(), permissionsListPermissionsToAttach.getPermID());
                attachedPermissionsList.add(permissionsListPermissionsToAttach);
            }
            groups.setPermissionsList(attachedPermissionsList);
            em.persist(groups);
            for (UserGroup userGroupListUserGroup : groups.getUserGroupList()) {
                Groups oldGroupIdOfUserGroupListUserGroup = userGroupListUserGroup.getGroupId();
                userGroupListUserGroup.setGroupId(groups);
                userGroupListUserGroup = em.merge(userGroupListUserGroup);
                if (oldGroupIdOfUserGroupListUserGroup != null) {
                    oldGroupIdOfUserGroupListUserGroup.getUserGroupList().remove(userGroupListUserGroup);
                    oldGroupIdOfUserGroupListUserGroup = em.merge(oldGroupIdOfUserGroupListUserGroup);
                }
            }
            for (PagePermission pagePermissionListPagePermission : groups.getPagePermissionList()) {
                Groups oldGroupIDOfPagePermissionListPagePermission = pagePermissionListPagePermission.getGroupID();
                pagePermissionListPagePermission.setGroupID(groups);
                pagePermissionListPagePermission = em.merge(pagePermissionListPagePermission);
                if (oldGroupIDOfPagePermissionListPagePermission != null) {
                    oldGroupIDOfPagePermissionListPagePermission.getPagePermissionList().remove(pagePermissionListPagePermission);
                    oldGroupIDOfPagePermissionListPagePermission = em.merge(oldGroupIDOfPagePermissionListPagePermission);
                }
            }
            for (Permissions permissionsListPermissions : groups.getPermissionsList()) {
                Groups oldGroupIdOfPermissionsListPermissions = permissionsListPermissions.getGroupId();
                permissionsListPermissions.setGroupId(groups);
                permissionsListPermissions = em.merge(permissionsListPermissions);
                if (oldGroupIdOfPermissionsListPermissions != null) {
                    oldGroupIdOfPermissionsListPermissions.getPermissionsList().remove(permissionsListPermissions);
                    oldGroupIdOfPermissionsListPermissions = em.merge(oldGroupIdOfPermissionsListPermissions);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGroups(groups.getGroupId()) != null) {
                throw new PreexistingEntityException("Groups " + groups + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void ManageEdit(Groups groups) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(groups);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = groups.getGroupId();
                if (findGroups(id) == null) {
                    throw new NonexistentEntityException("The groups with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Groups groups) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Groups persistentGroups = em.find(Groups.class, groups.getGroupId());
            List<UserGroup> userGroupListOld = persistentGroups.getUserGroupList();
            List<UserGroup> userGroupListNew = groups.getUserGroupList();
            List<PagePermission> pagePermissionListOld = persistentGroups.getPagePermissionList();
            List<PagePermission> pagePermissionListNew = groups.getPagePermissionList();
            List<Permissions> permissionsListOld = persistentGroups.getPermissionsList();
            List<Permissions> permissionsListNew = groups.getPermissionsList();
            List<String> illegalOrphanMessages = null;
            for (UserGroup userGroupListOldUserGroup : userGroupListOld) {
                if (!userGroupListNew.contains(userGroupListOldUserGroup)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserGroup " + userGroupListOldUserGroup + " since its groupId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<UserGroup> attachedUserGroupListNew = new ArrayList<UserGroup>();
            for (UserGroup userGroupListNewUserGroupToAttach : userGroupListNew) {
                userGroupListNewUserGroupToAttach = em.getReference(userGroupListNewUserGroupToAttach.getClass(), userGroupListNewUserGroupToAttach.getUgId());
                attachedUserGroupListNew.add(userGroupListNewUserGroupToAttach);
            }
            userGroupListNew = attachedUserGroupListNew;
            groups.setUserGroupList(userGroupListNew);
            List<PagePermission> attachedPagePermissionListNew = new ArrayList<PagePermission>();
            for (PagePermission pagePermissionListNewPagePermissionToAttach : pagePermissionListNew) {
                pagePermissionListNewPagePermissionToAttach = em.getReference(pagePermissionListNewPagePermissionToAttach.getClass(), pagePermissionListNewPagePermissionToAttach.getPId());
                attachedPagePermissionListNew.add(pagePermissionListNewPagePermissionToAttach);
            }
            pagePermissionListNew = attachedPagePermissionListNew;
            groups.setPagePermissionList(pagePermissionListNew);
            List<Permissions> attachedPermissionsListNew = new ArrayList<Permissions>();
            for (Permissions permissionsListNewPermissionsToAttach : permissionsListNew) {
                permissionsListNewPermissionsToAttach = em.getReference(permissionsListNewPermissionsToAttach.getClass(), permissionsListNewPermissionsToAttach.getPermID());
                attachedPermissionsListNew.add(permissionsListNewPermissionsToAttach);
            }
            permissionsListNew = attachedPermissionsListNew;
            groups.setPermissionsList(permissionsListNew);
            groups = em.merge(groups);
            for (UserGroup userGroupListNewUserGroup : userGroupListNew) {
                if (!userGroupListOld.contains(userGroupListNewUserGroup)) {
                    Groups oldGroupIdOfUserGroupListNewUserGroup = userGroupListNewUserGroup.getGroupId();
                    userGroupListNewUserGroup.setGroupId(groups);
                    userGroupListNewUserGroup = em.merge(userGroupListNewUserGroup);
                    if (oldGroupIdOfUserGroupListNewUserGroup != null && !oldGroupIdOfUserGroupListNewUserGroup.equals(groups)) {
                        oldGroupIdOfUserGroupListNewUserGroup.getUserGroupList().remove(userGroupListNewUserGroup);
                        oldGroupIdOfUserGroupListNewUserGroup = em.merge(oldGroupIdOfUserGroupListNewUserGroup);
                    }
                }
            }
            for (PagePermission pagePermissionListOldPagePermission : pagePermissionListOld) {
                if (!pagePermissionListNew.contains(pagePermissionListOldPagePermission)) {
                    pagePermissionListOldPagePermission.setGroupID(null);
                    pagePermissionListOldPagePermission = em.merge(pagePermissionListOldPagePermission);
                }
            }
            for (PagePermission pagePermissionListNewPagePermission : pagePermissionListNew) {
                if (!pagePermissionListOld.contains(pagePermissionListNewPagePermission)) {
                    Groups oldGroupIDOfPagePermissionListNewPagePermission = pagePermissionListNewPagePermission.getGroupID();
                    pagePermissionListNewPagePermission.setGroupID(groups);
                    pagePermissionListNewPagePermission = em.merge(pagePermissionListNewPagePermission);
                    if (oldGroupIDOfPagePermissionListNewPagePermission != null && !oldGroupIDOfPagePermissionListNewPagePermission.equals(groups)) {
                        oldGroupIDOfPagePermissionListNewPagePermission.getPagePermissionList().remove(pagePermissionListNewPagePermission);
                        oldGroupIDOfPagePermissionListNewPagePermission = em.merge(oldGroupIDOfPagePermissionListNewPagePermission);
                    }
                }
            }
            for (Permissions permissionsListOldPermissions : permissionsListOld) {
                if (!permissionsListNew.contains(permissionsListOldPermissions)) {
                    permissionsListOldPermissions.setGroupId(null);
                    permissionsListOldPermissions = em.merge(permissionsListOldPermissions);
                }
            }
            for (Permissions permissionsListNewPermissions : permissionsListNew) {
                if (!permissionsListOld.contains(permissionsListNewPermissions)) {
                    Groups oldGroupIdOfPermissionsListNewPermissions = permissionsListNewPermissions.getGroupId();
                    permissionsListNewPermissions.setGroupId(groups);
                    permissionsListNewPermissions = em.merge(permissionsListNewPermissions);
                    if (oldGroupIdOfPermissionsListNewPermissions != null && !oldGroupIdOfPermissionsListNewPermissions.equals(groups)) {
                        oldGroupIdOfPermissionsListNewPermissions.getPermissionsList().remove(permissionsListNewPermissions);
                        oldGroupIdOfPermissionsListNewPermissions = em.merge(oldGroupIdOfPermissionsListNewPermissions);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = groups.getGroupId();
                if (findGroups(id) == null) {
                    throw new NonexistentEntityException("The groups with id " + id + " no longer exists.");
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
            Groups groups;
            try {
                groups = em.getReference(Groups.class, id);
                groups.getGroupId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The groups with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<UserGroup> userGroupListOrphanCheck = groups.getUserGroupList();
            for (UserGroup userGroupListOrphanCheckUserGroup : userGroupListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Groups (" + groups + ") cannot be destroyed since the UserGroup " + userGroupListOrphanCheckUserGroup + " in its userGroupList field has a non-nullable groupId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PagePermission> pagePermissionList = groups.getPagePermissionList();
            for (PagePermission pagePermissionListPagePermission : pagePermissionList) {
                pagePermissionListPagePermission.setGroupID(null);
                pagePermissionListPagePermission = em.merge(pagePermissionListPagePermission);
            }
            List<Permissions> permissionsList = groups.getPermissionsList();
            for (Permissions permissionsListPermissions : permissionsList) {
                permissionsListPermissions.setGroupId(null);
                permissionsListPermissions = em.merge(permissionsListPermissions);
            }
            em.remove(groups);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Groups> findGroupsEntities() {
        return findGroupsEntities(true, -1, -1);
    }

    public List<Groups> findGroupsEntities(int maxResults, int firstResult) {
        return findGroupsEntities(false, maxResults, firstResult);
    }

    private List<Groups> findGroupsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Groups.class));
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

    public Groups findGroups(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Groups.class, id);
        } finally {
            em.close();
        }
    }

    public boolean isGroupExist(String groupName){
        
        EntityManager em=getEntityManager();
        Query q=em.createNamedQuery("Groups.findByGroupName");
        try{
        q=q.setParameter("groupName", groupName);
        q.getResultList();
        if( q.getResultList().isEmpty()){return false;}
         return true;
        }catch(Exception e){
            
            return false;
        }
       
    }
    public int getGroupsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Groups> rt = cq.from(Groups.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
