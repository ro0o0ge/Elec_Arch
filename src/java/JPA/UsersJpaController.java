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
import Entity.UserGroup;
import java.util.ArrayList;
import java.util.List;
import Entity.Documents;
import Entity.TransactionLog;
import Entity.DocTypes;
import Entity.PagePermission;
import Entity.Permissions;
import Entity.ApprovalRequest;
import Entity.Users;
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
public class UsersJpaController implements Serializable {

    public UsersJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws PreexistingEntityException, Exception {
        if (users.getUserGroupList() == null) {
            users.setUserGroupList(new ArrayList<UserGroup>());
        }
        if (users.getDocumentsList() == null) {
            users.setDocumentsList(new ArrayList<Documents>());
        }
        if (users.getTransactionLogList() == null) {
            users.setTransactionLogList(new ArrayList<TransactionLog>());
        }
        if (users.getDocTypesList() == null) {
            users.setDocTypesList(new ArrayList<DocTypes>());
        }
        if (users.getPagePermissionList() == null) {
            users.setPagePermissionList(new ArrayList<PagePermission>());
        }
        if (users.getPermissionsList() == null) {
            users.setPermissionsList(new ArrayList<Permissions>());
        }
        if (users.getApprovalRequestList() == null) {
            users.setApprovalRequestList(new ArrayList<ApprovalRequest>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<UserGroup> attachedUserGroupList = new ArrayList<UserGroup>();
            for (UserGroup userGroupListUserGroupToAttach : users.getUserGroupList()) {
                userGroupListUserGroupToAttach = em.getReference(userGroupListUserGroupToAttach.getClass(), userGroupListUserGroupToAttach.getUgId());
                attachedUserGroupList.add(userGroupListUserGroupToAttach);
            }
            users.setUserGroupList(attachedUserGroupList);
            List<Documents> attachedDocumentsList = new ArrayList<Documents>();
            for (Documents documentsListDocumentsToAttach : users.getDocumentsList()) {
                documentsListDocumentsToAttach = em.getReference(documentsListDocumentsToAttach.getClass(), documentsListDocumentsToAttach.getDocSer());
                attachedDocumentsList.add(documentsListDocumentsToAttach);
            }
            users.setDocumentsList(attachedDocumentsList);
            List<TransactionLog> attachedTransactionLogList = new ArrayList<TransactionLog>();
            for (TransactionLog transactionLogListTransactionLogToAttach : users.getTransactionLogList()) {
                transactionLogListTransactionLogToAttach = em.getReference(transactionLogListTransactionLogToAttach.getClass(), transactionLogListTransactionLogToAttach.getLogId());
                attachedTransactionLogList.add(transactionLogListTransactionLogToAttach);
            }
            users.setTransactionLogList(attachedTransactionLogList);
            List<DocTypes> attachedDocTypesList = new ArrayList<DocTypes>();
            for (DocTypes docTypesListDocTypesToAttach : users.getDocTypesList()) {
                docTypesListDocTypesToAttach = em.getReference(docTypesListDocTypesToAttach.getClass(), docTypesListDocTypesToAttach.getDoctypeId());
                attachedDocTypesList.add(docTypesListDocTypesToAttach);
            }
            users.setDocTypesList(attachedDocTypesList);
            List<PagePermission> attachedPagePermissionList = new ArrayList<PagePermission>();
            for (PagePermission pagePermissionListPagePermissionToAttach : users.getPagePermissionList()) {
                pagePermissionListPagePermissionToAttach = em.getReference(pagePermissionListPagePermissionToAttach.getClass(), pagePermissionListPagePermissionToAttach.getPId());
                attachedPagePermissionList.add(pagePermissionListPagePermissionToAttach);
            }
            users.setPagePermissionList(attachedPagePermissionList);
            List<Permissions> attachedPermissionsList = new ArrayList<Permissions>();
            for (Permissions permissionsListPermissionsToAttach : users.getPermissionsList()) {
                permissionsListPermissionsToAttach = em.getReference(permissionsListPermissionsToAttach.getClass(), permissionsListPermissionsToAttach.getPermID());
                attachedPermissionsList.add(permissionsListPermissionsToAttach);
            }
            users.setPermissionsList(attachedPermissionsList);
            List<ApprovalRequest> attachedApprovalRequestList = new ArrayList<ApprovalRequest>();
            for (ApprovalRequest approvalRequestListApprovalRequestToAttach : users.getApprovalRequestList()) {
                approvalRequestListApprovalRequestToAttach = em.getReference(approvalRequestListApprovalRequestToAttach.getClass(), approvalRequestListApprovalRequestToAttach.getAppID());
                attachedApprovalRequestList.add(approvalRequestListApprovalRequestToAttach);
            }
            users.setApprovalRequestList(attachedApprovalRequestList);
            em.persist(users);
            for (UserGroup userGroupListUserGroup : users.getUserGroupList()) {
                Users oldUserIdOfUserGroupListUserGroup = userGroupListUserGroup.getUserId();
                userGroupListUserGroup.setUserId(users);
                userGroupListUserGroup = em.merge(userGroupListUserGroup);
                if (oldUserIdOfUserGroupListUserGroup != null) {
                    oldUserIdOfUserGroupListUserGroup.getUserGroupList().remove(userGroupListUserGroup);
                    oldUserIdOfUserGroupListUserGroup = em.merge(oldUserIdOfUserGroupListUserGroup);
                }
            }
            for (Documents documentsListDocuments : users.getDocumentsList()) {
                Users oldUserIdOfDocumentsListDocuments = documentsListDocuments.getUserId();
                documentsListDocuments.setUserId(users);
                documentsListDocuments = em.merge(documentsListDocuments);
                if (oldUserIdOfDocumentsListDocuments != null) {
                    oldUserIdOfDocumentsListDocuments.getDocumentsList().remove(documentsListDocuments);
                    oldUserIdOfDocumentsListDocuments = em.merge(oldUserIdOfDocumentsListDocuments);
                }
            }
            for (TransactionLog transactionLogListTransactionLog : users.getTransactionLogList()) {
                Users oldUserIdOfTransactionLogListTransactionLog = transactionLogListTransactionLog.getUserId();
                transactionLogListTransactionLog.setUserId(users);
                transactionLogListTransactionLog = em.merge(transactionLogListTransactionLog);
                if (oldUserIdOfTransactionLogListTransactionLog != null) {
                    oldUserIdOfTransactionLogListTransactionLog.getTransactionLogList().remove(transactionLogListTransactionLog);
                    oldUserIdOfTransactionLogListTransactionLog = em.merge(oldUserIdOfTransactionLogListTransactionLog);
                }
            }
            for (DocTypes docTypesListDocTypes : users.getDocTypesList()) {
                Users oldApproverIDOfDocTypesListDocTypes = docTypesListDocTypes.getApproverID();
                docTypesListDocTypes.setApproverID(users);
                docTypesListDocTypes = em.merge(docTypesListDocTypes);
                if (oldApproverIDOfDocTypesListDocTypes != null) {
                    oldApproverIDOfDocTypesListDocTypes.getDocTypesList().remove(docTypesListDocTypes);
                    oldApproverIDOfDocTypesListDocTypes = em.merge(oldApproverIDOfDocTypesListDocTypes);
                }
            }
            for (PagePermission pagePermissionListPagePermission : users.getPagePermissionList()) {
                Users oldUserIDOfPagePermissionListPagePermission = pagePermissionListPagePermission.getUserID();
                pagePermissionListPagePermission.setUserID(users);
                pagePermissionListPagePermission = em.merge(pagePermissionListPagePermission);
                if (oldUserIDOfPagePermissionListPagePermission != null) {
                    oldUserIDOfPagePermissionListPagePermission.getPagePermissionList().remove(pagePermissionListPagePermission);
                    oldUserIDOfPagePermissionListPagePermission = em.merge(oldUserIDOfPagePermissionListPagePermission);
                }
            }
            for (Permissions permissionsListPermissions : users.getPermissionsList()) {
                Users oldUserIdOfPermissionsListPermissions = permissionsListPermissions.getUserId();
                permissionsListPermissions.setUserId(users);
                permissionsListPermissions = em.merge(permissionsListPermissions);
                if (oldUserIdOfPermissionsListPermissions != null) {
                    oldUserIdOfPermissionsListPermissions.getPermissionsList().remove(permissionsListPermissions);
                    oldUserIdOfPermissionsListPermissions = em.merge(oldUserIdOfPermissionsListPermissions);
                }
            }
            for (ApprovalRequest approvalRequestListApprovalRequest : users.getApprovalRequestList()) {
                Users oldRUserIDOfApprovalRequestListApprovalRequest = approvalRequestListApprovalRequest.getRUserID();
                approvalRequestListApprovalRequest.setRUserID(users);
                approvalRequestListApprovalRequest = em.merge(approvalRequestListApprovalRequest);
                if (oldRUserIDOfApprovalRequestListApprovalRequest != null) {
                    oldRUserIDOfApprovalRequestListApprovalRequest.getApprovalRequestList().remove(approvalRequestListApprovalRequest);
                    oldRUserIDOfApprovalRequestListApprovalRequest = em.merge(oldRUserIDOfApprovalRequestListApprovalRequest);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsers(users.getUserId()) != null) {
                throw new PreexistingEntityException("Users " + users + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void ManageEdit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(users);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = users.getUserId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The User with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getUserId());
            List<UserGroup> userGroupListOld = persistentUsers.getUserGroupList();
            List<UserGroup> userGroupListNew = users.getUserGroupList();
            List<Documents> documentsListOld = persistentUsers.getDocumentsList();
            List<Documents> documentsListNew = users.getDocumentsList();
            List<TransactionLog> transactionLogListOld = persistentUsers.getTransactionLogList();
            List<TransactionLog> transactionLogListNew = users.getTransactionLogList();
            List<DocTypes> docTypesListOld = persistentUsers.getDocTypesList();
            List<DocTypes> docTypesListNew = users.getDocTypesList();
            List<PagePermission> pagePermissionListOld = persistentUsers.getPagePermissionList();
            List<PagePermission> pagePermissionListNew = users.getPagePermissionList();
            List<Permissions> permissionsListOld = persistentUsers.getPermissionsList();
            List<Permissions> permissionsListNew = users.getPermissionsList();
            List<ApprovalRequest> approvalRequestListOld = persistentUsers.getApprovalRequestList();
            List<ApprovalRequest> approvalRequestListNew = users.getApprovalRequestList();
            List<String> illegalOrphanMessages = null;
            for (UserGroup userGroupListOldUserGroup : userGroupListOld) {
                if (!userGroupListNew.contains(userGroupListOldUserGroup)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UserGroup " + userGroupListOldUserGroup + " since its userId field is not nullable.");
                }
            }
            for (Documents documentsListOldDocuments : documentsListOld) {
                if (!documentsListNew.contains(documentsListOldDocuments)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documents " + documentsListOldDocuments + " since its userId field is not nullable.");
                }
            }
            for (ApprovalRequest approvalRequestListOldApprovalRequest : approvalRequestListOld) {
                if (!approvalRequestListNew.contains(approvalRequestListOldApprovalRequest)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ApprovalRequest " + approvalRequestListOldApprovalRequest + " since its RUserID field is not nullable.");
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
            users.setUserGroupList(userGroupListNew);
            List<Documents> attachedDocumentsListNew = new ArrayList<Documents>();
            for (Documents documentsListNewDocumentsToAttach : documentsListNew) {
                documentsListNewDocumentsToAttach = em.getReference(documentsListNewDocumentsToAttach.getClass(), documentsListNewDocumentsToAttach.getDocSer());
                attachedDocumentsListNew.add(documentsListNewDocumentsToAttach);
            }
            documentsListNew = attachedDocumentsListNew;
            users.setDocumentsList(documentsListNew);
            List<TransactionLog> attachedTransactionLogListNew = new ArrayList<TransactionLog>();
            for (TransactionLog transactionLogListNewTransactionLogToAttach : transactionLogListNew) {
                transactionLogListNewTransactionLogToAttach = em.getReference(transactionLogListNewTransactionLogToAttach.getClass(), transactionLogListNewTransactionLogToAttach.getLogId());
                attachedTransactionLogListNew.add(transactionLogListNewTransactionLogToAttach);
            }
            transactionLogListNew = attachedTransactionLogListNew;
            users.setTransactionLogList(transactionLogListNew);
            List<DocTypes> attachedDocTypesListNew = new ArrayList<DocTypes>();
            for (DocTypes docTypesListNewDocTypesToAttach : docTypesListNew) {
                docTypesListNewDocTypesToAttach = em.getReference(docTypesListNewDocTypesToAttach.getClass(), docTypesListNewDocTypesToAttach.getDoctypeId());
                attachedDocTypesListNew.add(docTypesListNewDocTypesToAttach);
            }
            docTypesListNew = attachedDocTypesListNew;
            users.setDocTypesList(docTypesListNew);
            List<PagePermission> attachedPagePermissionListNew = new ArrayList<PagePermission>();
            for (PagePermission pagePermissionListNewPagePermissionToAttach : pagePermissionListNew) {
                pagePermissionListNewPagePermissionToAttach = em.getReference(pagePermissionListNewPagePermissionToAttach.getClass(), pagePermissionListNewPagePermissionToAttach.getPId());
                attachedPagePermissionListNew.add(pagePermissionListNewPagePermissionToAttach);
            }
            pagePermissionListNew = attachedPagePermissionListNew;
            users.setPagePermissionList(pagePermissionListNew);
            List<Permissions> attachedPermissionsListNew = new ArrayList<Permissions>();
            for (Permissions permissionsListNewPermissionsToAttach : permissionsListNew) {
                permissionsListNewPermissionsToAttach = em.getReference(permissionsListNewPermissionsToAttach.getClass(), permissionsListNewPermissionsToAttach.getPermID());
                attachedPermissionsListNew.add(permissionsListNewPermissionsToAttach);
            }
            permissionsListNew = attachedPermissionsListNew;
            users.setPermissionsList(permissionsListNew);
            List<ApprovalRequest> attachedApprovalRequestListNew = new ArrayList<ApprovalRequest>();
            for (ApprovalRequest approvalRequestListNewApprovalRequestToAttach : approvalRequestListNew) {
                approvalRequestListNewApprovalRequestToAttach = em.getReference(approvalRequestListNewApprovalRequestToAttach.getClass(), approvalRequestListNewApprovalRequestToAttach.getAppID());
                attachedApprovalRequestListNew.add(approvalRequestListNewApprovalRequestToAttach);
            }
            approvalRequestListNew = attachedApprovalRequestListNew;
            users.setApprovalRequestList(approvalRequestListNew);
            users = em.merge(users);
            for (UserGroup userGroupListNewUserGroup : userGroupListNew) {
                if (!userGroupListOld.contains(userGroupListNewUserGroup)) {
                    Users oldUserIdOfUserGroupListNewUserGroup = userGroupListNewUserGroup.getUserId();
                    userGroupListNewUserGroup.setUserId(users);
                    userGroupListNewUserGroup = em.merge(userGroupListNewUserGroup);
                    if (oldUserIdOfUserGroupListNewUserGroup != null && !oldUserIdOfUserGroupListNewUserGroup.equals(users)) {
                        oldUserIdOfUserGroupListNewUserGroup.getUserGroupList().remove(userGroupListNewUserGroup);
                        oldUserIdOfUserGroupListNewUserGroup = em.merge(oldUserIdOfUserGroupListNewUserGroup);
                    }
                }
            }
            for (Documents documentsListNewDocuments : documentsListNew) {
                if (!documentsListOld.contains(documentsListNewDocuments)) {
                    Users oldUserIdOfDocumentsListNewDocuments = documentsListNewDocuments.getUserId();
                    documentsListNewDocuments.setUserId(users);
                    documentsListNewDocuments = em.merge(documentsListNewDocuments);
                    if (oldUserIdOfDocumentsListNewDocuments != null && !oldUserIdOfDocumentsListNewDocuments.equals(users)) {
                        oldUserIdOfDocumentsListNewDocuments.getDocumentsList().remove(documentsListNewDocuments);
                        oldUserIdOfDocumentsListNewDocuments = em.merge(oldUserIdOfDocumentsListNewDocuments);
                    }
                }
            }
            for (TransactionLog transactionLogListOldTransactionLog : transactionLogListOld) {
                if (!transactionLogListNew.contains(transactionLogListOldTransactionLog)) {
                    transactionLogListOldTransactionLog.setUserId(null);
                    transactionLogListOldTransactionLog = em.merge(transactionLogListOldTransactionLog);
                }
            }
            for (TransactionLog transactionLogListNewTransactionLog : transactionLogListNew) {
                if (!transactionLogListOld.contains(transactionLogListNewTransactionLog)) {
                    Users oldUserIdOfTransactionLogListNewTransactionLog = transactionLogListNewTransactionLog.getUserId();
                    transactionLogListNewTransactionLog.setUserId(users);
                    transactionLogListNewTransactionLog = em.merge(transactionLogListNewTransactionLog);
                    if (oldUserIdOfTransactionLogListNewTransactionLog != null && !oldUserIdOfTransactionLogListNewTransactionLog.equals(users)) {
                        oldUserIdOfTransactionLogListNewTransactionLog.getTransactionLogList().remove(transactionLogListNewTransactionLog);
                        oldUserIdOfTransactionLogListNewTransactionLog = em.merge(oldUserIdOfTransactionLogListNewTransactionLog);
                    }
                }
            }
            for (DocTypes docTypesListOldDocTypes : docTypesListOld) {
                if (!docTypesListNew.contains(docTypesListOldDocTypes)) {
                    docTypesListOldDocTypes.setApproverID(null);
                    docTypesListOldDocTypes = em.merge(docTypesListOldDocTypes);
                }
            }
            for (DocTypes docTypesListNewDocTypes : docTypesListNew) {
                if (!docTypesListOld.contains(docTypesListNewDocTypes)) {
                    Users oldApproverIDOfDocTypesListNewDocTypes = docTypesListNewDocTypes.getApproverID();
                    docTypesListNewDocTypes.setApproverID(users);
                    docTypesListNewDocTypes = em.merge(docTypesListNewDocTypes);
                    if (oldApproverIDOfDocTypesListNewDocTypes != null && !oldApproverIDOfDocTypesListNewDocTypes.equals(users)) {
                        oldApproverIDOfDocTypesListNewDocTypes.getDocTypesList().remove(docTypesListNewDocTypes);
                        oldApproverIDOfDocTypesListNewDocTypes = em.merge(oldApproverIDOfDocTypesListNewDocTypes);
                    }
                }
            }
            for (PagePermission pagePermissionListOldPagePermission : pagePermissionListOld) {
                if (!pagePermissionListNew.contains(pagePermissionListOldPagePermission)) {
                    pagePermissionListOldPagePermission.setUserID(null);
                    pagePermissionListOldPagePermission = em.merge(pagePermissionListOldPagePermission);
                }
            }
            for (PagePermission pagePermissionListNewPagePermission : pagePermissionListNew) {
                if (!pagePermissionListOld.contains(pagePermissionListNewPagePermission)) {
                    Users oldUserIDOfPagePermissionListNewPagePermission = pagePermissionListNewPagePermission.getUserID();
                    pagePermissionListNewPagePermission.setUserID(users);
                    pagePermissionListNewPagePermission = em.merge(pagePermissionListNewPagePermission);
                    if (oldUserIDOfPagePermissionListNewPagePermission != null && !oldUserIDOfPagePermissionListNewPagePermission.equals(users)) {
                        oldUserIDOfPagePermissionListNewPagePermission.getPagePermissionList().remove(pagePermissionListNewPagePermission);
                        oldUserIDOfPagePermissionListNewPagePermission = em.merge(oldUserIDOfPagePermissionListNewPagePermission);
                    }
                }
            }
            for (Permissions permissionsListOldPermissions : permissionsListOld) {
                if (!permissionsListNew.contains(permissionsListOldPermissions)) {
                    permissionsListOldPermissions.setUserId(null);
                    permissionsListOldPermissions = em.merge(permissionsListOldPermissions);
                }
            }
            for (Permissions permissionsListNewPermissions : permissionsListNew) {
                if (!permissionsListOld.contains(permissionsListNewPermissions)) {
                    Users oldUserIdOfPermissionsListNewPermissions = permissionsListNewPermissions.getUserId();
                    permissionsListNewPermissions.setUserId(users);
                    permissionsListNewPermissions = em.merge(permissionsListNewPermissions);
                    if (oldUserIdOfPermissionsListNewPermissions != null && !oldUserIdOfPermissionsListNewPermissions.equals(users)) {
                        oldUserIdOfPermissionsListNewPermissions.getPermissionsList().remove(permissionsListNewPermissions);
                        oldUserIdOfPermissionsListNewPermissions = em.merge(oldUserIdOfPermissionsListNewPermissions);
                    }
                }
            }
            for (ApprovalRequest approvalRequestListNewApprovalRequest : approvalRequestListNew) {
                if (!approvalRequestListOld.contains(approvalRequestListNewApprovalRequest)) {
                    Users oldRUserIDOfApprovalRequestListNewApprovalRequest = approvalRequestListNewApprovalRequest.getRUserID();
                    approvalRequestListNewApprovalRequest.setRUserID(users);
                    approvalRequestListNewApprovalRequest = em.merge(approvalRequestListNewApprovalRequest);
                    if (oldRUserIDOfApprovalRequestListNewApprovalRequest != null && !oldRUserIDOfApprovalRequestListNewApprovalRequest.equals(users)) {
                        oldRUserIDOfApprovalRequestListNewApprovalRequest.getApprovalRequestList().remove(approvalRequestListNewApprovalRequest);
                        oldRUserIDOfApprovalRequestListNewApprovalRequest = em.merge(oldRUserIDOfApprovalRequestListNewApprovalRequest);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = users.getUserId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<String> destroy(int id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        List<String> illegalOrphanMessages = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUserId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            
            List<UserGroup> userGroupListOrphanCheck = users.getUserGroupList();
            for (UserGroup userGroupListOrphanCheckUserGroup : userGroupListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (\n" + users.getUserName() + "\n) cannot be destroyed since the UserGroup \n" + userGroupListOrphanCheckUserGroup.getGroupId().getGroupName() + "\n in its userGroupList field has a non-nullable userId field.");
            }
            List<Documents> documentsListOrphanCheck = users.getDocumentsList();
            for (Documents documentsListOrphanCheckDocuments : documentsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (\n" + users.getUserName() + "\n) cannot be destroyed since the Documents \n" + documentsListOrphanCheckDocuments.getDocSer() + "\n in its documentsList field has a non-nullable userId field.");
            }
            List<ApprovalRequest> approvalRequestListOrphanCheck = users.getApprovalRequestList();
            for (ApprovalRequest approvalRequestListOrphanCheckApprovalRequest : approvalRequestListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (\n" + users.getUserName() + "\n) cannot be destroyed since the ApprovalRequest \n" + approvalRequestListOrphanCheckApprovalRequest.getRUserID().getUserName() + "\n in its approvalRequestList field has a non-nullable RUserID field.");
            }
            
            List<TransactionLog> transactionLogList = users.getTransactionLogList();
            for (TransactionLog transactionLogListTransactionLog : transactionLogList) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (\n" + users.getUserName() + "\n) cannot be destroyed since the Documents \n" + transactionLogListTransactionLog.getUserId().getUserName() + "\n in its documentsList field has a non-nullable userId field.");
            }
            
            
            
            List<DocTypes> docTypesList = users.getDocTypesList();
            for (DocTypes docTypesListDocTypes : docTypesList) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (\n" + users.getUserName() + "\n) cannot be destroyed since the Documents \n" + docTypesListDocTypes.getDoctypeName() + "\n in its documentsList field has a non-nullable userId field.");
            }
            List<PagePermission> pagePermissionList = users.getPagePermissionList();
            for (PagePermission pagePermissionListPagePermission : pagePermissionList) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (\n" + users.getUserName() + "\n) cannot be destroyed since the Documents \n" + pagePermissionListPagePermission.getPageName() + " in its documentsList field has a non-nullable userId field.");
            }
            List<Permissions> permissionsList = users.getPermissionsList();
            for (Permissions permissionsListPermissions : permissionsList) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (\n" + users.getUserName() + "\n) cannot be destroyed since the Documents \n" + permissionsListPermissions.getFieldName() + " in its documentsList field has a non-nullable userId field.");
            }
            if (illegalOrphanMessages != null) {
                return illegalOrphanMessages;
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        
        return null;
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public boolean isUser(String name, String pass) {

        //Users user=new Users();
        try {
            if (findUserNamePass(name, pass).size() == 0) {
                return false;
            } 
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Users> findUserNamePass(String name, String pass) throws NonexistentEntityException {
        EntityManager em = getEntityManager();
        List<Users> user = new ArrayList<>();
        Query q = em.createNamedQuery("Users.findByUserNamePass");
        try {
            
            q = q.setParameter("userName", name);
            q = q.setParameter("password", pass);
            user = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NonexistentEntityException("Invalid User Name OR Password");
        }
        return user;
    }

    public List<Users> getUsersByUserName(String Id) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Users.findByUserName").setParameter("userName", Id);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Users getByUserName(String userName, String pass) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Users.findByUserNamePass").setParameter("userName", userName).setParameter("password", pass);
            return (Users) q.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
