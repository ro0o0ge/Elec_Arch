/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.DocTypes;
import Entity.DoctypeDataDef;
import JPA.DocTypesJpaController;
import JPA.DoctypeDataDefJpaController;
import JPA.UsersJpaController;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author norsin
 */
@ManagedBean
@ViewScoped
public class docTypes_backing implements Serializable {

    private static final long serialVersionUID = 2L;

    private int docTypeId, ApproverID;
    private String docTypeName, docTypeNotes;
    private DocTypesJpaController DocTypesJPA = new DocTypesJpaController();
    private DoctypeDataDefJpaController DataDefJPA = new DoctypeDataDefJpaController();
    private UsersJpaController UsersJPA = new UsersJpaController();
    private DocTypes docTypes_entity = new DocTypes();

    List<DocTypes> docTypeItemsList;
    private DocTypes selectedDocType;
    private boolean disableEditp2 = true, disableAddp2 = true;
    private boolean disableEditp1 = true, disableAddp1 = false;
    private boolean disableText = true;

    List<DoctypeDataDef> docDataTypeDefList;

    private DoctypeDataDef selectedDocTypeDataDefRow = new DoctypeDataDef();
    private DoctypeDataDef DocTypeDataDefEntity_obj = new DoctypeDataDef();

    private String fieldName;
    private String fieldType;
    private String fieldLength;
    private String requierd;

    private int DataDefID;
    private String String_DataDefID;

    private HtmlInputText fieldNameText;
    private boolean disableCreate = false, disableCreate_2 = false;

    private FacesContext context;

    //me
    public docTypes_backing() {
        docTypeItemsList = DocTypesJPA.findDocTypesEntities();
//        docDataTypeDefList = DataDefJPA.findDoctypeDataDefEntities();

    }

    public void resetFields() {
        disableAddp2 = false;
        disableEditp2 = true;
        disableText = false;

        fieldLength = "";
        fieldName = "";
        fieldType = "";
        DataDefID = 0;
       
        selectedDocTypeDataDefRow = null;
    }

    public void reseting() {
//        docDataTypeDefList.clear();
        selectedDocType = null;

        ApproverID = 0;
        docTypeName = "";
        docTypeNotes = "";
        fieldLength = "";
        fieldName = "";
        fieldType = "";

        disableCreate = false;
        disableCreate_2 = false;
        disableAddp1 = false;
        disableEditp1 = true;
        disableAddp2 = true;
        disableEditp2 = true;
        disableText = true;
        selectedDocTypeDataDefRow = null;
        DataDefID = 0;

        docTypeId = 0;
        docDataTypeDefList = null;
    }

    public Object onNavigate() {
        // Add event code here...
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.setAttribute("DocTypeId_param", docTypeId);

        return "success";
    }

    public void New() throws Exception {
        docTypes_entity.setDoctypeId(docTypeId);

        context = FacesContext.getCurrentInstance();

        if (docTypeName != null && !docTypeName.equals("")) {

            docTypes_entity.setDoctypeName(docTypeName);
            docTypes_entity.setNotes(docTypeNotes);
            docTypes_entity.setApproverID(UsersJPA.findUsers(ApproverID));

            if (!DocTypesJPA.docTypeExist(docTypeName, ApproverID)) {

                DocTypesJPA.create(docTypes_entity);
                docTypeItemsList = DocTypesJPA.findDocTypesEntities();
                context.addMessage(null, new FacesMessage("تــم", " الاضـافـة  "));
            } else {
                context.addMessage(null, new FacesMessage("تـحـذيـر", " هذا الملف موجودبالفعل  "));

            }

        } else {
            context.addMessage(null, new FacesMessage("تـحـذيـر", " أكـمـل الـبـيانـات الـمـطـلوبـة  "));

        }
    }

