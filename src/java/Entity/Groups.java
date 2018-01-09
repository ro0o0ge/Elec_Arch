/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author norsin
 */
@Entity
@Table(name = "groups")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Groups.findAll", query = "SELECT g FROM Groups g"),
    @NamedQuery(name = "Groups.findByGroupId", query = "SELECT g FROM Groups g WHERE g.groupId = :groupId"),
    @NamedQuery(name = "Groups.findByGroupName", query = "SELECT g FROM Groups g WHERE g.groupName = :groupName")})
public class Groups implements Serializable {
    @OneToMany(mappedBy = "groupID")
    private List<Recievers> recieversList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "GROUP_ID")
    private Integer groupId;
    @Basic(optional = false)
    @Column(name = "GROUP_NAME")
    private String groupName;
    @Lob
    @Column(name = "NOTES")
    private String notes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupId")
    private List<UserGroup> userGroupList;
    @OneToMany(mappedBy = "groupID")
    private List<PagePermission> pagePermissionList;
    @OneToMany(mappedBy = "groupId")
    private List<Permissions> permissionsList;

    public Groups() {
    }

    public Groups(Integer groupId) {
        this.groupId = groupId;
    }

    public Groups(Integer groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @XmlTransient
    public List<UserGroup> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(List<UserGroup> userGroupList) {
        this.userGroupList = userGroupList;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupId != null ? groupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Groups)) {
            return false;
        }
        Groups other = (Groups) object;
        if ((this.groupId == null && other.groupId != null) || (this.groupId != null && !this.groupId.equals(other.groupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Groups[ groupId=" + groupId + " ]";
    }

    @XmlTransient
    public List<Recievers> getRecieversList() {
        return recieversList;
    }

    public void setRecieversList(List<Recievers> recieversList) {
        this.recieversList = recieversList;
    }
    
}
