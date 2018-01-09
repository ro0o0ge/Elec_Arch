/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.Groups;
import Entity.UserGroup;
import Entity.Users;
import JPA.GroupsJpaController;
import JPA.UserGroupJpaController;
import JPA.UsersJpaController;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author norsin
 */
@ManagedBean
@ViewScoped
public class User_Group_Backing implements Serializable {

    private static final long serialVersionUID = 2L;

    UserGroupJpaController UserGroupJPA = new UserGroupJpaController();
    UsersJpaController UserJPA = new UsersJpaController();
    GroupsJpaController GroupJPA = new GroupsJpaController();

    private List<Users> users;//= new ArrayList<Users>();
    private DualListModel<Groups> groups;
    List<Groups> source_g;//= new ArrayList<Groups>();
    List<Groups> TempSource;
    List<Groups> target_g;//= new ArrayList<Groups>();
    private String selectedUserId;
    Users users_entity = new Users();
    private int UserID = 0;
    private static int UId;
    private boolean disablebtn = true;
    private FacesContext context;

    public boolean isDisablebtn() {
        return disablebtn;
    }

    public void setDisablebtn(boolean disablebtn) {
        this.disablebtn = disablebtn;
    }

    public int getUId() {
        return UId;
    }

    public void setUId(int UId) {
        this.UId = UId;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }
    private List<Groups> source;
    private List<Groups> target;

    /**
     * Creates a new instance of User_Group_Backing
     */
    public User_Group_Backing() {
        users = UserJPA.findUsersEntities();
        List<Groups> s = new ArrayList<Groups>();
        List<Groups> t = new ArrayList<Groups>();
        groups = new DualListModel<Groups>(s, t);
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

    public void setGroups(DualListModel<Groups> Groups) {
        this.groups = Groups;
    }

    public List<Groups> getSource() {
        if (UserID != 0) {
            source = UserGroupJPA.getGroupsForUser(UserID);
        } else {
            return new ArrayList<Groups>();
        }
        return source;
    }

    public List<Groups> getTarget() {
        return target;
    }

    public void setTarget(List<Groups> target) {
        this.target = target;
    }

    public DualListModel<Groups> getGroups() {
        if (UserID != 0) {
            source = UserGroupJPA.getGroupsForUser(UserID);
            target = UserGroupJPA.getNotGroupsForUser(UserID);
            groups = new DualListModel<Groups>(source, target);
        } else {
            return new DualListModel<Groups>();
        }
        return groups;
    }

    public void selectMenuListner(ValueChangeEvent Event) throws IOException {

        if (Event != null) {
            if (Event.getNewValue() != null) {
                UserID = Integer.parseInt(Event.getNewValue().toString());
                UId = UserID;
                source = UserGroupJPA.getGroupsForUser(UId);
                target = UserGroupJPA.getNotGroupsForUser(UId);
                groups = new DualListModel<Groups>(source, target);
               if(UId==0) disablebtn=true;
               else disablebtn = false;
            } else {
                resetValues();
            }
        } else {
//            disablebtn=true;
            resetValues();
        }
        source_g = source;
        target_g = target;
    }

    public void onTransfer(TransferEvent event) {
        if (event.isAdd()) {

            source_g.removeAll((List<Groups>) event.getItems());
            target_g.addAll((List<Groups>) event.getItems());

        } else if (event.isRemove()) {
            source_g.addAll((List<Groups>) event.getItems());
            target_g.removeAll((List<Groups>) event.getItems());

        }
        TempSource = source_g;

    }

    public void resetValues() throws IOException {
        UserID = 0;
        groups.getSource().clear();
        groups.getTarget().clear();
        disablebtn = true;

//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public void Change(ActionEvent actionEvent) throws Exception {
        context = FacesContext.getCurrentInstance();
        UserGroup Temp = new UserGroup();
        Users user = new Users(UId);

        UserGroupJPA.destroyU(UId);
        if (!TempSource.isEmpty()) {
            for (Groups g : TempSource) {
                Temp.setGroupId(g);
                Temp.setUserId(user);
                UserGroupJPA.create(Temp);

            }
            context.addMessage(null, new FacesMessage("تــم", "الـعـديـل "));

        } else {
            context.addMessage(null, new FacesMessage("تــم", "الـعـديـل "));

        }

    }
}
