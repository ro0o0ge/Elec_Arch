package Bean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Entity.ApprovalRequest;
import Entity.Attachment;
import Entity.DocOwners;
import Entity.DocTypes;
import Entity.DoctypeDataDef;
import Entity.DocumentData;
import Entity.Documents;
import Entity.Groups;
import Entity.Permissions;
import Entity.TransactionLog;
import JPA.ApprovalRequestJpaController;
import JPA.AttachmentJpaController;
import JPA.DocOwnersJpaController;
import JPA.DocTypesJpaController;
import JPA.DoctypeDataDefJpaController;
import JPA.DocumentDataJpaController;
import JPA.DocumentsJpaController;
import JPA.PermissionsJpaController;
import JPA.TransactionLogJpaController;
import JPA.UserGroupJpaController;
import JPA.UsersJpaController;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author GIGABYTE
 */
@ManagedBean
@ViewScoped
public class viewDocuments_Backing implements Serializable {

    private static final long serialVersionUID = 2L;
    /**
     * Creates a new instance of viewDocuments_Backing
     */

    private List<Attachment> AttachList;

    private AttachmentJpaController AttachJPA = new AttachmentJpaController();

    private int OwnerId;
    private UIComponent deleteBtn;
    private List<DocOwners> OwnerList = new ArrayList<>();
    private final DocOwnersJpaController JPAOwner = new DocOwnersJpaController();

    private List<Documents> documentsList = new ArrayList<>();
    private Documents selectedDocument;
    private DocumentsJpaController JPADocs = new DocumentsJpaController();

    private List<DocumentData> documentDataList;
    private final DocumentDataJpaController JPAdocData = new DocumentDataJpaController();

    private final PermissionsJpaController JPAPermissions = new PermissionsJpaController();
    private final UserGroupJpaController JPAUserGroup = new UserGroupJpaController();
    private final TransactionLogJpaController JPATrans = new TransactionLogJpaController();
    private final UsersJpaController JPAUsers = new UsersJpaController();
    private final DocTypesJpaController JPADocType = new DocTypesJpaController();
    private final DoctypeDataDefJpaController DataDefJPA = new DoctypeDataDefJpaController();

    private List<Groups> groupList;
    private int userID;
    private List<Permissions> permisiion = new ArrayList<Permissions>();

    private boolean create = false, delete = false, update = false;

    Date date = new Date();
    private final ApprovalRequestJpaController JPAApproval = new ApprovalRequestJpaController();

    public viewDocuments_Backing() {
        List<DocOwners> TempOwnerList = JPAOwner.findDocOwnersEntities();

        for (DocOwners doc : TempOwnerList) {
            if (JPADocs.findDocByOwner(doc.getOwnerId()).size() != 0) {
                OwnerList.add(doc);
            }
        }
        userID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId").toString());
        groupList = JPAUserGroup.getGroupsForUser(userID);
        documentDataList = JPAdocData.findDocumentDataEntities();
        documentDataList.clear();
        RequestDocs = new ArrayList<>();

    }

    int doctypeId = 0;
    List<Documents> TempList2 = new ArrayList<>();

