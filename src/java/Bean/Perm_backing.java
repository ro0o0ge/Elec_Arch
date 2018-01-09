/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.DocTypes;
import Entity.DoctypeDataDef;
import Entity.Groups;
import Entity.Permissions;
import Entity.Users;
import JPA.DocTypesJpaController;
import JPA.DoctypeDataDefJpaController;
import JPA.GroupsJpaController;
import JPA.PermissionsJpaController;
import JPA.UsersJpaController;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author norsin
 */
@ManagedBean
@ViewScoped
public class Perm_backing implements Serializable {

    private static final long serialVersionUID = 2L;

    private int UserID = 0, GroupID = 0, DoctypeID = 0, gid = 0;
    private String FieldName = "", C = "false", R = "false", U = "false", D = "false";


    private List<DocTypes> DocTypeList;
    private List<Users> UsersList;
    private List<Groups> GroupsList;
    private List<DoctypeDataDef> DataDefList;

    private List<Permissions> PermListG;
    private List<Permissions> PermListU;
    private Permissions PermEntity = new Permissions();
    private Permissions SelectedPermG;
    private Permissions SelectedPermU;

    private List<String> SelecedPermissions;

    private GroupsJpaController GroupJPA = new GroupsJpaController();
    private UsersJpaController UserJPA = new UsersJpaController();
    private DocTypesJpaController docTypeJPA = new DocTypesJpaController();
    private DoctypeDataDefJpaController DataDefJPA = new DoctypeDataDefJpaController();
    private PermissionsJpaController PermJPA = new PermissionsJpaController();

    private FacesContext context;

    private boolean disabledocType = true, disableAdd = true, disableEdit = true;
    private boolean disablecheck = true;
    private String booleandoc = "hidden";

    /**
     * Creates a new instance of Perm_backing
     */
    public Perm_backing() {
        GroupsList = GroupJPA.findGroupsEntities();
        UsersList = UserJPA.findUsersEntities();
        DocTypeList = docTypeJPA.findDocTypesEntities();
        PermListG = PermJPA.findPermissionsEntities();
        PermListU = PermJPA.findPermissionsEntities();
        PermListG.clear();
        PermListU.clear();
    }

    public void ChangeOFDocType(ValueChangeEvent event) {
        SelecedPermissions = null;
        if (event.getNewValue() != null) {
            DoctypeID = Integer.parseInt(event.getNewValue().toString());
            DataDefList = DataDefJPA.DocTypeDataDef(DoctypeID);
            disableAdd = false;
            disablecheck = false;
        } else {
            disableAdd = true;
            disablecheck = true;

        }
    }

    public void onrowSelected() {
        
        disableAdd = true;
        disablecheck = false;
        disableEdit = false;
        disabledocType = true;
        booleandoc = "hidden";
        if (SelectedPermG != null) {
            setcheckBoxes(SelectedPermG);
        } else if (SelectedPermU != null) {
            setcheckBoxes(SelectedPermU);

        }

    }

    private void setcheckBoxes(Permissions Selected) {
        SelecedPermissions = new ArrayList<>();
        switch (Selected.getCreating()) {
            case "true":
                SelecedPermissions.add("C");
                break;
        }
        switch (Selected.getUpdating()) {
            case "true":
                SelecedPermissions.add("U");
                break;
        }
        switch (Selected.getReading()) {
            case "true":
                SelecedPermissions.add("R");
               break;
        }
        switch (Selected.getDeleting()) {
            case "true":
                SelecedPermissions.add("D");
                break;
        }

    }

    public void reset() {
        disabledocType = true;
        booleandoc = "visible";
        disableEdit = true;
        disableAdd = true;
        disablecheck = true;
        GroupID = gid = 0;
        UserID = 0;
        PermListG = new ArrayList<>();
        PermListU = new ArrayList<>();
        SelecedPermissions = new ArrayList<>();
        DoctypeID = 0;
        DataDefList = new ArrayList<>();
        SelectedPermG = null;

    }

    public void ChangeOFGroupId(ValueChangeEvent event) {
        reset();
        if (event != null && event.getNewValue() != null) {
            if (event.getNewValue() != null) {
                GroupID = Integer.parseInt(event.getNewValue().toString());
                gid = GroupID;

                SelecedPermissions = null;
                PermListG = PermJPA.getByGroupId(GroupID);
                disabledocType = false;
                booleandoc = "visible";
                disableEdit = true;
                disableAdd = true;
                disablecheck = true;
                DoctypeID = 0;
            }
        }

    }

    public void ChangeOFUserId(ValueChangeEvent event) {
        reset();
        if (event != null) {
            if (event.getNewValue() != null) {
                SelecedPermissions = null;
                UserID = Integer.parseInt(event.getNewValue().toString());
                PermListU = PermJPA.getByUserId(UserID);
                disabledocType = false;
                booleandoc = "visible";
                disableEdit = true;
                disableAdd = true;
                disablecheck = true;
                DoctypeID = 0;
            }
        }
    }

