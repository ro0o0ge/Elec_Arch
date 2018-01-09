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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUserId", query = "SELECT u FROM Users u WHERE u.userId = :userId"),
    @NamedQuery(name = "Users.findByUserName", query = "SELECT u FROM Users u WHERE u.userName = :userName"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
    @NamedQuery(name = "Users.findByActiveDate", query = "SELECT u FROM Users u WHERE u.activeDate = :activeDate"),
    @NamedQuery(name = "Users.findByUserNamePass", query = "SELECT u FROM Users u WHERE u.userName = :userName and u.password = :password"),
    @NamedQuery(name = "Users.findByStatus", query = "SELECT u FROM Users u WHERE u.status = :status")})
public class Users implements Serializable {
    @OneToMany(mappedBy = "userID")
    private List<Recievers> recieversList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "senderID")
    private List<Message> messageList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private Integer userId;
    @Basic(optional = false)
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @Column(name = "ACTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activeDate;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<UserGroup> userGroupList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private List<Documents> documentsList;
    @OneToMany(mappedBy = "userId")
    private List<TransactionLog> transactionLogList;
    @OneToMany(mappedBy = "approverID")
    private List<DocTypes> docTypesList;
    @OneToMany(mappedBy = "userID")
    private List<PagePermission> pagePermissionList;
    @OneToMany(mappedBy = "userId")
    private List<Permissions> permissionsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rUserID")
    private List<ApprovalRequest> approvalRequestList;

    public Users() {
    }

    public Users(Integer userId) {
        this.userId = userId;
    }

    public Users(Integer userId, String userName, Date activeDate, String status) {
        this.userId = userId;
        this.userName = userName;
        this.activeDate = activeDate;
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public List<UserGroup> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(List<UserGroup> userGroupList) {
        this.userGroupList = userGroupList;
    }

    @XmlTransient
    public List<Documents> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(List<Documents> documentsList) {
        this.documentsList = documentsList;
    }

    @XmlTransient
    public List<TransactionLog> getTransactionLogList() {
        return transactionLogList;
    }

    public void setTransactionLogList(List<TransactionLog> transactionLogList) {
        this.transactionLogList = transactionLogList;
    }

    @XmlTransient
    public List<DocTypes> getDocTypesList() {
        return docTypesList;
    }

    public void setDocTypesList(List<DocTypes> docTypesList) {
        this.docTypesList = docTypesList;
    }

    @XmlTransient
    public List<PagePermission> getPagePermissionList() {
        return pagePermissionList;
    }

    public void setPagePermissionList(List<PagePermission> pagePermissionList) {
        this.pagePermissionList = pagePermissionList;
    }

    @XmlTransient
    public List<Permissions> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(List<Permissions> permissionsList) {
        this.permissionsList = permissionsList;
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
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Users[ userId=" + userId + " ]";
    }

    @XmlTransient
    public List<Recievers> getRecieversList() {
        return recieversList;
    }

    public void setRecieversList(List<Recievers> recieversList) {
        this.recieversList = recieversList;
    }

    @XmlTransient
    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

}