    public void Edit(ActionEvent actionEvent) throws Exception {
        context = FacesContext.getCurrentInstance();

        for (int i = 0; i < documentsList.size(); i++) {
            for (int j = 0; j < documentsList.get(i).getDocumentDataList().size(); j++) {

                if (!documentsList.get(i).getDocumentDataList().get(j).getFieldValue()
                        .equals(TempList2.get(i).getDocumentDataList().get(j).getFieldValue())) {

                    DoctypeDataDef d = DataDefJPA.findDoctypeDataDef(documentsList.get(i).getDocumentDataList().get(j).getFieldName());
                    if (d.getRequired().equals("نعم")) {
                        if (documentsList.get(i).getDocumentDataList().get(j).getFieldValue() == null
                                || documentsList.get(i).getDocumentDataList().get(j).getFieldValue().equals("")) {
                            context.addMessage(null, new FacesMessage("تـحـذيـر", " أكمل البيانات المطلوبة "));
                            return;
                        }
                    }
                    boolean flag = update(documentsList.get(i).getDocumentDataList().get(j).toString());
                    if (!flag) {
                        TransactionLog log = new TransactionLog();
                        log.setLogId(1);
                        log.setDocSer(documentsList.get(i));
                        log.setFieldName(documentsList.get(i).getDocumentDataList().get(j).getFieldName());
                        log.setOldValue(TempList2.get(i).getDocumentDataList().get(j).getFieldValue());
                        log.setUserId(JPAUsers.findUsers(userID));
                        log.setDocFlag("تعديل");
                        log.setUpdatedDate(new Timestamp(date.getTime()));
                        JPATrans.create(log);
                        JPAdocData.editDate(documentsList.get(i).getDocumentDataList().get(j));
                        context.addMessage(null, new FacesMessage("تــم", " الـتعـديـل  "));
                    }
                }
            }
        }

    }
    Map<String, String> files = new HashMap<>();
    DocumentsJpaController DocJPA = new DocumentsJpaController();

    public String changeDoc2(Documents selectedDocument) {
        String T2 = "";
        if (selectedDocument != null) {
            String Temp = selectedDocument.getHomeId().getHomePath().toUpperCase();
            String Temp2 = selectedDocument.getHomeId().getHomePath();
            if (Temp2.length() > 3) {
                Temp2 = Temp2.substring(3);
            } else {
                Temp2 = "";
            }
            switch (Temp.charAt(0)) {
                case 'C':
                    T2 = "/Docs1/" + Temp2 + selectedDocument.getFileName();
                    break;
                case 'D':
                    T2 = "/Docs2/" + Temp2 + selectedDocument.getFileName();
                    break;
                case 'N':
                    T2 = "/Docs3/" + Temp2 + selectedDocument.getFileName();
                    break;
                default:
                    break;
            }
        }
        return T2;
    }

    public Map<String, String> changeDoc(Documents selectedDocument) {
        String T1 = "";
        files = new HashMap<>();
        if (selectedDocument != null) {
            AttachList = AttachJPA.findBydocSer(selectedDocument.getDocSer());
            for (Attachment aa : AttachList) {
                String Temp = aa.getHomeId().getHomePath().toUpperCase();
                String Temp2 = aa.getHomeId().getHomePath();
                if (Temp2.length() > 3) {
                    Temp2 = Temp2.substring(3);
                } else {
                    Temp2 = "";
                }
                switch (Temp.charAt(0)) {
                    case 'C':
                        T1 = "/Docs1/" + Temp2 + aa.getName();
                        break;
                    case 'D':
                        T1 = "/Docs2/" + Temp2 + aa.getName();
                        break;
                    case 'N':
                        T1 = "/Docs3/" + Temp2 + aa.getName();
                        break;
                    default:
                        break;
                }
                files.put(T1, aa.getName());
            }
        }
        return files;
    }

    public void selectOwnerListner(ValueChangeEvent Event) {
        documentsList.clear();
        RequestDocs.clear();
        context = FacesContext.getCurrentInstance();
        OwnerId = Integer.parseInt(Event.getNewValue().toString());
        getList(OwnerId, documentsList);

        List<Documents> TempList1 = new ArrayList<>();
        getList(OwnerId, TempList1);
        getList(OwnerId, TempList2);
        for (Documents dd : documentsList) {
            DocTypes TdT = JPADocType.findDocTypes(dd.getDoctypeId().getDoctypeId());
            if (TdT.getApproverID() != null) {
                List<ApprovalRequest> approv = JPAApproval.findDocType(userID, dd.getDocSer(), TdT.getApproverID().getUserId());

                if (!approv.isEmpty()) {
                    switch (approv.get(0).getIsApproved()) {
                        case "Accept":
                            break;
                        case "Reject":
                            TempList1.remove(dd);
                            TempList2.remove(dd);
                            context.addMessage(null, new FacesMessage("تـحـذيـر", "لديك طلب مرفوض للملف رقم : " + dd.getDocSer()));
                            break;
                        case "Pending":
                            TempList1.remove(dd);
                            TempList2.remove(dd);
                            context.addMessage(null, new FacesMessage("تـحـذيـر", "لديك طلب معلق ل للملف رقم :  " + dd.getDocSer()));
                            break;
                    }
                } else if (userID == TdT.getApproverID().getUserId()) {
                } else {
                    TempList1.remove(dd);
                    TempList2.remove(dd);
                    RequestDocs.add(dd);
                }
            }
        }
        documentsList = new ArrayList<>(TempList1);
    }

