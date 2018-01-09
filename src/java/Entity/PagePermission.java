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
@Table(name = "page_permission")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PagePermission.findAll", query = "SELECT p FROM PagePermission p"),
    @NamedQuery(name = "PagePermission.findByPageName", query = "SELECT p FROM PagePermission p WHERE p.pageName.appPageName = :pageName"),
    @NamedQuery(name = "PagePermission.findByPageNameGroup", query = "SELECT p FROM PagePermission p WHERE p.pageName.appPageName = :pageName and p.groupID.groupId = :groupID"),
    @NamedQuery(name = "PagePermission.findByPageNameUser", query = "SELECT p FROM PagePermission p WHERE p.pageName.appPageName = :pageName and p.userID.userId = :user"),
    @NamedQuery(name = "PagePermission.findByUserID", query = "SELECT p FROM PagePermission p WHERE p.userID.userId = :user"),
    @NamedQuery(name = "PagePermission.findByGroupID", query = "SELECT p FROM PagePermission p WHERE p.groupID.groupId = :group"),
    @NamedQuery(name = "PagePermission.findByPId", query = "SELECT p FROM PagePermission p WHERE p.pId = :pId")})
public class PagePermission implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "P_ID")
    private Integer pId;
    @JoinColumn(name = "Page_Name", referencedColumnName = "App_Page_Name")
    @ManyToOne(optional = false)
    private ApplicationPages pageName;
    @JoinColumn(name = "Group_ID", referencedColumnName = "GROUP_ID")
    @ManyToOne
    private Groups groupID;
    @JoinColumn(name = "User_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private Users userID;

    public PagePermission() {
    }

    public PagePermission(Integer pId) {
        this.pId = pId;
    }

    public Integer getPId() {
        return pId;
    }

    public void setPId(Integer pId) {
        this.pId = pId;
    }

    public ApplicationPages getPageName() {
        return pageName;
    }

    public void setPageName(ApplicationPages pageName) {
        this.pageName = pageName;
    }

    public Groups getGroupID() {
        return groupID;
    }

    public void setGroupID(Groups groupID) {
        this.groupID = groupID;
    }

    public Users getUserID() {
        return userID;
    }

    public void setUserID(Users userID) {
        this.userID = userID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pId != null ? pId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagePermission)) {
            return false;
        }
        PagePermission other = (PagePermission) object;
        if ((this.pId == null && other.pId != null) || (this.pId != null && !this.pId.equals(other.pId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.PagePermission[ pId=" + pId + " ]";
    }
    
}

