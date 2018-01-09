/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.Attachment;
import Entity.DocHomes;
import Entity.DocOwners;
import Entity.DocTypes;
import Entity.DoctypeDataDef;
import Entity.DocumentData;
import Entity.Documents;
import Entity.Groups;
import Entity.Permissions;
import Entity.Users;
import JPA.AttachmentJpaController;
import JPA.DocHomesJpaController;
import JPA.DocOwnersJpaController;
import JPA.DocTypesJpaController;
import JPA.DoctypeDataDefJpaController;
import JPA.DocumentDataJpaController;
import JPA.DocumentsJpaController;
import JPA.PermissionsJpaController;
import JPA.UserGroupJpaController;
import JPA.UsersJpaController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author norsin
 */
@ManagedBean
@ViewScoped
public class Doc_Backing implements Serializable {

    private static final long serialVersionUID = 2L;
    private boolean disableUpload = true, disableAttach = true, disabledoctype = false;

    private String Serial = "N/A", DateCreated, Location, Home, FieldName, FieldValue;
    private int SeletedDocType, ownerId;

    private int Username = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId").toString());

    private List<Users> UserList;
    private Users User;

    private List<DocumentData> DocDataList;
    private DocumentData DocData;

    private List<DocTypes> DocTypeList;
    private List<DoctypeDataDef> TypeDefList;

    private List<DocOwners> DocOwnerList;
    private DocHomes DocumentHome;

    private List<Documents> DocumentList;
    private Documents Document;

    private List<Attachment> AttachList;
    private Attachment Attach;

    private final DocHomesJpaController HomeJPA = new DocHomesJpaController();
    private final DocOwnersJpaController OwnerJPA = new DocOwnersJpaController();
    private final DoctypeDataDefJpaController DataDefJPA = new DoctypeDataDefJpaController();
    private final DocTypesJpaController TypeJPA = new DocTypesJpaController();
    private final DocumentDataJpaController DocDataJPA = new DocumentDataJpaController();
    private final DocumentsJpaController DocumentJPA = new DocumentsJpaController();
    private final UsersJpaController UsersJPA = new UsersJpaController();
    private final AttachmentJpaController AttachJPA = new AttachmentJpaController();
    private final PermissionsJpaController JPApermissions = new PermissionsJpaController();
    private final UserGroupJpaController JPAusergroup = new UserGroupJpaController();

    private boolean required = false;
    private FacesContext context;
    private final int userID;

    /**
     * Creates a new instance of Doc_Backing
     */
    public Doc_Backing() {
        DocumentList = DocumentJPA.findDocumentsEntities();
        DocumentList.clear();
        UserList = UsersJPA.findUsersEntities();
        DocTypeList = TypeJPA.findDocTypesEntities();
        DocOwnerList = OwnerJPA.findDocOwnersEntities();
        DocumentHome = HomeJPA.getDocHomes_findCurrentHome().get(0);
        Home = DocumentHome.getHomePath();
        DocDataList = DocDataJPA.findDocumentDataEntities();
        DocDataList.clear();
        Document = new Documents();
        Location = "";
        userID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId").toString());

        AttachList = new ArrayList<>();
    }
    private boolean disableCreate = false;

    public void SelectDocType(ValueChangeEvent event) {
        SeletedDocType = Integer.parseInt(event.getNewValue().toString());
        TypeDefList = DataDefJPA.DocTypeDataDef(SeletedDocType);
        DocDataList.clear();
        boolean done = false;

        for (DoctypeDataDef dd : TypeDefList) {
            out1:
            for (Groups gg : JPAusergroup.getGroupsForUser(userID)) {
                done = true;
                for (Permissions pp : JPApermissions.findByGroupUserDocId(dd.getDoctypeId().getDoctypeId(),
                        gg.getGroupId(), userID)) {
                    if (dd.getFieldName().equals(pp.getFieldName()) || pp.getFieldName() == null || pp.getFieldName().equals("")) {
                        if (pp.getCreating().equals("true")) {
                            DocData = new DocumentData();
                            DocData.setFieldName(dd.getFieldName());
                            DocDataList.add(DocData);
                            disableCreate = false;
                            break out1;
                        } else if (pp.getFieldName() == null || pp.getFieldName().equals("")) {
                            disableCreate = true;
                        }
                    }
                }
            }

            if (done == false) {
                for (Permissions pp : JPApermissions.findByGroupUserDocId(dd.getDoctypeId().getDoctypeId(),
                        0, userID)) {
                    if (dd.getFieldName().equals(pp.getFieldName()) || pp.getFieldName() == null || pp.getFieldName().equals("")) {
                        if (pp.getCreating().equals("true")) {
                            DocData = new DocumentData();
                            DocData.setFieldName(dd.getFieldName());
                            DocDataList.add(DocData);
                        } else if (pp.getFieldName() == null || pp.getFieldName().equals("")) {
                            disableCreate = true;
                        }
                    }
                }
            }
        }

    }

