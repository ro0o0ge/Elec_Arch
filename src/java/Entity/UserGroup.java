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
@Table(name = "user_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserGroup.findAll", query = "SELECT u FROM UserGroup u"),
    @NamedQuery(name = "UserGroup.findByUgId", query = "SELECT u FROM UserGroup u WHERE u.ugId = :ugId"),
    @NamedQuery(name = "UserGroup.findByUserId", query = "SELECT u FROM UserGroup u WHERE u.userId.userId = :uId"),
    @NamedQuery(name = "UserGroup.findByGroupId", query = "SELECT u FROM UserGroup u WHERE u.groupId.groupId = :gId"),
    @NamedQuery(name = "UserGroup.GroupsContainUser", query = "SELECT distinct u FROM Users u join UserGroup ug WHERE ug.userId.userId = u.userId and ug.groupId.groupId = :groupId"),
    @NamedQuery(name = "UserGroup.GroupsHaveNotUser", query = "SELECT distinct u FROM Users u left join UserGroup ug WHERE ug.groupId.groupId !=:groupId and u.userId not in(SELECT distinct u.userId FROM Users u join UserGroup ug WHERE ug.userId.userId = u.userId and ug.groupId.groupId =:groupId) or ug.groupId.groupId is null"),
    @NamedQuery(name = "UserGroup.UserInGroup", query = "SELECT distinct u FROM Groups u join UserGroup ug WHERE u.groupId = ug.groupId.groupId and ug.userId.userId= :userId"),
    @NamedQuery(name = "UserGroup.UserNotInGroup", query = "SELECT distinct u FROM Groups u left join UserGroup ug WHERE ug.userId.userId!=:userId and u.groupId not in (SELECT distinct u.groupId FROM Groups u join UserGroup ug WHERE u.groupId = ug.groupId.groupId and ug.userId.userId=:userId)or ug.userId.userId is null")})
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "UG_ID")
    private Integer ugId;
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")
    @ManyToOne(optional = false)
    private Groups groupId;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users userId;

    public UserGroup() {
    }

    public UserGroup(Integer ugId) {
        this.ugId = ugId;
    }

    public Integer getUgId() {
        return ugId;
    }

    public void setUgId(Integer ugId) {
        this.ugId = ugId;
    }

    public Groups getGroupId() {
        return groupId;
    }

    public void setGroupId(Groups groupId) {
        this.groupId = groupId;
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
        hash += (ugId != null ? ugId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserGroup)) {
            return false;
        }
        UserGroup other = (UserGroup) object;
        if ((this.ugId == null && other.ugId != null) || (this.ugId != null && !this.ugId.equals(other.ugId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.UserGroup[ ugId=" + ugId + " ]";
    }

}
