/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.Groups;
import JPA.GroupsJpaController;
import java.util.ArrayList;
import java.util.List;
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
public class Group_Backing {

    private int GroupID = 0;
    private int groupId;
    private String groupName;
    private String note;
    private String SelectedGroupID;
    private boolean disablebtn = false;
    private boolean diableEdit=true;

    private GroupsJpaController GroupJPA = new GroupsJpaController();
    private Groups groups_entity;// = new Groups();

    private List<Groups> groups = new ArrayList<Groups>();

    private FacesContext context;

    public boolean isDisablebtn() {
        return disablebtn;
    }

    public void setDisablebtn(boolean disablebtn) {
        this.disablebtn = disablebtn;
    }

    public Group_Backing() {
        groups = GroupJPA.findGroupsEntities();
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void selectManageListner(ValueChangeEvent Event) {
        
        if (Event.getNewValue() != null) {
            GroupID = Integer.parseInt(Event.getNewValue().toString());
            Groups e = GroupJPA.findGroups(GroupID);
            groupId = e.getGroupId();
            groupName = e.getGroupName();
            note = e.getNotes();
            disablebtn = true;
            diableEdit=false;
        } else {
            disablebtn = false;
            diableEdit=true;
        }
    }

    public void selectMenu2forListner(ValueChangeEvent Event) {
        GroupID = Integer.parseInt(Event.getNewValue().toString());

    }

    public void Delete() throws Exception {

        context = FacesContext.getCurrentInstance();

        GroupJPA.destroy(groupId);

        groups = GroupJPA.findGroupsEntities();
        reset();
        context.addMessage(null, new FacesMessage("تـم", " إلـغـاء الـمـجـموعـة "));

    }

    public void Editing() throws Exception {
       
        if(groupId==0 || GroupID==0){}
        else{
        groups_entity=new Groups();
        groups_entity.setGroupId(groupId);
        groups_entity.setGroupName(groupName);
        groups_entity.setNotes(note);

        GroupJPA.ManageEdit(groups_entity);
        groups = GroupJPA.findGroupsEntities();
        
        context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("تـم", " الـتـعـديـل "));
        }
//        return "";
    }

    public void New() throws Exception {

        groups_entity=new Groups();
        
        groups_entity.setGroupId(0);
        context = FacesContext.getCurrentInstance();

        if (groupName == null || groupName.equals("")) {
            context.addMessage(null, new FacesMessage("تـحـذيـر", " أكـمـل الـبـيانـات الـمـطـلوبـة  "));

        } else {

            if (!GroupJPA.isGroupExist(groupName)) {
                groups_entity.setGroupName(groupName);
                groups_entity.setNotes(note);
                GroupJPA.create(groups_entity);
                groups = GroupJPA.findGroupsEntities();
                context.addMessage(null, new FacesMessage("تـم", " إضـافـة الـمـجـموعـة  "));
            reset();
            } else {
                context.addMessage(null, new FacesMessage("تـحـذيـر", " هـذه الـمـجـمـوعـة مـوجـودة بالـفـعل  "));

            }
        }
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public boolean isDisable(String ID) {

        if (GroupID == 0) {
            return false;
        }
        return true;
    }


    public void reset() {
        GroupID = 0;
        groupId = 0;
        groupName = "";
        note = "";
        disablebtn = false;
        diableEdit=true;

    }

    public boolean isDiableEdit() {
        return diableEdit;
    }

    public void setDiableEdit(boolean diableEdit) {
        this.diableEdit = diableEdit;
    }
    
    

    public Groups getGroups_entity() {
        return groups_entity;
    }

    public void setGroups_entity(Groups groups_entity) {
        this.groups_entity = groups_entity;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int GroupID) {
        this.GroupID = GroupID;
    }

    public void setSelectedGroupID(String SelectedGroupID) {

        this.SelectedGroupID = SelectedGroupID;
    }

    public String getSelectedGroupID() {
        return SelectedGroupID;
    }

    public void setGroups(List<Groups> groups) {
        this.groups = groups;
    }

    public List<Groups> getGroups() {
        return groups;
    }

}