    public void SelectOwnerId(ValueChangeEvent event) {
        ownerId = Integer.parseInt(event.getNewValue().toString());
        DocumentList = DocumentJPA.getDocsByOwner(ownerId);
        if (ownerId == 0) {
            disableAttach = true;
            disableUpload = true;
            Serial = "N/A";
        } else if (!DocumentList.isEmpty()) {
            disableUpload = false;
            disableAttach = false;
            Serial = ownerId + "_" + (DocumentList.size() + 1);
        } else {
            disableUpload = false;
            disableAttach = false;
            Serial = "" + ownerId + "_1";
        }
    }

    public String selectedRow(String fieldName) {
        String typeText = "text";
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

        context = FacesContext.getCurrentInstance();

        for (DocumentData DocDataList1 : DocDataList) {
            String fielname = ((DocumentData) event.getObject()).getFieldName();
            String value = DocDataList1.getFieldValue();

            if (value != null && !value.equals("")) {
                if (DocDataList1.getFieldName().equals(((DocumentData) event.getObject()).getFieldName())) {
                    DoctypeDataDef d = DataDefJPA.findDoctypeDataDef(DocDataList1.getFieldName());
                    String type = d.getFieldType();
                    String length = d.getFieldLength();

                    Pattern pattern;// = Pattern.compile("[a-zA-Z0-9]{6,20}");
                    Matcher matcher;//= pattern.matcher("");

                    switch (type) {
                        case "حروف":
                            pattern = Pattern.compile("[a-zA-Z0-9 _ أ ب ت ث ج ح خ د ذ ر ز س ش ص ض ط ظ ع غ ف ق ك ل م ن ه و ى ة ء ا ي ئ]{1," + length + "}");
                            matcher = pattern.matcher(value);
                            if (!matcher.matches()) {
                                context.addMessage(null, new FacesMessage("تـحـذيـر ", " عدد الاحرف يجب الا يزيد عن  " + length));
                                DocDataList1.setFieldValue("");
                                return;
                            } else {
                            }
                            break;
                        case "تاريخ":
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY");
                            try {
                                format.parse(value);
                            } catch (Exception e) {
                                context.addMessage(null, new FacesMessage("تـحـذيـر ", "التاريخ يجب ان يكون على الصور " + " 1988-3-4  "));
                                DocDataList1.setFieldValue("");
                                return;
                            }
                            break;
                        case "ارقام":
                            pattern = Pattern.compile("[0-9]{1," + length + "}");
                            matcher = pattern.matcher(value);
                            if (!matcher.matches()) {
                                context.addMessage(null, new FacesMessage("تـحـذيـر ", " عدد الارقام يجب الا يقل عن  " + length));
                                DocDataList1.setFieldValue("");
                                return;
                            }
                            break;
                    }
                    context.addMessage(null, new FacesMessage("تــم", " التعديل  "));
                    DocDataList1.setFieldValue(((DocumentData) event.getObject()).getFieldValue());
//                    DocDataList1.setDocSer(Document);
                }
            }

        }

    }

