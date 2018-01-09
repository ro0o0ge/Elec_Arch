/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.Groups;
import Entity.Recievers;
import Entity.Users;
import JPA.RecieversJpaController;
import JPA.UserGroupJpaController;
import JPA.UsersJpaController;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author GIGABYTE
 */
@ManagedBean
@SessionScoped

public class Login_Backing implements Serializable {

    private static final long serialVersionUID = 2L;

    private List<Recievers> inboxList = new ArrayList<>();
    private HtmlInputText inputText1;
    private HtmlOutputText wrongMessage;
    Users SessionUser;
    private final UsersJpaController UserJPA = new UsersJpaController();
    private final RecieversJpaController JPARecievers = new RecieversJpaController();
    private final UserGroupJpaController JPAusergroup = new UserGroupJpaController();

    private String userName, password, userId, Status;
    private FacesContext context;

    public void logIn() throws IOException {
        context = FacesContext.getCurrentInstance();
        if (!(UserJPA.isUser(userName, password))) {
            FacesMessage msg = new FacesMessage("اســم المـسـتـخـدم أو الـرقـم الـسري غـير صـحـيح", "");
            context.addMessage(null, msg);
            return;
        }
        SessionUser = UserJPA.getUsersByUserName(userName).get(0);
        if (SessionUser.getStatus().equals("0")) {
//            correct = "Login.xhtml";
            FacesMessage msg = new FacesMessage("أنـت موقوف , أرجـع للأدمــن", "");
            context.addMessage(null, msg);
            return;
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userName", SessionUser.getUserName());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", SessionUser.getUserId());

        inboxList = JPARecievers.findRecieverByUser(SessionUser.getUserId());
        for (Groups g : JPAusergroup.getGroupsForUser(SessionUser.getUserId())) {
            if (JPARecievers.findRecieverByGroup(g.getGroupId()).size() > 0) {
                for (Recievers r : JPARecievers.findRecieverByGroup(g.getGroupId())) {
                    if (!inboxList.contains(r)) {
                        inboxList.add(r);
                    }
                }

            }
        }
        
        List<Recievers> temp=new ArrayList<>();
        for(Recievers r:inboxList){
            if(temp.isEmpty()){
                temp.add(r);
                
            }
            else{
                boolean exist=false;
                for(Recievers t:temp){
                    if(t.getMessageID().getMessageID()==r.getMessageID().getMessageID()){
                        exist=true;
                    }
                }
                if(!exist){
                    temp.add(r);
                }
            }
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("inbox", inboxList.size());

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("Home.xhtml");
    }

    public void LogOut() throws IOException {

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
        nh.handleNavigation(facesContext, null, "Login.xhtml");

    }

    public HtmlInputText getInputText1() {
        return inputText1;
    }

    public void setInputText1(HtmlInputText inputText1) {
        this.inputText1 = inputText1;
    }

    // ============ Set_Get_Mehtods ===================== //
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HtmlOutputText getWrongMessage() {
        return wrongMessage;
    }

    public void setWrongMessage(HtmlOutputText wrongMessage) {
        this.wrongMessage = wrongMessage;
    }

    public UsersJpaController getUserJPA() {
        return UserJPA;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

}