    FacesContext context;
    List<Documents> RequestDocs;
    private String typeText = "text";

    public String selectedRow(String fieldName) {

        if (fieldName != null && !fieldName.equals("")) {
            DoctypeDataDef d = DataDefJPA.findDoctypeDataDef(fieldName);
            String type = d.getFieldType();
            if (!type.equals("تاريخ")) {
                typeText = "text";
            } else {
                typeText = "date";

            }

        }
        return typeText;
    }

    public void onRowEdit(RowEditEvent event) {
        typeText = "text";
        context = FacesContext.getCurrentInstance();
        DocumentData docs = (DocumentData) event.getObject();
        if (update(docs.getFieldName())) {
            docs.setFieldValue(JPAdocData.findDocumentData(docs.getDataID()).getFieldValue());
            context.addMessage(null, new FacesMessage("تحـذيـر", " لا يمكن التعديل  "));
            return;
        } else {
            String value = docs.getFieldValue();
            if (value != null && !value.equals("")) {
                if (docs.getFieldName().equals(((DocumentData) event.getObject()).getFieldName())) {

                    DoctypeDataDef d = DataDefJPA.findDoctypeDataDef(docs.getFieldName());
                    String type = d.getFieldType();
                    String length = d.getFieldLength();

                    Pattern pattern;// = Pattern.compile("[a-zA-Z0-9]{6,20}");
                    Matcher matcher;//= pattern.matcher("");
                    switch (type) {
                        case "حروف":
                            pattern = Pattern.compile("[a-zA-Z0-9]{1," + length + "}");
                            matcher = pattern.matcher(value);

                            if (!matcher.matches()) {
                                context.addMessage(null, new FacesMessage("تـحـذيـر ", " عدد الاحرف يجب الا يزيد عن  " + length));
//                                    data.setFieldValue("");
                                return;
                            } else {
                            }
                            break;
                        case "تاريخ":
                            break;
                        case "ارقام":

                            pattern = Pattern.compile("[0-9]{1," + length + "}");
                            matcher = pattern.matcher(value);
                            if (!matcher.matches()) {
                                context.addMessage(null, new FacesMessage("تـحـذيـر ", " عدد الارقام يجب الا يزيد عن  " + length));
//                                    data.setFieldValue("");
                                return;
                            }
                            break;
                    }
//                    context.addMessage(null, new FacesMessage("تـم ", " الـتـعـديـل  "));

                    docs.setFieldValue(((DocumentData) event.getObject()).getFieldValue());

                }
            }
        }
    }

