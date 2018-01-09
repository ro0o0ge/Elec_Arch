/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author norsin
 */
@Entity
@Table(name = "transaction_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionLog.findAll", query = "SELECT t FROM TransactionLog t order by t.updatedDate desc"),
    @NamedQuery(name = "TransactionLog.findByLogId", query = "SELECT t FROM TransactionLog t WHERE t.logId = :logId"),
    @NamedQuery(name = "TransactionLog.findByFieldName", query = "SELECT t FROM TransactionLog t WHERE t.fieldName = :fieldName"),
    @NamedQuery(name = "TransactionLog.findByOldValue", query = "SELECT t FROM TransactionLog t WHERE t.oldValue = :oldValue"),
    @NamedQuery(name = "TransactionLog.findDeletedDoc", query = "SELECT t FROM TransactionLog t WHERE t.docSer.docSer = :docSer and t.docFlag = 'حذف' order by t.updatedDate desc"),
    @NamedQuery(name = "TransactionLog.findByUpdatedDate", query = "SELECT t FROM TransactionLog t WHERE t.updatedDate = :updatedDate")})
public class TransactionLog implements Serializable {
    @Basic(optional = false)
    @Column(name = "Doc_Flag")
    private String docFlag;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LOG_ID")
    private Integer logId;
    @Basic(optional = false)
    @Column(name = "FIELD_NAME")
    private String fieldName;
    @Column(name = "OLD_VALUE")
    private String oldValue;
    @Basic(optional = false)
    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @JoinColumn(name = "Doc_Ser", referencedColumnName = "DOC_SER")
    @ManyToOne
    private Documents docSer;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private Users userId;

    public TransactionLog() {
    }

    public TransactionLog(Integer logId) {
        this.logId = logId;
    }

    public TransactionLog(Integer logId, String fieldName, Date updatedDate) {
        this.logId = logId;
        this.fieldName = fieldName;
        this.updatedDate = updatedDate;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Documents getDocSer() {
        return docSer;
    }

    public void setDocSer(Documents docSer) {
        this.docSer = docSer;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logId != null ? logId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionLog)) {
            return false;
        }
        TransactionLog other = (TransactionLog) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.TransactionLog[ logId=" + logId + " ]";
    }

    public String getDocFlag() {
        return docFlag;
    }

    public void setDocFlag(String docFlag) {
        this.docFlag = docFlag;
    }
    
}
