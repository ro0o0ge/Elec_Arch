/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.ApprovalRequest;
import JPA.ApprovalRequestJpaController;
import JPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author norsin
 */
@ManagedBean
@ViewScoped
public class Approval_Backing implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Creates a new instance of Approval_Backing
     */
    private List<ApprovalRequest> AppRequestList;
    private ApprovalRequestJpaController approvalJPA = new ApprovalRequestJpaController();
    private Date date = new java.util.Date();
    private FacesContext context;

    public Approval_Backing() {
        AppRequestList = approvalJPA.
                findByAppUser(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId").toString()));

    }

    public void Edit() throws Exception, NonexistentEntityException {

        boolean flag = false;

        context = FacesContext.getCurrentInstance();
        for (ApprovalRequest appReq : AppRequestList) {
            if (appReq.getIsApproved().toLowerCase().equals("pending")) {
                flag = true;

                break;
            }

        }
        if (!flag) {
            for (ApprovalRequest appReq : AppRequestList) {
                appReq.setApproveDateAndTime(new Timestamp(date.getTime()));
                approvalJPA.edit(appReq);
                AppRequestList = approvalJPA.
                        findByAppUser(Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId").toString()));
                
            }
            context.addMessage(null, new FacesMessage("تــم", " إعــطــاء الاذن  للـمـستـخـدم "));

        } else {
            
            context.addMessage(null, new FacesMessage("تــحــذــير", " يـجـب إعـطاء الأذن للـمـسـتخـدمـيـن  "));

        }

    }

    public void onRowEdit(RowEditEvent event) throws Exception, NonexistentEntityException {
        for (ApprovalRequest appReq : AppRequestList) {
            if (appReq.getRUserID().getUserId().equals(((ApprovalRequest) event.getObject()).getRUserID().getUserId())) {
                if (appReq.getDocSER().getDocSer().equals(((ApprovalRequest) event.getObject()).getDocSER().getDocSer())) {

                   
                    Date date = new java.util.Date();
                    appReq.setApproveDateAndTime(new Timestamp(date.getTime()));
                    
                }
            }
        }

    }

    public List<ApprovalRequest> getAppRequestList() {
        return AppRequestList;
    }

    public void setAppRequestList(List<ApprovalRequest> AppRequestList) {
        this.AppRequestList = AppRequestList;
    }
}