    public void SendReq(ActionEvent event) throws Exception {
        context = FacesContext.getCurrentInstance();
        if (!RequestDocs.isEmpty()) {
            outer:
            for (Documents dd : RequestDocs) {
                DocTypes TdT = JPADocType.findDocTypes(dd.getDoctypeId().getDoctypeId());

                dd.getDoctypeId().getDoctypeId(); ////////////
                for (DocumentData data : dd.getDocumentDataList()) {
                    String fieldName = data.getFieldName();
                    if (!update(fieldName)) {
                        context.addMessage(null, new FacesMessage("تــحــذيــر", "يوجد حقول لا يمكن تعديلها: " + dd.getDocSer()));
                    }
//                    deleting(data.getDocSer().getDocSer())
                }
                context.addMessage(null, new FacesMessage("تــم", "إرسـال طـلب للاطلاع على المستند : " + dd.getDocSer()));
                ApprovalRequest ar = new ApprovalRequest();
                ar.setAPPUser(TdT.getApproverID().getUserId());
                ar.setDocSER(dd);
                ar.setIsApproved("Pending");
                ar.setRUserID(JPAUsers.findUsers(userID));
                ar.setRequestDateAndTime(new Timestamp(date.getTime()));
                JPAApproval.create(ar);
            }
        }

        for (Documents ss : documentsList) {
            TransactionLog log = new TransactionLog();
            log.setLogId(1);
            log.setDocSer(ss);
//            log.setFieldName(documentsList.get(i).getDocumentDataList().get(j).getFieldName());
//            log.setOldValue(TempList2.get(i).getDocumentDataList().get(j).getFieldValue());
            log.setUserId(JPAUsers.findUsers(userID));
            log.setDocFlag("إطلاع");
            log.setUpdatedDate(new Timestamp(date.getTime()));
            JPATrans.create(log);
        }

    }

    private void getList(int OwnerId, List<Documents> documentsList) {

        documentsList.clear();
        for (Documents docData : JPADocs.findDocByOwner(OwnerId)) {

            int docTypeID = docData.getDoctypeId().getDoctypeId();
            if (groupList.size() > 0) {
                for (Groups groupID : groupList) {
                    permisiion.addAll(JPAPermissions.findByGroupUserDocId(docTypeID, groupID.getGroupId(), userID));
                    for (Permissions p : permisiion) {
                        if (p.getReading().equals("true")) {
                            if (docData.getDelete().equals("false")) {
                                documentsList.add(docData);
                            }
                        }
                    }
                }
            } else {
                permisiion.addAll(JPAPermissions.findByGroupUserDocId(docTypeID, 0, userID));
                for (Permissions p : permisiion) {
                    if (p.getReading().equals("true")) {
                        if (docData.getDelete().equals("false")) {
                            documentsList.add(docData);
                        }
                    }
                }
            }

        }
        for (int i = 0; i < permisiion.size(); i++) {
            for (int j = permisiion.size() - 1; j > i; j--) {
                if (permisiion.get(i).getPermID().equals(permisiion.get(j).getPermID())) {
                    permisiion.remove(j);
                }
            }
        }
        for (int i = 0; i < documentsList.size(); i++) {
            for (int j = documentsList.size() - 1; j > i; j--) {
                if (documentsList.get(i).getDocSer()
                        .equals(documentsList.get(j).getDocSer())) {
                    documentsList.remove(j);
                }
            }
        }
        List<DocumentData> tempList2 = new ArrayList<>();
        for (int i = 0; i < documentsList.size(); i++) {
            List<DocumentData> tempList = documentsList.get(i).getDocumentDataList();
            tempList2.clear();
            out:
            for (Permissions p : permisiion) {
                if (p.getDoctypeId().equals(documentsList.get(i).getDoctypeId())) {
                    for (DocumentData d : tempList) {
                        if (p.getFieldName() != null) {
                            if (d.getFieldName().equals(p.getFieldName())) {
                                tempList2.add(d);
                            }
                        } else {
                            tempList2.addAll(tempList);
                            break out;
                        }
                    }
                }
            }
            documentsList.get(i).getDocumentDataList().clear();
            List<DocumentData> tempList3 = new ArrayList<>();
            tempList3.addAll(tempList2);
            documentsList.get(i).setDocumentDataList(tempList3);
        }

    }