    public void Edit() throws Exception {
        context = FacesContext.getCurrentInstance();
        if (docTypeId != 0) {
            docTypes_entity.setDoctypeId(docTypeId);
            docTypes_entity.setDoctypeName(docTypeName);
            docTypes_entity.setNotes(docTypeNotes);
            docTypes_entity.setApproverID(UsersJPA.findUsers(ApproverID));

            DocTypesJPA.editDocType(docTypes_entity);
            docTypeItemsList = DocTypesJPA.findDocTypesEntities();
            context.addMessage(null, new FacesMessage("تــم", " الـتـعـذيـل  "));
            disableCreate = false;

        } else {
            context.addMessage(null, new FacesMessage("تـحـذيـر", " يجب اختيار نوع الملف المراد تعديله "));

        }
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public void Delete() throws Exception {
        context = FacesContext.getCurrentInstance();
        if (selectedDocType.getDoctypeDataDefList().isEmpty()) {
            DocTypesJPA.destroy(docTypeId);
            docTypeItemsList = DocTypesJPA.findDocTypesEntities();
            context.addMessage(null, new FacesMessage("تــم", " المـسـح  "));
            reseting();
        } else {
            context.addMessage(null, new FacesMessage("تـحـذيـر", " يجب اختيار نوع الملف المراد مـسـحـه  "));

        }
    }

    public void onRowSelect(SelectEvent event) {

        disableCreate = true;
        disableAddp1 = true;
        disableEditp1 = false;
        disableAddp2 = false;
        disableEditp2 = true;
        disableText = false;

        //Retrive the current record Data to the Page Items
        docTypeId = selectedDocType.getDoctypeId();
        docTypeName = selectedDocType.getDoctypeName();
        docTypeNotes = selectedDocType.getNotes();
        if (selectedDocType.getApproverID() != null) {
            
            ApproverID = selectedDocType.getApproverID().getUserId();
        }else{
            ApproverID=0;
        }
        docDataTypeDefList = DataDefJPA.DocTypeDataDef(docTypeId);

        if (fieldNameText instanceof HtmlInputText) {

            ((HtmlInputText) fieldNameText).setDisabled(false);

        }
        fieldName = null;
        fieldType = null;
        fieldLength = null;
        requierd = null;
        String_DataDefID = null;

    }

    public void onDocDefRowSelect(SelectEvent event) {

        disableCreate_2 = true;
        disableAddp2 = true;
        disableEditp2 = false;
        disableText = false;
        fieldName = selectedDocTypeDataDefRow.getFieldName();
        fieldType = selectedDocTypeDataDefRow.getFieldType();
        fieldLength = selectedDocTypeDataDefRow.getFieldLength();
        requierd = selectedDocTypeDataDefRow.getRequired();
        String_DataDefID = "" + selectedDocTypeDataDefRow.getDataDefID();
    }

    public void onInsertDataDef() throws Exception {

        context = FacesContext.getCurrentInstance();
        if (fieldName == null || fieldName.equals("")
                || fieldType == null || fieldType.equals("")
                || requierd == null || requierd.equals("")
                || fieldLength == null || fieldLength.equals("")) {
            context.addMessage(null, new FacesMessage("تـحـذيـر", " أكـمـل الـبـيانـات الـمـطـلوبـة "));

        } else if (!DataDefJPA.isExist(selectedDocType, fieldName)) //(){
        {
            DocTypeDataDefEntity_obj.setDoctypeId(selectedDocType);
            DocTypeDataDefEntity_obj.setFieldName(fieldName);
            DocTypeDataDefEntity_obj.setFieldLength(fieldLength);
            DocTypeDataDefEntity_obj.setFieldType(fieldType);
            DocTypeDataDefEntity_obj.setRequired(requierd);

            DataDefJPA.create(DocTypeDataDefEntity_obj);
            context.addMessage(null, new FacesMessage("تــم", "الاضافة  "));

        } else {
            context.addMessage(null, new FacesMessage("تـحـذيـر", " هـذا الملف موجود بالفعل  "));

        }

    }

    public void onEditDataDef() throws Exception {

        context = FacesContext.getCurrentInstance();
        if (String_DataDefID != null && !String_DataDefID.equals("")) {
            DocTypeDataDefEntity_obj.setDataDefID(Integer.parseInt(String_DataDefID));
            DocTypeDataDefEntity_obj.setDoctypeId(selectedDocType);
            DocTypeDataDefEntity_obj.setFieldName(fieldName);
            DocTypeDataDefEntity_obj.setFieldLength(fieldLength);
            DocTypeDataDefEntity_obj.setFieldType(fieldType);
            DocTypeDataDefEntity_obj.setRequired(requierd);

            DataDefJPA.edit(DocTypeDataDefEntity_obj);
            disableCreate_2 = false;

            context.addMessage(null, new FacesMessage("تــم", " التـعـذيـل  "));
        } else {
            context.addMessage(null, new FacesMessage("تـحـذيـر", " يجب اختيار الملف المراد تعديله "));

        }
    }

    public void onDeleteDataDef() throws Exception {

        context = FacesContext.getCurrentInstance();

        DataDefJPA.destroy(Integer.parseInt(String_DataDefID));

        disableAddp2 = false;
        disableEditp2 = true;
        String_DataDefID = null;
        fieldName = "";
        fieldLength = "";
        fieldType="";

        context.addMessage(null, new FacesMessage("تــم", " Deleted  "));

//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public boolean isDisableText() {
        return disableText;
    }

    public void setDisableText(boolean disableText) {
        this.disableText = disableText;
    }

    public boolean isDisableCreate() {
        return disableCreate_2;
    }

    public void setDisableCreate(boolean disableCreate) {
        this.disableCreate_2 = disableCreate;
    }

    public boolean isFlag() {
        return disableCreate;
    }

    public void setFlag(boolean Flag) {
        this.disableCreate = Flag;
    }

    public int getApproverID() {
        return ApproverID;
    }

    public void setApproverID(int ApproverID) {
        this.ApproverID = ApproverID;
    }

    public int getDataDefID() {
        return DataDefID;
    }

    public void setDataDefID(int DataDefID) {
        this.DataDefID = DataDefID;
    }

    public void setDocTypeId(int docTypeId) {
        this.docTypeId = docTypeId;
    }

    public int getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeNotes(String docTypeNotes) {
        this.docTypeNotes = docTypeNotes;
    }

    public String getDocTypeNotes() {
        return docTypeNotes;
    }

    public void setSelectedDocType(DocTypes selectedDocType) {
        this.selectedDocType = selectedDocType;
    }

    public DocTypes getSelectedDocType() {
        return selectedDocType;
    }

    public void setDocTypeItemsList(List<DocTypes> docTypeItemsList) {
        this.docTypeItemsList = docTypeItemsList;
    }

    public List<DocTypes> getDocTypeItemsList() {
        return docTypeItemsList;
    }

    public void setDocDataTypeDefList(List<DoctypeDataDef> docDataTypeDefList) {
        this.docDataTypeDefList = docDataTypeDefList;
    }

    public List<DoctypeDataDef> getDocDataTypeDefList() {
        return docDataTypeDefList = DataDefJPA.DocTypeDataDef(docTypeId);
    }

    public void setSelectedDocTypeDataDefRow(DoctypeDataDef selectedDocTypeDataDefRow) {
        this.selectedDocTypeDataDefRow = selectedDocTypeDataDefRow;
    }

    public DoctypeDataDef getSelectedDocTypeDataDefRow() {
        return selectedDocTypeDataDefRow;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldLength(String fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getFieldLength() {
        return fieldLength;
    }

    public void setRequierd(String requierd) {
        this.requierd = requierd;
    }

    public String getRequierd() {
        return requierd;
    }

    public void setFieldNameText(HtmlInputText inputText1) {
        this.fieldNameText = inputText1;
    }

    public HtmlInputText getFieldNameText() {
        return fieldNameText;
    }

    public String getString_DataDefID() {
        return String_DataDefID;
    }

    public void setString_DataDefID(String String_DataDefID) {
        this.String_DataDefID = String_DataDefID;
    }

    public boolean isDisableEditp2() {
        return disableEditp2;
    }

    public void setDisableEditp2(boolean disableEditp2) {
        this.disableEditp2 = disableEditp2;
    }

    public boolean isDisableAddp2() {
        return disableAddp2;
    }

    public void setDisableAddp2(boolean disableAddp2) {
        this.disableAddp2 = disableAddp2;
    }

    public boolean isDisableEditp1() {
        return disableEditp1;
    }

    public void setDisableEditp1(boolean disableEditp1) {
        this.disableEditp1 = disableEditp1;
    }

    public boolean isDisableAddp1() {
        return disableAddp1;
    }

    public void setDisableAddp1(boolean disableAddp1) {
        this.disableAddp1 = disableAddp1;
    }

}
