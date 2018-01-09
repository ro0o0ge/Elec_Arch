package Bean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Entity.DocumentData;
import Entity.Documents;
import Entity.TransactionLog;
import JPA.DocumentsJpaController;
import JPA.TransactionLogJpaController;
import java.io.Serializable;
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
public class UndoDeletedDoc_Backing implements Serializable {

    private static final long serialVersionUID = 2L;
    /**
     * Creates a new instance of viewDocuments_Backing
     */

    private List<Documents> DocumentsList = new ArrayList<>();
    private DocumentData selectedDocument;
    private final DocumentsJpaController JPADocs = new DocumentsJpaController();
    private FacesContext context;
    TransactionLogJpaController JPALOG = new TransactionLogJpaController();
    String DUser = "";

    Date DDate = new Date();

    public UndoDeletedDoc_Backing() {
        DocumentsList = JPADocs.findByDeleted();
    }

    public void logData(String docser) {
        List<TransactionLog> tt = JPALOG.GetDeletedLOG(docser);
        for (TransactionLog dd : tt) {
            DDate = dd.getUpdatedDate();
            DUser = dd.getUserId().getUserName().toString();
            break;
        }

    }

    public void undeletProcess(String serialID) throws Exception {
        Documents Temp = JPADocs.findByID(serialID);
        Temp.setDelete("false");
        JPADocs.editDelete(Temp);
        context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("تـم", "إعادة المستندالمحذوف  "));
        DocumentsList = JPADocs.findByDeleted();
    }

    public String changeDoc2(Documents selectedDocument) {
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
        }
        return T2;
    }

    public String getDUser() {
        return DUser;
    }

    public void setDUser(String DUser) {
        this.DUser = DUser;
    }

    public Date getDDate() {
        return DDate;
    }

    public void setDDate(Date DDate) {
        this.DDate = DDate;
    }

    public DocumentData getSelectedDocument() {
        return selectedDocument;
    }

    public void setSelectedDocument(DocumentData selectedDocument) {
        this.selectedDocument = selectedDocument;
    }

    public List<Documents> getDocList() {
        return DocumentsList;
    }

    public void setDocList(List<Documents> DocList) {
        this.DocumentsList = DocList;
    }

}