    public void printing(Documents doc) {
        if (doc != null) {
            TransactionLog log = new TransactionLog();
            log.setLogId(1);
            log.setDocSer(doc);
            log.setUserId(JPAUsers.findUsers(userID));
            log.setDocFlag("طباعة");
            log.setUpdatedDate(new Timestamp(date.getTime()));
            try {
                JPATrans.create(log);
            } catch (Exception ex) {
                Logger.getLogger(viewDocuments_Backing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean update(String fieldName) {

        boolean flag = false;
        if (fieldName != null) {
            List<Permissions> ch = JPAPermissions.findByGroupUserDocIdObject(fieldName, userID);//.getUpdating();
            for (Permissions c : ch) {
                if (c.getFieldName().equals(fieldName)) {
                    flag = true;
                    return !Boolean.parseBoolean(c.getUpdating());
                }
            }

        } else {
            Permissions ch = JPAPermissions.findBydocTypeId(doctypeId, userID);//.getUpdating();
            if (ch != null) {
                flag = true;
                return !Boolean.parseBoolean(ch.getUpdating());
            }
        }

        if (!flag) {
            for (Groups groupID : groupList) {
                flag = true;
                if (fieldName != null) {
                    List<Permissions> ch = JPAPermissions.findByGroupUserDocIdObject(groupID.getGroupId(), fieldName);//.getUpdating();

                    for (Permissions c : ch) {
                        if (c.getFieldName().equals(fieldName)) {
                            return !Boolean.parseBoolean(c.getUpdating());
                        }
                    }
                } else {
                    Permissions ch = JPAPermissions.findBydocTypeId(groupID.getGroupId(), doctypeId, userID);//.getUpdating();
                    if (ch != null) {
                        return !Boolean.parseBoolean(ch.getUpdating());
                    }
                }
            }
        }
        return false;
    }

    public boolean deleting(String serialID) {
        boolean flag = false;
        if (!(serialID == null || serialID.equals(""))) {
            int docType = JPADocs.findByID(serialID).getDoctypeId().getDoctypeId();

            if (JPADocs.findByID(serialID).getDelete().equals("false")) {
                List<Permissions> pl = JPAPermissions.finddeleteByUserId(docType, userID);//.getUpdating();
                for (Permissions ch : pl) {
                    if (ch != null) {
                        flag = true;
                        return !Boolean.parseBoolean(ch.getDeleting());
                    }
                }
                if (!flag) {
                    for (Groups groupID : groupList) {
                        List<Permissions> p2 = JPAPermissions.findByGdocTypeId(groupID.getGroupId(), doctypeId);
                        for (Permissions ch : p2) {
                            if (ch != null) {
                                flag = true;
                                return !Boolean.parseBoolean(ch.getDeleting());
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public void deletProcess(String serialID) throws Exception {

        Documents Temp = JPADocs.findByID(serialID);
        Temp.setDelete("true");
        JPADocs.editDelete(Temp);
        TransactionLog log = new TransactionLog();
        log.setLogId(1);
        log.setDocSer(Temp);
        log.setUserId(JPAUsers.findUsers(userID));
        log.setDocFlag("حذف");
        log.setUpdatedDate(new Timestamp(date.getTime()));
        JPATrans.create(log);
        context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("تـم", " حذف المستند  "));

    }

    // ============== Setter Gettr ====================
    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }

    public List<Documents> getRequestDocs() {
        return RequestDocs;
    }

    public void setRequestDocs(List<Documents> RequestDocs) {
        this.RequestDocs = RequestDocs;
    }

    public Documents getSelectedDocument() {
        return selectedDocument;
    }

    public void setSelectedDocument(Documents selectedDocument) {
        this.selectedDocument = selectedDocument;
    }

    public int getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(int OwnerId) {
        this.OwnerId = OwnerId;
    }

    public UIComponent getDeleteBtn() {
        return deleteBtn;
    }

    public List<DocumentData> getDocumentDataList() {
        return documentDataList;
    }

    public void setDocumentDataList(List<DocumentData> documentDataList) {
        this.documentDataList = documentDataList;
    }

    public void setDeleteBtn(UIComponent deleteBtn) {
        this.deleteBtn = deleteBtn;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public List<DocOwners> getOwnerList() {
        return OwnerList;
    }

    public void setOwnerList(List<DocOwners> OwnerList) {
        this.OwnerList = OwnerList;
    }

    public List<Documents> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(List<Documents> documentsList) {
        this.documentsList = documentsList;
    }

}
