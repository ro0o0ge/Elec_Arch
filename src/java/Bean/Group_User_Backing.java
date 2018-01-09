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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author norsin
 */
@ManagedBean
@ViewScoped
public class Group_User_Backing implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Creates a new instance of Group_User_Backing
     */
    int Id;
    private Groups selectedGroups;
    GroupsJpaController GroupJPA = new GroupsJpaController();
    UserGroupJpaController UserGroupJPA = new UserGroupJpaController();
    Groups groups_entity = new Groups();
    List<Groups> groups;
    List<Groups> groupsCheck;

    private DualListModel<Users> DualUsers;

    private List<Users> source;
    private List<Users> target;

    private List<Users> source_g;
    private List<Users> target_g;

    private List<Users> Temp;

    private int GroupsID = 0;
    private boolean disablebtn = true;

    private FacesContext context;

    public Group_User_Backing() {

        List<Users> s = new ArrayList<>();
        List<Users> t = new ArrayList<>();

        DualUsers = new DualListModel<Users>(s, t);

        groups = GroupJPA.findGroupsEntities();
    }

    public List<Users> getTarget() {
        if (GroupsID != 0) {

//            target = new ArrayList<Users>();
            target = UserGroupJPA.getUserNotInGroup(GroupsID);
        } else {

            return new ArrayList<Users>();
        }
        return target;
    }

    public List<Users> getSource() {
        if (GroupsID != 0) {
//            source = new ArrayList<Users>();
            source = UserGroupJPA.getUserInGroup(GroupsID);
        } else {
            return new ArrayList<Users>();
        }
        return source;
    }

    public void selectMenuListner(ValueChangeEvent Event) throws IOException {
        if (Event != null) {
            if (Event.getNewValue() != null) {
                GroupsID = Integer.parseInt(Event.getNewValue().toString());
                Id = GroupsID;
                source = UserGroupJPA.getUserInGroup(Id);
                target = UserGroupJPA.getUserNotInGroup(Id);
                DualUsers = new DualListModel<Users>(source, target);
               if(GroupsID==0) disablebtn=true;
               else disablebtn = false;
            } 
            else {
                resetValues();

            }
        } else {
            resetValues();
        }
        source_g = source;
        target_g = target;

    }

    public void onTransfer(TransferEvent event) {
        if (event.isAdd()) {
           
            source_g.removeAll((List<Users>) event.getItems());
            target_g.addAll((List<Users>) event.getItems());
        }
        if (event.isRemove()) {
            
            source_g.addAll((List<Users>) event.getItems());
            target_g.removeAll((List<Users>) event.getItems());
        }

        Temp = source_g;
    }

    public void OkChange(ActionEvent actionEvent) throws Exception {
        context = FacesContext.getCurrentInstance();
        UserGroup UG = new UserGroup();
        Groups e = GroupJPA.findGroups(Id);
        UserGroupJPA.destroyG(Id);
        if (!Temp.isEmpty()) {
            for (Users u : Temp) {
                UG.setGroupId(e);
                UG.setUserId(u);
                UserGroupJPA.create(UG);
            }
        }
        context.addMessage(null, new FacesMessage("Successful", "الـتـعـديـل "));

    }

    public void resetValues() throws IOException {
        GroupsID = 0;
        DualUsers.getSource().clear();
        DualUsers.getTarget().clear();
        disablebtn = true;

    }

    public boolean isDisablebtn() {
        return disablebtn;
    }

    public void setDisablebtn(boolean disablebtn) {
        this.disablebtn = disablebtn;
    }

    public int getGroupsID() {
        return GroupsID;
    }

    public void setGroupsID(int GroupsID) {
        this.GroupsID = GroupsID;
    }

    public void setDualUsers(DualListModel<Users> Users) {

        this.DualUsers = Users;
    }

    public DualListModel<Users> getDualUsers() {

        if (GroupsID != 0) {
            source = UserGroupJPA.getUserInGroup(GroupsID);
            target = UserGroupJPA.getUserNotInGroup(GroupsID);

            if (source.isEmpty()) {
                DualUsers = new DualListModel<Users>(source, target);
            }
        } else {
            return new DualListModel<Users>();
        }
        return DualUsers;
    }

    public void setGroups(List<Groups> groups) {
        this.groups = groups;
    }

    public List<Groups> getGroups() {
        return groups;
    }

    public void setSelectedGroups(Groups selectedGroups) {
        this.selectedGroups = selectedGroups;
    }

    public Groups getSelectedGroups() {
        return selectedGroups;
    }
}
