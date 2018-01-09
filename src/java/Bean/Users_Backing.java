/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.Users;
import JPA.UsersJpaController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author norsin
 */
@ManagedBean
@ViewScoped
public class Users_Backing {

//    EntityManagerFactory
    UsersJpaController UserJPA = new UsersJpaController();
    private String userName, password, Status;
    private int userId;

    private String  newPass, confirmPassword;

    FacesContext context;
    private boolean disableAdd = false, disableEdit = true;

    public String getCurrentpassword() {
        userName = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName").toString();
        return UserJPA.getUsersByUserName(userName).get(0).getPassword();
    }

   

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public Users getUsers_entity() {
        return users_entity;
    }

    public void setUsers_entity(Users users_entity) {
        this.users_entity = users_entity;
    }
    private String selectedUserId;
    Users users_entity = new Users();
    private List<Users> users = new ArrayList<Users>();
    private int UserID = 0;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
        
    }

    public boolean isDisable() {

        if (UserID == 0) {
            return false;
        }
        return true;
    }

    public void reset() {
        UserID = 0;
        userId = 0;
        userName = "";
        password = "";
        Status = "";
        disableAdd = false;
        disableEdit = true;

    }

    public Object clear() {
        // Add event code here...
        userName = "";
        password = "";
        return null;
    }

//    ArchJavaServiceFacade services = new ArchJavaServiceFacade();
    public Users_Backing() {
        users = UserJPA.findUsersEntities();
//        List<Groups> s = new ArrayList<Groups>();
//        List<Groups> t = new ArrayList<Groups>();
//        groups = new DualListModel<Groups>(s, t);
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void selectMenu2forListner(ValueChangeEvent Event) {
        UserID = Integer.parseInt(Event.getNewValue().toString());
        
    }

    public void selectManageListner(ValueChangeEvent Event) {

        if (Event.getNewValue() != null) {
            UserID = Integer.parseInt(Event.getNewValue().toString());
           
            if (UserID != 0) {
                Users e = UserJPA.findUsers(UserID);
                userId = e.getUserId();
                userName = e.getUserName();
                password = e.getPassword();
                Status = e.getStatus();
                disableAdd = true;
                disableEdit = false;
            } else {
                reset();
            }
        } else {
            reset();
        }
    }

    public String emptyfield() {
        FacesMessage msg;
        if (userName == null || userName.equals("") || password == null || password.equals("")) {

            return "msg";
        }

        return "";
    }

    public void New() throws IOException, Exception {
        users_entity.setUserId(1);
        context = FacesContext.getCurrentInstance();
        
        if (userName == null || userName.equals("") || password == null || password.equals("") || Status.equals("") || Status == null) {
            context.addMessage(null, new FacesMessage("تـحـذيـر", " أكـمـل الـبـيانـات الـمـطـلوبـة  "));

        } else {

            users_entity.setUserName(userName);
            users_entity.setPassword(password);
            users_entity.setStatus(Status);
            if (UserJPA.getUsersByUserName(userName).isEmpty()) {
                UserJPA.create(users_entity);
                users = UserJPA.findUsersEntities();
                context.addMessage(null, new FacesMessage("تــم", " الاضـافـة  "));
                reset();
            } else {
                context.addMessage(null, new FacesMessage("تـحـذيـر", " هـذا الـسـتـخـدم موجود بالفعل  "));

            }
        }

    }

    public void Edit(ActionEvent actionEvent) throws Exception {

        context = FacesContext.getCurrentInstance();

        users_entity.setUserId(userId);
        users_entity.setUserName(userName);
        users_entity.setPassword(password);
        users_entity.setStatus(Status);

        UserJPA.ManageEdit(users_entity);
        users = UserJPA.findUsersEntities();
        context.addMessage(null, new FacesMessage("تــم", " الـتـعـديـل  "));

    }

    public void Delete(ActionEvent actionEvent) throws Exception {
        context = FacesContext.getCurrentInstance();

        List<String> ex = UserJPA.destroy(userId);

        if (ex == null) {

            users = UserJPA.findUsersEntities();
            reset();
            context.addMessage(null, new FacesMessage("تــم", " مـسـح الـمـسـخـدم "));
        } else {
            for (String except : ex) {
                context.addMessage(null, new FacesMessage("تـحـذيـر", "لاا يمكن مسح هذا المستخدم"));
            }
        }

    }

    public void resetPass() {
        newPass = confirmPassword = "";
    }

    public void confirm() throws Exception {
        context = FacesContext.getCurrentInstance();

        if (newPass != null && confirmPassword != null && !newPass.equals("") && !confirmPassword.equals("")) {

            if (newPass.equals(confirmPassword)) {

                Pattern pattern = Pattern.compile("[a-zA-Z0-9]{6,20}");
                Matcher matcher = pattern.matcher(newPass);

                if (matcher.matches()) {
                    
                    users_entity = UserJPA.findUsers(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId").toString()));
                    users_entity.setPassword(confirmPassword);
                    UserJPA.ManageEdit(users_entity);

                    context.addMessage(null, new FacesMessage("تــم", " الـتـعـديـل "));

                } else {

                    context.addMessage(null, new FacesMessage("", "يجب ألا يـزيـد الرقم السري عن 6 أحرف"));

                }
            } else {
                context.addMessage(null, new FacesMessage("تـحـذيـر", "يجب ان يتطابق الرقم السري التأكيدي "));

            }
        } else {
            context.addMessage(null, new FacesMessage("تـحـذيـر", " أكـمـل الـبـيانـات الـمـطـلوبـة "));

        }

    }

    public void confirmPass() throws Exception {

        context = FacesContext.getCurrentInstance();

        userName = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userName").toString();

        users_entity = UserJPA.findUsers(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId").toString()));
        users_entity.setPassword(confirmPassword);
        UserJPA.ManageEdit(users_entity);

        context.addMessage(null, new FacesMessage("تــم", " تـغـيـر الرقم الـسـري  "));

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
}
