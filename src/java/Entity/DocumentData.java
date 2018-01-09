/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author norsin
 */
@Entity
@Table(name = "document_data")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentData.findAll", query = "SELECT d FROM DocumentData d"),
    @NamedQuery(name = "DocumentData.findByFieldName", query = "SELECT d FROM DocumentData d WHERE d.fieldName = :fieldName"),
    @NamedQuery(name = "DocumentData.findByFieldValue", query = "SELECT d FROM DocumentData d WHERE d.fieldValue = :fieldValue"),
    @NamedQuery(name = "DocumentData.findByDocSer", query = "SELECT d FROM DocumentData d WHERE d.docSer.docSer = :docSer"),
    @NamedQuery(name = "DocumentData.findByDocSerFieldName", query = "SELECT d FROM DocumentData d WHERE d.docSer.docSer = :docSer and d.fieldName = :fieldName"),

    @NamedQuery(name = "DocumentData.findByDataID", query = "SELECT d FROM DocumentData d WHERE d.dataID = :dataID"),

    @NamedQuery(name = "Permissions.findAll", query = "SELECT p FROM Permissions p"),
    @NamedQuery(name = "Permissions.findByPermID", query = "SELECT p FROM Permissions p WHERE p.permID = :permID"),
    @NamedQuery(name = "Permissions.findByFieldName", query = "SELECT p FROM Permissions p WHERE p.fieldName = :fieldName"),
    @NamedQuery(name = "Permissions.findByCreating", query = "SELECT p FROM Permissions p WHERE p.creating = :creating"),
    @NamedQuery(name = "Permissions.findByReading", query = "SELECT p FROM Permissions p WHERE p.reading = :reading"),
    @NamedQuery(name = "Permissions.findByUpdating", query = "SELECT p FROM Permissions p WHERE p.updating = :updating"),
    @NamedQuery(name = "Permissions.findByDeleting", query = "SELECT p FROM Permissions p WHERE p.deleting = :deleting"),
    @NamedQuery(name = "Permissions.findByGroupId", query = "SELECT p FROM Permissions p WHERE p.groupId.groupId = :groupId"),
    @NamedQuery(name = "Permissions.findByGroupUserDocId", query = "SELECT DISTINCT p FROM Permissions p WHERE p.doctypeId.doctypeId= :docTypeId and ( p.groupId.groupId = :groupId OR p.userId.userId = :userId)"),
    @NamedQuery(name = "Permissions.findByUserDocId", query = "SELECT p FROM Permissions p WHERE p.doctypeId.doctypeId= :docTypeId and ( p.groupId.groupId = :groupId OR p.userId.userId = :userId) and p.fieldName is NULL"),
    @NamedQuery(name = "Permissions.findByGroupUserDocIdObject", query = "SELECT DISTINCT p FROM Permissions p WHERE  p.fieldName = :fieldName and(p.groupId.groupId = :groupId OR p.userId.userId = :userId)"),
    
    @NamedQuery(name = "Permissions.findByUserFieldname", query = "SELECT DISTINCT p FROM Permissions p WHERE  p.fieldName = :fieldName and p.userId.userId = :userId"),
     @NamedQuery(name = "Permissions.findByUserWOFieldname", query = "SELECT p FROM Permissions p WHERE p.doctypeId.doctypeId= :docTypeId and p.userId.userId = :userId and p.fieldName is NULL"),
   
     @NamedQuery(name = "Permissions.findByGroupFieldname", query = "SELECT DISTINCT p FROM Permissions p WHERE  p.fieldName = :fieldName and p.groupId.groupId = :groupId "),
    @NamedQuery(name = "Permissions.findByGroupDocId", query = "SELECT p FROM Permissions p WHERE p.doctypeId.doctypeId= :docTypeId and  p.groupId.groupId = :groupId and p.fieldName is NULL"),
   
    
    @NamedQuery(name = "Permissions.findByUserId", query = "SELECT p FROM Permissions p WHERE p.userId.userId = :userId")})
public class DocumentData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "FIELD_NAME")
    private String fieldName;
    @Basic(optional = false)
    @Column(name = "FIELD_VALUE")
    private String fieldValue;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Data_ID")
    private Integer dataID;
    @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER")
    @ManyToOne(optional = false)
    private Documents docSer;

    public DocumentData() {
    }

    public DocumentData(Integer dataID) {
        this.dataID = dataID;
    }

    public DocumentData(Integer dataID, String fieldName, String fieldValue) {
        this.dataID = dataID;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Integer getDataID() {
        return dataID;
    }

    public void setDataID(Integer dataID) {
        this.dataID = dataID;
    }

    public Documents getDocSer() {
        return docSer;
    }

    public void setDocSer(Documents docSer) {
        this.docSer = docSer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dataID != null ? dataID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentData)) {
            return false;
        }
        DocumentData other = (DocumentData) object;
        if ((this.dataID == null && other.dataID != null) || (this.dataID != null && !this.dataID.equals(other.dataID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DocumentData[ dataID=" + dataID + " ]";
    }

}
