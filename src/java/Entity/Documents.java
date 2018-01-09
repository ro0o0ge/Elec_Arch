/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author norsin
 */
@Entity
@Table(name = "documents")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documents.findAll", query = "SELECT d FROM Documents d where d.deleted = 'false' "),
    
    @NamedQuery(name = "Documents.findByDocSer", query = "SELECT d FROM Documents d WHERE d.docSer = :docSer"),
    @NamedQuery(name = "Documents.findByFileName", query = "SELECT d FROM Documents d WHERE d.fileName = :fileName"),
    @NamedQuery(name = "Documents.findByCreatedDate", query = "SELECT d FROM Documents d WHERE d.createdDate = :createdDate"),
    @NamedQuery(name = "Documents.findByLocation", query = "SELECT d FROM Documents d WHERE d.location = :location"),
    @NamedQuery(name = "Documents.findByOwnerId", query = "SELECT d FROM Documents d WHERE d.ownerId.ownerId = :ownerId"),
    @NamedQuery(name = "Documents.findByDeleted", query = "SELECT d FROM Documents d WHERE d.deleted = 'true'"),
    @NamedQuery(name = "Documents.findByDelete", query = "SELECT d FROM Documents d WHERE d.deleted = :delete")})
public class Documents implements Serializable {
    @OneToMany(mappedBy = "docSER")
    private List<Recievers> recieversList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docSer")
    private List<Attachment> attachmentList;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DOC_SER")
    private String docSer;
    @Basic(optional = false)
    @Column(name = "FILE_NAME")
    private String fileName;
    @Basic(optional = false)
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "Location")
    private String location;
    @Basic(optional = false)
    @Column(name = "Deleted")
    private String deleted;
    @JoinColumn(name = "DOCTYPE_ID", referencedColumnName = "DOCTYPE_ID")
    @ManyToOne(optional = false)
    private DocTypes doctypeId;
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "OWNER_ID")
    @ManyToOne(optional = false)
    private DocOwners ownerId;
    @JoinColumn(name = "HOME_ID", referencedColumnName = "HOME_ID")
    @ManyToOne(optional = false)
    private DocHomes homeId;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docSer")
    private List<DocumentData> documentDataList;
    @OneToMany(mappedBy = "docSer")
    private List<TransactionLog> transactionLogList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docSER")
    private List<ApprovalRequest> approvalRequestList;

    public Documents() {
    }

    public Documents(String docSer) {
        this.docSer = docSer;
    }

    public Documents(String docSer, String fileName, Date createdDate, String location, String delete) {
        this.docSer = docSer;
        this.fileName = fileName;
        this.createdDate = createdDate;
        this.location = location;
        this.deleted = delete;
    }

    public String getDocSer() {
        return docSer;
    }

    public void setDocSer(String docSer) {
        this.docSer = docSer;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDelete() {
        return deleted;
    }

    public void setDelete(String delete) {
        this.deleted = delete;
    }

    public DocTypes getDoctypeId() {
        return doctypeId;
    }

    public void setDoctypeId(DocTypes doctypeId) {
        this.doctypeId = doctypeId;
    }

    public DocOwners getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(DocOwners ownerId) {
        this.ownerId = ownerId;
    }

    public DocHomes getHomeId() {
        return homeId;
    }

    public void setHomeId(DocHomes homeId) {
        this.homeId = homeId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @XmlTransient
    public List<DocumentData> getDocumentDataList() {
        return documentDataList;
    }

    public void setDocumentDataList(List<DocumentData> documentDataList) {
        this.documentDataList = documentDataList;
    }

    @XmlTransient
    public List<TransactionLog> getTransactionLogList() {
        return transactionLogList;
    }

    public void setTransactionLogList(List<TransactionLog> transactionLogList) {
        this.transactionLogList = transactionLogList;
    }

    @XmlTransient
    public List<ApprovalRequest> getApprovalRequestList() {
        return approvalRequestList;
    }

    public void setApprovalRequestList(List<ApprovalRequest> approvalRequestList) {
        this.approvalRequestList = approvalRequestList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (docSer != null ? docSer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documents)) {
            return false;
        }
        Documents other = (Documents) object;
        if ((this.docSer == null && other.docSer != null) || (this.docSer != null && !this.docSer.equals(other.docSer))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Documents[ docSer=" + docSer + " ]";
    }

    @XmlTransient
    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    @XmlTransient
    public List<Recievers> getRecieversList() {
        return recieversList;
    }

    public void setRecieversList(List<Recievers> recieversList) {
        this.recieversList = recieversList;
    }
    
}
