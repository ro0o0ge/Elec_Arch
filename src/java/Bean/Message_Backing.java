/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.Documents;
import Entity.Groups;
import Entity.Message;
import Entity.Recievers;
import Entity.Template;
import Entity.Users;
import JPA.DocumentsJpaController;
import JPA.GroupsJpaController;
import JPA.MessageJpaController;
import JPA.RecieversJpaController;
import JPA.TemplateJpaController;
import JPA.UserGroupJpaController;
import JPA.UsersJpaController;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author GIGABYTE
 */
@ManagedBean
@ViewScoped
public class Message_Backing {

    /**
     * Creates a new instance of Message_Backing
     */
    private Message message = new Message();
    private Recievers Rx = new Recievers();
    private List<String> selectedUsers = new ArrayList<>();
    private List<String> selectedgroups = new ArrayList<>();
    private List<String> selectedDoc = new ArrayList<>();
    private int selectedTemplate;

    private List<Users> usersList = new ArrayList<>();
    private List<Groups> groupsList = new ArrayList<>();
    private List<Template> templateList = new ArrayList<>();
    private List<Documents> docList = new ArrayList<>();

    private String body = "", subject = "";
    private int userID;

    private final MessageJpaController JPAmessage = new MessageJpaController();
    private final UsersJpaController JPAusers = new UsersJpaController();
    private final GroupsJpaController JPAgroups = new GroupsJpaController();
    private final TemplateJpaController JPAtemplate = new TemplateJpaController();
    private final DocumentsJpaController JPAdocument = new DocumentsJpaController();
    private final UserGroupJpaController JPAusergroup = new UserGroupJpaController();
    private final RecieversJpaController JPArecivers = new RecieversJpaController();

    FacesContext context;

    public Message_Backing() {
        usersList = JPAusers.findUsersEntities();
        groupsList = JPAgroups.findGroupsEntities();
        templateList = JPAtemplate.findTemplateEntities();
        docList = JPAdocument.findall();
        userID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId").toString());

    }

    public void resetValues() {
        selectedTemplate = 0;
        selectedUsers = new ArrayList<>();
        selectedgroups = new ArrayList<>();
        selectedDoc = new ArrayList<>();
        body = "";
        subject = "";
    }

    public void putTemplate() {
        if (selectedTemplate != 0) {
            body = JPAtemplate.findTemplate(selectedTemplate).getTBody() + "\n";
        }
    }

    public void sendMessage() {
        context = FacesContext.getCurrentInstance();
        message = new Message();
        Rx = new Recievers();

        if (body.equals("") || body == null) {
            context.addMessage(null, new FacesMessage("تـحـذيـر", " يجب كتابة محتوي الرسالة "));
            return;
        }
        message.setBody(body);

        if (selectedUsers.isEmpty() && selectedgroups.isEmpty()) {
            context.addMessage(null, new FacesMessage("تـحـذيـر", " يجب إختيار المرسل إليه "));
            return;
        }

        if (subject.equals("") || subject == null) {
            message.setSubject("لا يوجد عنوان");
        } else {
            message.setSubject(subject);
        }
//        Calendar cal = Calendar.getInstance();
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        message.setDateSent(new Date());
        message.setSenderID(JPAusers.findUsers(userID));
        JPAmessage.create(message);


        for (String gs : selectedgroups) {
            if (selectedDoc.size() > 0) {
                for (String doc : selectedDoc) {
                    Rx = new Recievers();
                    Rx.setGroupID(JPAgroups.findGroups(Integer.parseInt(gs)));
                    Rx.setMessageID(JPAmessage.findMessage(message.getMessageID()));
                    Rx.setDocSER(JPAdocument.findDocuments(doc));
                    JPArecivers.create(Rx);
                }
            } else {
                Rx = new Recievers();
                Rx.setGroupID(JPAgroups.findGroups(Integer.parseInt(gs)));
                Rx.setMessageID(JPAmessage.findMessage(message.getMessageID()));
                JPArecivers.create(Rx);
            }
        }

        for (String us : selectedUsers) {
            boolean exist = false;
            List<Groups> yy = JPAusergroup.getGroupsForUser(Integer.parseInt(us));
            for (String gs : selectedgroups) {
                if (yy.contains(JPAgroups.findGroups(Integer.parseInt(gs)))) {
                    exist = true;
                }
            }
            if (!exist) {
                if (selectedDoc.size() > 0) {
                    for (String doc : selectedDoc) {
                        Rx = new Recievers();
                        Rx.setUserID(JPAusers.findUsers(Integer.parseInt(us)));
                        Rx.setMessageID(JPAmessage.findMessage(message.getMessageID()));
                        Rx.setDocSER(JPAdocument.findDocuments(doc));
                        JPArecivers.create(Rx);
                    }
                } else {
                    Rx = new Recievers();
                    Rx.setUserID(JPAusers.findUsers(Integer.parseInt(us)));
                    Rx.setMessageID(JPAmessage.findMessage(message.getMessageID()));
                    JPArecivers.create(Rx);
                }
            }
        }
        context.addMessage(null, new FacesMessage("تــم", " الارسال "));
        resetValues();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getSelectedDoc() {
        return selectedDoc;
    }

    public void setSelectedDoc(List<String> selectedDoc) {
        this.selectedDoc = selectedDoc;
    }

    public List<Documents> getDocList() {
        return docList;
    }

    public void setDocList(List<Documents> docList) {
        this.docList = docList;
    }

    public List<String> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<String> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public List<String> getSelectedgroups() {
        return selectedgroups;
    }

    public void setSelectedgroups(List<String> selectedgroups) {
        this.selectedgroups = selectedgroups;
    }

    public int getSelectedTemplate() {
        return selectedTemplate;
    }

    public void setSelectedTemplate(int selectedTemplate) {
        this.selectedTemplate = selectedTemplate;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    public List<Groups> getGroupsList() {
        return groupsList;
    }

    public void setGroupsList(List<Groups> groupsList) {
        this.groupsList = groupsList;
    }

    public List<Template> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<Template> templateList) {
        this.templateList = templateList;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
