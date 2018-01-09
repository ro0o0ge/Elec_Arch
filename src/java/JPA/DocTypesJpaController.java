/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA;

import Entity.DocTypes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.Users;
import Entity.Documents;
import java.util.ArrayList;
import java.util.List;
import Entity.Permissions;
import Entity.DoctypeDataDef;
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
public class DocTypesJpaController implements Serializable {

    public DocTypesJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Model-1");
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Model-1");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DocTypes docTypes) throws PreexistingEntityException, Exception {
        if (docTypes.getDocumentsList() == null) {
            docTypes.setDocumentsList(new ArrayList<Documents>());
        }
        if (docTypes.getPermissionsList() == null) {
            docTypes.setPermissionsList(new ArrayList<Permissions>());
        }
        if (docTypes.getDoctypeDataDefList() == null) {
            docTypes.setDoctypeDataDefList(new ArrayList<DoctypeDataDef>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users approverID = docTypes.getApproverID();
            if (approverID != null) {
                approverID = em.getReference(approverID.getClass(), approverID.getUserId());
                docTypes.setApproverID(approverID);
            }
            List<Documents> attachedDocumentsList = new ArrayList<Documents>();
            for (Documents documentsListDocumentsToAttach : docTypes.getDocumentsList()) {
                documentsListDocumentsToAttach = em.getReference(documentsListDocumentsToAttach.getClass(), documentsListDocumentsToAttach.getDocSer());
                attachedDocumentsList.add(documentsListDocumentsToAttach);
            }
            docTypes.setDocumentsList(attachedDocumentsList);
            List<Permissions> attachedPermissionsList = new ArrayList<Permissions>();
            for (Permissions permissionsListPermissionsToAttach : docTypes.getPermissionsList()) {
                permissionsListPermissionsToAttach = em.getReference(permissionsListPermissionsToAttach.getClass(), permissionsListPermissionsToAttach.getPermID());
                attachedPermissionsList.add(permissionsListPermissionsToAttach);
            }
            docTypes.setPermissionsList(attachedPermissionsList);
            List<DoctypeDataDef> attachedDoctypeDataDefList = new ArrayList<DoctypeDataDef>();
            for (DoctypeDataDef doctypeDataDefListDoctypeDataDefToAttach : docTypes.getDoctypeDataDefList()) {
                doctypeDataDefListDoctypeDataDefToAttach = em.getReference(doctypeDataDefListDoctypeDataDefToAttach.getClass(), doctypeDataDefListDoctypeDataDefToAttach.getDataDefID());
                attachedDoctypeDataDefList.add(doctypeDataDefListDoctypeDataDefToAttach);
            }
            docTypes.setDoctypeDataDefList(attachedDoctypeDataDefList);
            em.persist(docTypes);
            if (approverID != null) {
                approverID.getDocTypesList().add(docTypes);
                approverID = em.merge(approverID);
            }
            for (Documents documentsListDocuments : docTypes.getDocumentsList()) {
                DocTypes oldDoctypeIdOfDocumentsListDocuments = documentsListDocuments.getDoctypeId();
                documentsListDocuments.setDoctypeId(docTypes);
                documentsListDocuments = em.merge(documentsListDocuments);
                if (oldDoctypeIdOfDocumentsListDocuments != null) {
                    oldDoctypeIdOfDocumentsListDocuments.getDocumentsList().remove(documentsListDocuments);
                    oldDoctypeIdOfDocumentsListDocuments = em.merge(oldDoctypeIdOfDocumentsListDocuments);
                }
            }
            for (Permissions permissionsListPermissions : docTypes.getPermissionsList()) {
                DocTypes oldDoctypeIdOfPermissionsListPermissions = permissionsListPermissions.getDoctypeId();
                permissionsListPermissions.setDoctypeId(docTypes);
                permissionsListPermissions = em.merge(permissionsListPermissions);
                if (oldDoctypeIdOfPermissionsListPermissions != null) {
                    oldDoctypeIdOfPermissionsListPermissions.getPermissionsList().remove(permissionsListPermissions);
                    oldDoctypeIdOfPermissionsListPermissions = em.merge(oldDoctypeIdOfPermissionsListPermissions);
                }
            }
            for (DoctypeDataDef doctypeDataDefListDoctypeDataDef : docTypes.getDoctypeDataDefList()) {
                DocTypes oldDoctypeIdOfDoctypeDataDefListDoctypeDataDef = doctypeDataDefListDoctypeDataDef.getDoctypeId();
                doctypeDataDefListDoctypeDataDef.setDoctypeId(docTypes);
                doctypeDataDefListDoctypeDataDef = em.merge(doctypeDataDefListDoctypeDataDef);
                if (oldDoctypeIdOfDoctypeDataDefListDoctypeDataDef != null) {
                    oldDoctypeIdOfDoctypeDataDefListDoctypeDataDef.getDoctypeDataDefList().remove(doctypeDataDefListDoctypeDataDef);
                    oldDoctypeIdOfDoctypeDataDefListDoctypeDataDef = em.merge(oldDoctypeIdOfDoctypeDataDefListDoctypeDataDef);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDocTypes(docTypes.getDoctypeId()) != null) {
                throw new PreexistingEntityException("DocTypes " + docTypes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public boolean docTypeExist(String docName, int userID) {
        
        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("DocTypes.findByDocNameAppID");
        q = q.setParameter("userID", userID).setParameter("docName", docName);
        try {
            q.getSingleResult();
            
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    public void editDocType(DocTypes docTypes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(docTypes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = docTypes.getDoctypeId();
                if (findDocTypes(id) == null) {
                    throw new NonexistentEntityException("The docTypes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DocTypes docTypes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocTypes persistentDocTypes = em.find(DocTypes.class, docTypes.getDoctypeId());
            Users approverIDOld = persistentDocTypes.getApproverID();
            Users approverIDNew = docTypes.getApproverID();
            List<Documents> documentsListOld = persistentDocTypes.getDocumentsList();
            List<Documents> documentsListNew = docTypes.getDocumentsList();
            List<Permissions> permissionsListOld = persistentDocTypes.getPermissionsList();
            List<Permissions> permissionsListNew = docTypes.getPermissionsList();
            List<DoctypeDataDef> doctypeDataDefListOld = persistentDocTypes.getDoctypeDataDefList();
            List<DoctypeDataDef> doctypeDataDefListNew = docTypes.getDoctypeDataDefList();
            List<String> illegalOrphanMessages = null;
            for (Documents documentsListOldDocuments : documentsListOld) {
                if (!documentsListNew.contains(documentsListOldDocuments)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documents " + documentsListOldDocuments + " since its doctypeId field is not nullable.");
                }
            }
            for (Permissions permissionsListOldPermissions : permissionsListOld) {
                if (!permissionsListNew.contains(permissionsListOldPermissions)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Permissions " + permissionsListOldPermissions + " since its doctypeId field is not nullable.");
                }
            }
            for (DoctypeDataDef doctypeDataDefListOldDoctypeDataDef : doctypeDataDefListOld) {
                if (!doctypeDataDefListNew.contains(doctypeDataDefListOldDoctypeDataDef)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DoctypeDataDef " + doctypeDataDefListOldDoctypeDataDef + " since its doctypeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (approverIDNew != null) {
                approverIDNew = em.getReference(approverIDNew.getClass(), approverIDNew.getUserId());
                docTypes.setApproverID(approverIDNew);
            }
            List<Documents> attachedDocumentsListNew = new ArrayList<Documents>();
            for (Documents documentsListNewDocumentsToAttach : documentsListNew) {
                documentsListNewDocumentsToAttach = em.getReference(documentsListNewDocumentsToAttach.getClass(), documentsListNewDocumentsToAttach.getDocSer());
                attachedDocumentsListNew.add(documentsListNewDocumentsToAttach);
            }
            documentsListNew = attachedDocumentsListNew;
            docTypes.setDocumentsList(documentsListNew);
            List<Permissions> attachedPermissionsListNew = new ArrayList<Permissions>();
            for (Permissions permissionsListNewPermissionsToAttach : permissionsListNew) {
                permissionsListNewPermissionsToAttach = em.getReference(permissionsListNewPermissionsToAttach.getClass(), permissionsListNewPermissionsToAttach.getPermID());
                attachedPermissionsListNew.add(permissionsListNewPermissionsToAttach);
            }
            permissionsListNew = attachedPermissionsListNew;
            docTypes.setPermissionsList(permissionsListNew);
            List<DoctypeDataDef> attachedDoctypeDataDefListNew = new ArrayList<DoctypeDataDef>();
            for (DoctypeDataDef doctypeDataDefListNewDoctypeDataDefToAttach : doctypeDataDefListNew) {
                doctypeDataDefListNewDoctypeDataDefToAttach = em.getReference(doctypeDataDefListNewDoctypeDataDefToAttach.getClass(), doctypeDataDefListNewDoctypeDataDefToAttach.getDataDefID());
                attachedDoctypeDataDefListNew.add(doctypeDataDefListNewDoctypeDataDefToAttach);
            }
            doctypeDataDefListNew = attachedDoctypeDataDefListNew;
            docTypes.setDoctypeDataDefList(doctypeDataDefListNew);
            docTypes = em.merge(docTypes);
            if (approverIDOld != null && !approverIDOld.equals(approverIDNew)) {
                approverIDOld.getDocTypesList().remove(docTypes);
                approverIDOld = em.merge(approverIDOld);
            }
            if (approverIDNew != null && !approverIDNew.equals(approverIDOld)) {
                approverIDNew.getDocTypesList().add(docTypes);
                approverIDNew = em.merge(approverIDNew);
            }
            for (Documents documentsListNewDocuments : documentsListNew) {
                if (!documentsListOld.contains(documentsListNewDocuments)) {
                    DocTypes oldDoctypeIdOfDocumentsListNewDocuments = documentsListNewDocuments.getDoctypeId();
                    documentsListNewDocuments.setDoctypeId(docTypes);
                    documentsListNewDocuments = em.merge(documentsListNewDocuments);
                    if (oldDoctypeIdOfDocumentsListNewDocuments != null && !oldDoctypeIdOfDocumentsListNewDocuments.equals(docTypes)) {
                        oldDoctypeIdOfDocumentsListNewDocuments.getDocumentsList().remove(documentsListNewDocuments);
                        oldDoctypeIdOfDocumentsListNewDocuments = em.merge(oldDoctypeIdOfDocumentsListNewDocuments);
                    }
                }
            }
            for (Permissions permissionsListNewPermissions : permissionsListNew) {
                if (!permissionsListOld.contains(permissionsListNewPermissions)) {
                    DocTypes oldDoctypeIdOfPermissionsListNewPermissions = permissionsListNewPermissions.getDoctypeId();
                    permissionsListNewPermissions.setDoctypeId(docTypes);
                    permissionsListNewPermissions = em.merge(permissionsListNewPermissions);
                    if (oldDoctypeIdOfPermissionsListNewPermissions != null && !oldDoctypeIdOfPermissionsListNewPermissions.equals(docTypes)) {
                        oldDoctypeIdOfPermissionsListNewPermissions.getPermissionsList().remove(permissionsListNewPermissions);
                        oldDoctypeIdOfPermissionsListNewPermissions = em.merge(oldDoctypeIdOfPermissionsListNewPermissions);
                    }
                }
            }
            for (DoctypeDataDef doctypeDataDefListNewDoctypeDataDef : doctypeDataDefListNew) {
                if (!doctypeDataDefListOld.contains(doctypeDataDefListNewDoctypeDataDef)) {
                    DocTypes oldDoctypeIdOfDoctypeDataDefListNewDoctypeDataDef = doctypeDataDefListNewDoctypeDataDef.getDoctypeId();
                    doctypeDataDefListNewDoctypeDataDef.setDoctypeId(docTypes);
                    doctypeDataDefListNewDoctypeDataDef = em.merge(doctypeDataDefListNewDoctypeDataDef);
                    if (oldDoctypeIdOfDoctypeDataDefListNewDoctypeDataDef != null && !oldDoctypeIdOfDoctypeDataDefListNewDoctypeDataDef.equals(docTypes)) {
                        oldDoctypeIdOfDoctypeDataDefListNewDoctypeDataDef.getDoctypeDataDefList().remove(doctypeDataDefListNewDoctypeDataDef);
                        oldDoctypeIdOfDoctypeDataDefListNewDoctypeDataDef = em.merge(oldDoctypeIdOfDoctypeDataDefListNewDoctypeDataDef);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = docTypes.getDoctypeId();
                if (findDocTypes(id) == null) {
                    throw new NonexistentEntityException("The docTypes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocTypes docTypes;
            try {
                docTypes = em.getReference(DocTypes.class, id);
                docTypes.getDoctypeId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docTypes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Documents> documentsListOrphanCheck = docTypes.getDocumentsList();
            for (Documents documentsListOrphanCheckDocuments : documentsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DocTypes (" + docTypes + ") cannot be destroyed since the Documents " + documentsListOrphanCheckDocuments + " in its documentsList field has a non-nullable doctypeId field.");
            }
            List<Permissions> permissionsListOrphanCheck = docTypes.getPermissionsList();
            for (Permissions permissionsListOrphanCheckPermissions : permissionsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DocTypes (" + docTypes + ") cannot be destroyed since the Permissions " + permissionsListOrphanCheckPermissions + " in its permissionsList field has a non-nullable doctypeId field.");
            }
            List<DoctypeDataDef> doctypeDataDefListOrphanCheck = docTypes.getDoctypeDataDefList();
            for (DoctypeDataDef doctypeDataDefListOrphanCheckDoctypeDataDef : doctypeDataDefListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DocTypes (" + docTypes + ") cannot be destroyed since the DoctypeDataDef " + doctypeDataDefListOrphanCheckDoctypeDataDef + " in its doctypeDataDefList field has a non-nullable doctypeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Users approverID = docTypes.getApproverID();
            if (approverID != null) {
                approverID.getDocTypesList().remove(docTypes);
                approverID = em.merge(approverID);
            }
            em.remove(docTypes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DocTypes> findDocTypesEntities() {
        return findDocTypesEntities(true, -1, -1);
    }

    public List<DocTypes> findDocTypesEntities(int maxResults, int firstResult) {
        return findDocTypesEntities(false, maxResults, firstResult);
    }

    private List<DocTypes> findDocTypesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocTypes.class));
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

    public DocTypes findDocTypes(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DocTypes.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocTypesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocTypes> rt = cq.from(DocTypes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