    public void Editing() throws Exception {
        // ========================= GroupPermission ==============

        if (!PermListG.isEmpty()) {

            SelectedPermG.setCreating("false");
            SelectedPermG.setUpdating("false");
            SelectedPermG.setDeleting("false");
            SelectedPermG.setReading("false");

            for (String temp : SelecedPermissions) {
                switch (temp) {
                    case "C":
                        SelectedPermG.setCreating("true");
                        break;
                    case "D":
                        SelectedPermG.setDeleting("true");
                        break;
                    case "R":
                        SelectedPermG.setReading("true");
                        break;
                    case "U":
                        SelectedPermG.setUpdating("true");
                        break;
                    default:
                        break;
                }
            }
            PermJPA.edit(SelectedPermG);
            PermListG = PermJPA.getByGroupId(GroupID);
        }

        // ========================= UserPermission ==============
        if (!PermListU.isEmpty()) {
            SelectedPermU.setCreating("false");
            SelectedPermU.setUpdating("false");
            SelectedPermU.setDeleting("false");
            SelectedPermU.setReading("false");

            for (String temp : SelecedPermissions) {
              
                switch (temp) {
                    case "C":
                        SelectedPermU.setCreating("true");
                        break;
                    case "D":
                        SelectedPermU.setDeleting("true");
                        break;
                    case "R":
                        SelectedPermU.setReading("true");
                        break;
                    case "U":
                        SelectedPermU.setUpdating("true");
                        break;
                    default:
                       
                        break;
                }
            }
            PermJPA.edit(SelectedPermU);

            PermListU = PermJPA.getByUserId(UserID);

        }

        context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("تــم", "التـعـديـل"));

    }

    public void onRowEdit(RowEditEvent event) {

    }

    public void onRowCancel(RowEditEvent event) {
    }

    public void New() throws IOException, Exception {

        PermEntity = new Permissions();

        PermEntity.setDoctypeId(docTypeJPA.findDocTypes(DoctypeID));
        if (!FieldName.isEmpty()) {
            PermEntity.setFieldName(FieldName);
        }
        if (UserID == 0) {

            PermEntity.setGroupId(GroupJPA.findGroups(GroupID));
        } else {
            PermEntity.setUserId(UserJPA.findUsers(UserID));
        }

        PermEntity.setCreating("false");
        PermEntity.setUpdating("false");
        PermEntity.setDeleting("false");
        PermEntity.setReading("false");

        if (SelecedPermissions != null && SelecedPermissions.size() > 0) {
            for (String temp : SelecedPermissions) {
                switch (temp) {
                    case "C":
                        PermEntity.setCreating("true");
                        break;
                    case "D":
                        PermEntity.setDeleting("true");
                        break;
                    case "R":
                        PermEntity.setReading("true");
                        break;
                    case "U":
                        PermEntity.setUpdating("true");
                        break;
                    default:
                        break;
                }
            }
        }


    }

    public void Newgroup() throws IOException, Exception {
        context = FacesContext.getCurrentInstance();

        New();
        if (FieldName == null || FieldName.equals("")) {
            if (!PermJPA.groupIsExist(DoctypeID, gid)) {
                PermJPA.create(PermEntity);
                PermListG.add(PermEntity);
                context.addMessage(null, new FacesMessage("تــم", "إضـافـة الـمـجـمـوعـة"));
            } else {
                context.addMessage(null, new FacesMessage("تـحـذيـر", "المـجـمـوعـة موجـودة بالـفـعـل"));

            }
        } else {
            for(Permissions per:PermListG){
                if(per.getDoctypeId().getDoctypeName().equals(docTypeJPA.findDocTypes(DoctypeID))){
                    if(per.getFieldName()==null || per.getFieldName().equals("")|| per.getFieldName().equals(FieldName)){
                         context.addMessage(null, new FacesMessage("تـحـذيـر", "الـمـسـتـخـدم مـوجـود بالـفـعـل"));
                         return;
                    }
                }
            }
            if (!PermJPA.groupIsExist(FieldName, DoctypeID, gid)) {
                PermJPA.create(PermEntity);
                PermListG.add(PermEntity);
                context.addMessage(null, new FacesMessage("تــم", "إضـافـة الـمـجـمـوعـة"));
            } else {
                context.addMessage(null, new FacesMessage("تـحـذيـر", "الـمجـمـوعـة موجـودة بالـفـعـل"));

            }
        }
    }

    public void Newuser() throws IOException, Exception {

        context = FacesContext.getCurrentInstance();
//        if (FieldName != null && !FieldName.equals("")) {
        New();
        if (FieldName == null || FieldName.equals("")) {
            
            if (!PermJPA.userIsExist(DoctypeID, UserID)) {
                PermJPA.create(PermEntity);
                PermListU.add(PermEntity);
                context.addMessage(null, new FacesMessage("تــم", "إضـافـة الـمـسـتـخـدم"));

            } else {
                context.addMessage(null, new FacesMessage("تـحـذيـر", "الـمـسـتـخـدم مـوجـود بالـفـعـل"));

            }
        } else {
            
            for(Permissions per:PermListU){
                if(per.getDoctypeId().getDoctypeName().equals(docTypeJPA.findDocTypes(DoctypeID))){
                    
                    if(per.getFieldName()==null || per.getFieldName().equals("") || per.getFieldName().equals(FieldName)){
                       
                        
                        context.addMessage(null, new FacesMessage("تـحـذيـر", "الـمـسـتـخـدم مـوجـود بالـفـعـل"));
                         return;
                    }
                }
            }
            if (!PermJPA.userIsExist(FieldName, DoctypeID, UserID)) {
                PermJPA.create(PermEntity);
                PermListU.add(PermEntity);
                context.addMessage(null, new FacesMessage("تــم", "إضـافـة الـمـسـتـخـدم"));
            } else {
                
                context.addMessage(null, new FacesMessage("تـحـذيـر", "الـمـسـتـخـدم مـوجـود بالـفـعـل"));

            }
        }
    }
    
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    public void DeleteG() throws IOException, Exception {

        disabledocType = false;
        disableAdd = false;
        disableEdit = true;
        SelecedPermissions = null;
        PermJPA.destroy(SelectedPermG.getPermID());
        PermListG.remove(SelectedPermG);

        context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("تــم", "الالـغاء"));
    }

    public void DeleteU() throws IOException, Exception {

        disabledocType = false;
        disableAdd = false;
        disableEdit = true;
        SelecedPermissions = null;

        PermJPA.destroy(SelectedPermU.getPermID());
        PermListU.remove(SelectedPermU);

        context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("تــم", "الالـغاء"));

