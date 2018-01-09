/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.ApplicationPages;
import Entity.Groups;
import Entity.PagePermission;
import Entity.Users;
import JPA.ApplicationPagesJpaController;
import JPA.GroupsJpaController;
import JPA.PagePermissionJpaController;
import JPA.UsersJpaController;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author norsin
 */
@ManagedBean
@ViewScoped
public class Page_Permission_Backing implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Creates a new instance of Page_Permission_Backing
     */
    private String PID = null, Page_Name = null, Page_Description = null;
    int PageID, UserID, GroupID;

    private boolean disFlag = false;
    private boolean disFlag2 = true, addUserFlag = true,
            addGroupFlag = true, deleteUserFlag = true, deleteGroupFlag = true, tabDisable = true;
    PagePermissionJpaController PagesJPA = new PagePermissionJpaController();
    UsersJpaController UsersJPA = new UsersJpaController();
    GroupsJpaController GroupsJPA = new GroupsJpaController();
    ApplicationPagesJpaController AppPagesJPA = new ApplicationPagesJpaController();

    List<ApplicationPages> AppPages;
    ApplicationPages AppPagesEntity = new ApplicationPages();
    ApplicationPages AppPagesSelected = new ApplicationPages();
    private List<PagePermission> PageList;
    PagePermission PagePermissionEntity = new PagePermission();
    PagePermission PagePermissionSelected = new PagePermission();

    Users UsersEntity = new Users();
    private List<Groups> GroupList;
    private List<Users> userList;
    Groups GroupsEntity = new Groups();

    FacesContext context;

    public Page_Permission_Backing() {
        AppPages = AppPagesJPA.findApplicationPagesEntities();
        for(ApplicationPages app:AppPages){
           
        }
        PageList = PagesJPA.findPagePermissionEntities();
        PageList.clear();
        userList = new ArrayList<>();
        
        GroupList = new ArrayList<>();
        

    }

    public void resetValues() {
        
        PageID = 0;
        PageList = null;
        userList = null;
        GroupList = null;
        Page_Name = "";
        Page_Description = "";
        disFlag = false;
        addUserFlag = addGroupFlag = deleteGroupFlag = deleteUserFlag = disFlag2 = tabDisable = true;
        UserID = 0;
        GroupID = 0;
    }

    public void userSelected(ValueChangeEvent Event) {
        if (Event.getNewValue() != null) {
            addUserFlag = false;

        } else {
            addUserFlag = true;
        }
    }

    public void groupSelected(ValueChangeEvent Event) {
        if (Event.getNewValue() != null) {
            addGroupFlag = false;

        } else {
            addGroupFlag = true;
        }
    }

    public String gettingArabic(String key){
        Map<String, String> avalablePages = new HashMap<>();
        avalablePages= (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PagesALL");
        
        String value="";
        value=avalablePages.get(key+".xhtml");
        
        return value;
    }
    public void selectPageListner(ValueChangeEvent Event) {
        if (Event.getNewValue() != null) {
            PageID = Integer.parseInt(Event.getNewValue().toString());
            ApplicationPages e = AppPagesJPA.findApplicationPages(PageID);
//        PagePermission e = PagesJPA.findPagePermission(Integer.parseInt(PageID));
            PID = "" + e.getPageID();
            Page_Name = e.getAppPageName();
            Page_Description = e.getPageDescription();
            AppPagesSelected = e;

            disFlag = true;
            disFlag2 = false;
            tabDisable = false;

            PageList = PagesJPA.DocTypeDataDef(Page_Name);
            GroupList = new ArrayList<>();
            userList = new ArrayList<>();
            for (PagePermission pp : PageList) {
                if (pp.getGroupID() != null) {
                    GroupList.add(pp.getGroupID());
                }
                if (pp.getUserID() != null) {
                    userList.add(pp.getUserID());
                }
            }
        } else {
            PageList = null;
            Page_Name = "";
            Page_Description = "";
            UserID = 0;
            GroupID = 0;
            disFlag = false;
            disFlag2 = true;
            tabDisable = true;
        }
    }

    public void New() throws Exception {

        context = FacesContext.getCurrentInstance();
       
        if (Page_Name != null && !Page_Name.equals("")) {

            if (!AppPagesJPA.pageNameExist(Page_Name)) {
//                AppPagesEntity.setPageID(Integer.parseInt(PID));
                AppPagesEntity.setAppPageName(Page_Name);
                AppPagesEntity.setPageDescription(Page_Description);

                AppPagesJPA.create(AppPagesEntity);
                AppPages = AppPagesJPA.findApplicationPagesEntities();

                Page_Name = "";
                Page_Description = "";

                context.addMessage(null, new FacesMessage("تــم", "إنـشـاء الـصـفـحـة "));
            } else {
                context.addMessage(null, new FacesMessage("تـحـذيـر", "هـذه الـصـفحـة مـوجـودة بالـفـعـل "));

            }

            
        } else {
            context.addMessage(null, new FacesMessage("تـحـذيـر", "أكـمـل الـبـيانـات الـمـطـلوبـة"));
        }
        resetValues();
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        
//        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public void Edit() throws Exception {

        context = FacesContext.getCurrentInstance();

        if (!PID.equals("")) {
            AppPagesEntity.setPageID(Integer.parseInt(PID));
            AppPagesEntity.setAppPageName(Page_Name);
            AppPagesEntity.setPageDescription(Page_Description);

            AppPagesJPA.editAppPages(AppPagesEntity);
            AppPages = AppPagesJPA.findApplicationPagesEntities();

            context.addMessage(null, new FacesMessage("تــم", "تـعـديـل الـصـفـحـة "));
        }

    }

    public void Delete() throws Exception {
        context = FacesContext.getCurrentInstance();

        AppPagesJPA.destroy(PageID);
        AppPages = AppPagesJPA.findApplicationPagesEntities();
//        PageID = 0;
//        PageList = null;
//        Page_Name = "";
//        Page_Description = "";
//        UserID = 0;
//        GroupID = 0;
//        disFlag = false;
        resetValues();
        disFlag2 = true;
        context.addMessage(null, new FacesMessage("تــم", "إلـغـاء  الـصـفـحـة "));
    }

    public void onAddUser() throws Exception {
        if (UserID != 0) {
            context = FacesContext.getCurrentInstance();
            if (!PagesJPA.userExist(UserID, Page_Name)) {

                PagePermissionEntity.setUserID(UsersJPA.findUsers(UserID));
                PagePermissionEntity.setPageName(AppPagesSelected);

                PagesJPA.create(PagePermissionEntity);
                PageList = PagesJPA.DocTypeDataDef(Page_Name);
                userList.clear();
                for (PagePermission pp : PageList) {

                    if (pp.getUserID() != null) {
                        userList.add(pp.getUserID());
                    }
                }
                context.addMessage(null, new FacesMessage("تــم", "إعـطـاء اذن الولوج لهذه الصفحة للـمـسـخـدم"));

            } else {
                context.addMessage(null, new FacesMessage("تـحـذيـر", "هـذا المـسـخـدم لديه الاذن بالفعل"));

            }
        }
    }

    public void onDeleteUser() throws Exception {
        context = FacesContext.getCurrentInstance();
        if (UsersEntity != null) {
            PagesJPA.destroy(PagesJPA.findByUser(UsersEntity.getUserId(), Page_Name).getPId());
            PageList = PagesJPA.DocTypeDataDef(Page_Name);
            userList.clear();
            for (PagePermission pp : PageList) {
                if (pp.getUserID() != null) {
                    userList.add(pp.getUserID());
                }
            }
            context.addMessage(null, new FacesMessage("تــم", "الغـاء اذن الولوج للصفحة من هذا المستخدم "));
        } else {
            context.addMessage(null, new FacesMessage("تـحـذيـر", "يجب اختيار مستخدم  "));

        }
    }

    public void onAddGroup() throws Exception {
        if (GroupID != 0) {
            context = FacesContext.getCurrentInstance();
           
            if (!PagesJPA.groupExist(GroupID, Page_Name)) {
                PagePermissionEntity.setGroupID(GroupsJPA.findGroups(GroupID));
                PagePermissionEntity.setPageName(AppPagesSelected);
                PagesJPA.create(PagePermissionEntity);
                PageList = PagesJPA.DocTypeDataDef(Page_Name);
                GroupList.clear();
                for (PagePermission pp : PageList) {
                    if (pp.getGroupID() != null) {
                        GroupList.add(pp.getGroupID());
                    }
                }
                context.addMessage(null, new FacesMessage("تــم", "اعطاء اذن الولوج لهذه المجموعة "));
            } else {
                context.addMessage(null, new FacesMessage("تـحـذيـر", "هذه المجموعة لديها اذن الولوج بالفعل"));

            }
        }
    }

    public void onDeleteGroup() throws Exception {
        if (GroupsEntity != null) {
            
            PagesJPA.destroy(PagesJPA.findByGroup(GroupsEntity.getGroupId(), Page_Name).getPId());
            PageList = PagesJPA.DocTypeDataDef(Page_Name);
            GroupList.clear();
            for (PagePermission pp : PageList) {
                if (pp.getGroupID() != null) {
                    GroupList.add(pp.getGroupID());
                }

            }
            context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("تــم", "إالغاء اذن الولوج للصفحة من هذه المجوعة "));
        } else {
            context.addMessage(null, new FacesMessage("تـحـذيـر", "يجب اختيار مجموعة "));

        }
    }

    public boolean isTabDisable() {
        return tabDisable;
    }

    public void setTabDisable(boolean tabDisable) {
        this.tabDisable = tabDisable;
    }

    public Users getUsersEntity() {
        return UsersEntity;
    }

    public void setUsersEntity(Users UsersEntity) {
        this.UsersEntity = UsersEntity;
    }

    public Groups getGroupsEntity() {
        return GroupsEntity;
    }

    public void setGroupsEntity(Groups GroupsEntity) {
        this.GroupsEntity = GroupsEntity;
    }

    public List<Users> getUserList() {
        return userList;
    }

    public void setUserList(List<Users> userList) {
        this.userList = userList;
    }

    public boolean isAddUserFlag() {
        return addUserFlag;
    }

    public void setAddUserFlag(boolean addUserFlag) {
        this.addUserFlag = addUserFlag;
    }

    public boolean isAddGroupFlag() {
        return addGroupFlag;
    }

    public void setAddGroupFlag(boolean addGroupFlag) {
        this.addGroupFlag = addGroupFlag;
    }

    public boolean isDeleteUserFlag() {
        return deleteUserFlag;
    }

    public void setDeleteUserFlag(boolean deleteUserFlag) {
        this.deleteUserFlag = deleteUserFlag;
    }

    public boolean isDeleteGroupFlag() {
        return deleteGroupFlag;
    }

    public void setDeleteGroupFlag(boolean deleteGroupFlag) {
        this.deleteGroupFlag = deleteGroupFlag;
    }

    public boolean isDisFlag2() {
        return disFlag2;
    }

    public void setDisFlag2(boolean disFlag2) {
        this.disFlag2 = disFlag2;
    }

    public boolean isDisFlag() {
        return disFlag;
    }

    public void setDisFlag(boolean disFlag) {
        this.disFlag = disFlag;
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

    public int getPageID() {
        return PageID;
    }

    public void setPageID(int PageID) {
        this.PageID = PageID;
    }

    public List<ApplicationPages> getAppPages() {
        return AppPages;
    }

    public void setAppPages(List<ApplicationPages> AppPages) {
        this.AppPages = AppPages;
    }

    public PagePermission getPagePermissionEntity() {
        return PagePermissionEntity;
    }

    public void setPagePermissionEntity(PagePermission PagePermissionEntity) {
        this.PagePermissionEntity = PagePermissionEntity;
    }

    public PagePermission getPagePermissionSelected() {
        return PagePermissionSelected;
    }

    public void setPagePermissionSelected(PagePermission PagePermissionSelected) {
        this.PagePermissionSelected = PagePermissionSelected;
    }

    public List<PagePermission> getPageList() {
        return PageList;
    }

    public void setPageList(List<PagePermission> PageList) {
        this.PageList = PageList;
    }

    public List<Groups> getGroupList() {
        return GroupList;
    }

    public void setGroupList(List<Groups> GroupList) {
        this.GroupList = GroupList;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPage_Name() {
        return Page_Name;
    }

    public void setPage_Name(String Page_Name) {
        this.Page_Name = Page_Name;
    }

    public String getPage_Description() {
        return Page_Description;
    }

    public void setPage_Description(String Page_Description) {
        this.Page_Description = Page_Description;
    }

}