    public void onRowCancel(RowEditEvent event) {
        context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("", " تـم الـغـاء الـعـديـل  "));

    }

    public void New() throws Exception {
        context = FacesContext.getCurrentInstance();
        Document = new Documents();
        Document.setHomeId(DocumentHome);
        Document.setOwnerId(OwnerJPA.findDocOwners(ownerId));
        if (ownerId == 0) {
            context.addMessage(null, new FacesMessage("تحذير", "اختر المستند "));
            return;
        }
        if (Location == null || Location.equals("")) {
            context.addMessage(null, new FacesMessage("تحذير", "اختر مكان المستند "));
            return;
        }
        Document.setLocation(Location);
        Document.setUserId(UsersJPA.findUsers(Username));
        Document.setDocSer(Serial);
        Document.setFileName(Serial + extension);
        if (SeletedDocType == 0) {
            context.addMessage(null, new FacesMessage("تحذير", "اختر تصنيف المستند "));
            return;
        }
        Document.setDoctypeId(TypeJPA.findDocTypes(SeletedDocType));
        Date date = new Date();
        Document.setCreatedDate(new Timestamp(date.getTime()));
        Document.setDelete("false");
        boolean flag = true;
        for (DocumentData data : DocDataList) {

            DoctypeDataDef d = DataDefJPA.findDoctypeDataDef(data.getFieldName());

            if (d.getRequired().equals("نعم")) {
                if (data.getFieldValue() == null || data.getFieldValue().equals("")) {
                    context.addMessage(null, new FacesMessage("تحذير", " أكمل المطلوب  " + data.getFieldName()));
                    flag = false;
                }
            }
        }
        if (flag) {
            DocumentJPA.create(Document);
            for (DocumentData data : DocDataList) {
                data.setDocSer(Document);
                DocDataJPA.create(data);
            }
            if (!AttachList.isEmpty()) {
                for (Attachment att : AttachList) {
                    att.setDocSer(Document);
                    att.setHomeId(DocumentHome);
                    AttachJPA.create(att);
                }
            }
            context.addMessage(null, new FacesMessage("تــم", " حـفـظ المستند  "));
            resetValues();
        }
    }

    public void resetValues() throws IOException {
        SeletedDocType = 0;
        DocDataList = DocDataJPA.findDocumentDataEntities();
        DocDataList.clear();
        TypeDefList = null;
        Serial = DateCreated = Location = Home = "";
        SeletedDocType = ownerId = 0;
        PathToFile = "";
    }

    private String extension = "";

    public void uploadDocument(FileUploadEvent event) throws Exception {
        context = FacesContext.getCurrentInstance();

        try {
            if (SeletedDocType != 0) {
                String con = event.getFile().getContentType();
                extension = "." + con.substring(con.indexOf("/") + 1);
                copyDocument(Serial, event.getFile().getInputstream());
                context.addMessage(null, new FacesMessage("تــم ", " رفع الـمـلـف"));
                disableUpload = true;
                disabledoctype = true;
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String PathToFile = "";

    public void copyDocument(String fileName, InputStream in) throws IOException {
        try {
            
            
            String Temp2 = "";
            Temp2 += Home;
            OutputStream out = null;
            boolean check = new File(Home).mkdirs();
            out = new FileOutputStream(new File(Home + fileName + extension));
            int read = 0;
            byte[] bytes = new byte[1048576];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();

            String Temp = "";
            Temp = "" + Home;
            String Drive = Temp.substring(0, 3);
            if (Temp2.length() > 3) {
                Temp2 = Temp2.substring(3);
            } else {
                Temp2 = "";
            }
            Drive.toUpperCase();
            
            switch (Drive.charAt(0)) {
                case 'C':
                    Home = "/Docs1/" + Temp2;
                    break;
                case 'D':
                    Home = "/Docs2/" + Temp2;
                    break;
                case 'N':
                    Home = "/Docs3/" + Temp2;
                    break;
                default:
                    break;
            }
            PathToFile = Home + fileName + extension;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadAttachment(FileUploadEvent event) throws Exception {
        context = FacesContext.getCurrentInstance();
        copyAttachment(event.getFile().getFileName(), event.getFile().getInputstream());
        context.addMessage(null, new FacesMessage("تــم ", "رفع الـمـرفقات "));
    }

    public void copyAttachment(String fileName, InputStream in) throws IOException {
        Attach = new Attachment();

        Home = DocumentHome.getHomePath();
        
        OutputStream out = null;
        new File(Home).mkdirs();
        System.out.println("Path "+Home + Serial + "_" + fileName);
        out = new FileOutputStream(new File(Home + Serial + "_" + fileName));
        int read = 0;
        byte[] bytes = new byte[5242880];
        while ((read = in.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        in.close();
        out.flush();
        out.close();
        Attach.setName(Serial + "_" + fileName);
        AttachList.add(Attach);

    }

    public boolean isDisabledoctype() {
        return disabledoctype;
    }

    public void setDisabledoctype(boolean disabledoctype) {
        this.disabledoctype = disabledoctype;
    }

    public boolean isDisableCreate() {
        return disableCreate;
    }

    public void setDisableCreate(boolean disableCreate) {
        this.disableCreate = disableCreate;
    }

    public String getPathToFile() {
        return PathToFile;
    }

    public void setPathToFile(String PathToFile) {
        this.PathToFile = PathToFile;
    }

    public boolean isDisableUpload() {
        return disableUpload;
    }

    public void setDisableUpload(boolean disableUpload) {
        this.disableUpload = disableUpload;
    }

    public boolean isDisableAttach() {
        return disableAttach;
    }

    public void setDisableAttach(boolean disableAttach) {
        this.disableAttach = disableAttach;
    }

    public DocumentData getDocData() {
        return DocData;
    }

    public void setDocData(DocumentData DocData) {
        this.DocData = DocData;
    }

    public String getHome() {
        return Home;
    }

    public void setHome(String Home) {
        this.Home = Home;
    }

    public int getSeletedDocType() {
        return SeletedDocType;
    }

    public void setSeletedDocType(int SeletedDocType) {
        this.SeletedDocType = SeletedDocType;
    }

    public List<Users> getUserList() {
        return UserList;
    }

    public void setUserList(List<Users> UserList) {
        this.UserList = UserList;
    }

    public List<DocumentData> getDocDataList() {
        return DocDataList;
    }

    public void setDocDataList(List<DocumentData> DocDataList) {
        this.DocDataList = DocDataList;
    }

    public List<DocTypes> getDocTypeList() {
        return DocTypeList;
    }

    public void setDocTypeList(List<DocTypes> DocTypeList) {
        this.DocTypeList = DocTypeList;
    }

    public List<DocOwners> getDocOwnerList() {
        return DocOwnerList;
    }

    public void setDocOwnerList(List<DocOwners> DocOwnerList) {
        this.DocOwnerList = DocOwnerList;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String Serial) {
        this.Serial = Serial;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String DateCreated) {
        this.DateCreated = DateCreated;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String FieldName) {
        this.FieldName = FieldName;
    }

    public String getFieldValue() {
        return FieldValue;
    }

    public void setFieldValue(String FieldValue) {
        this.FieldValue = FieldValue;
    }

    public int getUsername() {
        return Username;
    }

    public void setUsername(int Username) {
        this.Username = Username;
    }

}
