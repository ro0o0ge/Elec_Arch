/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.Attachment;
import Entity.Documents;
import Entity.Groups;
import Entity.Recievers;
import JPA.AttachmentJpaController;
import JPA.RecieversJpaController;
import JPA.UserGroupJpaController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author GIGABYTE
 */
@ManagedBean
@ViewScoped
public class Recievers_Backing {

    private List<Recievers> inboxList = new ArrayList<>();
    private Recievers selectedMsg = new Recievers();

    private List<Attachment> AttachList;
    private Map<String, String> files = new HashMap<>();
    private int userID;
    private String srcpic = "";

    private final RecieversJpaController JPARecievers = new RecieversJpaController();
    private final AttachmentJpaController JPAattachment = new AttachmentJpaController();
    private final UserGroupJpaController JPAusergroup = new UserGroupJpaController();

    public Recievers_Backing() {
        userID = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId").toString());
        inboxList = JPARecievers.findRecieverByUser(userID);
        inboxList = formatinboxList();
        for (Groups g : JPAusergroup.getGroupsForUser(userID)) {
            if (JPARecievers.findRecieverByGroup(g.getGroupId()).size() > 0) {
                inboxList.addAll(JPARecievers.findRecieverByGroup(g.getGroupId()));
            }
        }
//        Date max=new Date();
//        int maxIndex=0;
//        for (int i = 0; i < inboxList.size(); i++) {
//            max=inboxList.get(i).getMessageID().getDateSent();
//            maxIndex=i;
//            for (int j = inboxList.size() - 1; j > i; j--) {
//                if (max.getDate() > 
//                        inboxList.get(j).getMessageID().getDateSent().getDate()) {
//                    
//                    max=inboxList.get(j).getMessageID().getDateSent();
//                    maxIndex=j;
//                }
//            }
//            inboxList.set(maxIndex, inboxList.get(maxIndex));
//
//        }

    }

    public List<Recievers> formatinboxList() {
        List<Recievers> newInbox = new ArrayList<>();
        boolean exist = false;
        for (Recievers Rx : inboxList) {
            List<Recievers> t = JPARecievers.findBymsgId(Rx.getMessageID().getMessageID(), userID);
            if (t.size() > 1) {
                for (Recievers R : newInbox) {
                    if (Objects.equals(R.getMessageID().getMessageID(), Rx.getMessageID().getMessageID())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    newInbox.add(Rx);
                }
            } else {
                newInbox.add(Rx);
            }
        }
        return newInbox;
    }

    // ======= Get Attached Files =====================
    public Map<String, String> findAttachfiles(Recievers Rxmsg) {
        if (Rxmsg != null) {
            files = new HashMap<>();
            for (Recievers R : JPARecievers.findBymsgId(Rxmsg.getMessageID().getMessageID(), userID)) {
                Documents selectedDocument = R.getDocSER();
                String T1 = "";

                if (selectedDocument != null) {
                    AttachList = JPAattachment.findBydocSer(selectedDocument.getDocSer());
                    for (Attachment aa : AttachList) {
                        String Temp = aa.getHomeId().getHomePath().toUpperCase();
                        String Temp2 = aa.getHomeId().getHomePath();
                        if (Temp2.length() > 3) {
                            Temp2 = Temp2.substring(3);
                        } else {
                            Temp2 = "";
                        }
                        switch (Temp.charAt(0)) {
                            case 'C':
                                T1 = "/Docs1/" + Temp2 + aa.getName();
                                break;
                            case 'D':
                                T1 = "/Docs2/" + Temp2 + aa.getName();
                                break;
                            case 'N':
                                T1 = "/Docs3/" + Temp2 + aa.getName();
                                break;
                            default:
                                break;
                        }
                        System.out.println("path "+T1+" "+aa.getName());
                        if (selectedDocument.getDelete().equals("false")) {
                            files.put(T1, aa.getName());
                        }
                    }
                }
            }
        }
        return files;
    }

    // ================== get Document Pictures ===============
    public Map<String, String> findDocPicture(Recievers Rxmsg) {
        if (Rxmsg != null) {
            files = new HashMap<>();
            for (Recievers R : JPARecievers.findBymsgId(Rxmsg.getMessageID().getMessageID(), userID)) {
                Documents selectedDocument = R.getDocSER();
                String T2 = "";

                if (selectedDocument != null) {

                    String Temp = selectedDocument.getHomeId().getHomePath().toUpperCase();
                    String Temp2 = selectedDocument.getHomeId().getHomePath();
                    if (Temp2.length() > 3) {
                        Temp2 = Temp2.substring(3);
                    } else {
                        Temp2 = "";
                    }
                    switch (Temp.charAt(0)) {
                        case 'C':
                            T2 = "/Docs1/" + Temp2 + selectedDocument.getFileName();
                            break;
                        case 'D':
                            T2 = "/Docs2/" + Temp2 + selectedDocument.getFileName();
                            break;
                        case 'N':
                            T2 = "/Docs3/" + Temp2 + selectedDocument.getFileName();
                            break;
                        default:
                            break;
                    }
                    if (selectedDocument.getDelete().equals("false")) {
                        files.put(T2, selectedDocument.getDocSer());
                    }

                }
            }
        }
        return files;
    }

    public void getPic(String key) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('imgdlg').show();");

        srcpic = key;

    }

    public String getSrcpic() {
        return srcpic;
    }

    public void setSrcpic(String srcpic) {
        this.srcpic = srcpic;
    }

// ============== Setter Getter =================== 
    public List<Recievers> getInboxList() {
        return inboxList;
    }

    public void setInboxList(List<Recievers> inboxList) {
        this.inboxList = inboxList;
    }

    public Recievers getSelectedMsg() {
        return selectedMsg;
    }

    public void setSelectedMsg(Recievers selectedMsg) {
        this.selectedMsg = selectedMsg;
    }

}
