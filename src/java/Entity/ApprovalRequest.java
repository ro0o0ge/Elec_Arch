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
@Table(name = "approval_request")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ApprovalRequest.findAll", query = "SELECT a FROM ApprovalRequest a"),
    @NamedQuery(name = "ApprovalRequest.findByAppID", query = "SELECT a FROM ApprovalRequest a WHERE a.appID = :appID"),
    @NamedQuery(name = "ApprovalRequest.findByIsApproved", query = "SELECT a FROM ApprovalRequest a WHERE a.isApproved = :isApproved"),
    @NamedQuery(name = "ApprovalRequest.findByAPPUser", query = "SELECT a FROM ApprovalRequest a WHERE a.aPPUser = :aPPUser"),
    @NamedQuery(name = "ApprovalRequest.findByRequestDateAndTime", query = "SELECT a FROM ApprovalRequest a WHERE a.requestDateAndTime = :requestDateAndTime"),
    @NamedQuery(name = "ApprovalRequest.findByApproveDateAndTime", query = "SELECT a FROM ApprovalRequest a WHERE a.approveDateAndTime = :approveDateAndTime"),
    @NamedQuery(name = "ApprovalRequest.findforDocType", query = "SELECT a FROM ApprovalRequest a WHERE a.rUserID.userId = :rUserID and a.docSER.docSer = :docSer and a.aPPUser = :appUser")})

public class ApprovalRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "App_ID")
    private Integer appID;
    @Column(name = "IsApproved")
    private String isApproved;
    @Column(name = "APP_User")
    private Integer aPPUser;
    @Basic(optional = false)
    @Column(name = "RequestDateAndTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDateAndTime;
    @Column(name = "ApproveDateAndTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveDateAndTime;
    @JoinColumn(name = "RUser_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users rUserID;
    @JoinColumn(name = "Doc_SER", referencedColumnName = "DOC_SER")
    @ManyToOne(optional = false)
    private Documents docSER;

    public ApprovalRequest() {
    }

    public ApprovalRequest(Integer appID) {
        this.appID = appID;
    }

    public ApprovalRequest(Integer appID, Date requestDateAndTime) {
        this.appID = appID;
        this.requestDateAndTime = requestDateAndTime;
    }

    public Integer getAppID() {
        return appID;
    }

    public void setAppID(Integer appID) {
        this.appID = appID;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getAPPUser() {
        return aPPUser;
    }

    public void setAPPUser(Integer aPPUser) {
        this.aPPUser = aPPUser;
    }

    public Date getRequestDateAndTime() {
        return requestDateAndTime;
    }

    public void setRequestDateAndTime(Date requestDateAndTime) {
        this.requestDateAndTime = requestDateAndTime;
    }

    public Date getApproveDateAndTime() {
        return approveDateAndTime;
    }

    public void setApproveDateAndTime(Date approveDateAndTime) {
        this.approveDateAndTime = approveDateAndTime;
    }

    public Users getRUserID() {
        return rUserID;
    }

    public void setRUserID(Users rUserID) {
        this.rUserID = rUserID;
    }

    public Documents getDocSER() {
        return docSER;
    }

    public void setDocSER(Documents docSER) {
        this.docSER = docSER;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appID != null ? appID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApprovalRequest)) {
            return false;
        }
        ApprovalRequest other = (ApprovalRequest) object;
        if ((this.appID == null && other.appID != null) || (this.appID != null && !this.appID.equals(other.appID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ApprovalRequest[ appID=" + appID + " ]";
    }

}