//
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public String getBooleandoc() {
        return booleandoc;
    }

    public void setBooleandoc(String booleandoc) {
        this.booleandoc = booleandoc;
    }

    public boolean isDisablecheck() {
        return disablecheck;
    }

    public void setDisablecheck(boolean disablecheck) {
        this.disablecheck = disablecheck;
    }

    public boolean isDisabledocType() {
        return disabledocType;
    }

    public void setDisabledocType(boolean disabledocType) {
        this.disabledocType = disabledocType;
    }

    public boolean isDisableAdd() {
        return disableAdd;
    }

    public void setDisableAdd(boolean disableAdd) {
        this.disableAdd = disableAdd;
    }

    public boolean isDisableEdit() {
        return disableEdit;
    }

    public void setDisableEdit(boolean disableEdit) {
        this.disableEdit = disableEdit;
    }

    public Permissions getSelectedPermU() {
        return SelectedPermU;
    }

    public void setSelectedPermU(Permissions SelectedPermU) {
        this.SelectedPermU = SelectedPermU;
    }

    public Permissions getSelectedPermG() {
        return SelectedPermG;
    }

    public void setSelectedPermG(Permissions SelectedPerm) {
        this.SelectedPermG = SelectedPerm;
    }

    public List<String> getSelecedPermissions() {
        return SelecedPermissions;
    }

    public void setSelecedPermissions(List<String> SelecedPermissions) {
        this.SelecedPermissions = SelecedPermissions;
    }

    public List<Permissions> getPermListG() {
        return PermListG;
    }

    public void setPermListG(List<Permissions> PermListG) {
        this.PermListG = PermListG;
    }

    public List<Permissions> getPermListU() {
        return PermListU;
    }

    public void setPermListU(List<Permissions> PermListU) {
        this.PermListU = PermListU;
    }

    public List<DoctypeDataDef> getDataDefList() {
        return DataDefList;
    }

    public void setDataDefList(List<DoctypeDataDef> DataDefList) {
        this.DataDefList = DataDefList;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int GroupID) {
        this.GroupID = GroupID;
    }

    public int getDoctypeID() {
        return DoctypeID;
    }

    public void setDoctypeID(int DoctypeID) {
        this.DoctypeID = DoctypeID;
    }

    public String getFieldName() {
        return FieldName;
    }

    public void setFieldName(String FieldName) {
        this.FieldName = FieldName;
    }

    public String getC() {
        return C;
    }

    public void setC(String C) {
        this.C = C;
    }

    public String getR() {
        return R;
    }

    public void setR(String R) {
        this.R = R;
    }

    public String getU() {
        return U;
    }

    public void setU(String U) {
        this.U = U;
    }

    public String getD() {
        return D;
    }

    public void setD(String D) {
        this.D = D;
    }

}
