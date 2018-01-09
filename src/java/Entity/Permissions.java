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
@Table(name = "permissions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permissions.findAll", query = "SELECT p FROM Permissions p"),
    @NamedQuery(name = "Permissions.findByPermID", query = "SELECT p FROM Permissions p WHERE p.permID = :permID"),
    @NamedQuery(name = "Permissions.findByFieldName", query = "SELECT p FROM Permissions p WHERE p.fieldName = :fieldName"),
    @NamedQuery(name = "Permissions.findByCreating", query = "SELECT p FROM Permissions p WHERE p.creating = :creating"),
    @NamedQuery(name = "Permissions.findByReading", query = "SELECT p FROM Permissions p WHERE p.reading = :reading"),
    @NamedQuery(name = "Permissions.findByFieldNameDocTypeGroupID", query = "SELECT p FROM Permissions p WHERE p.fieldName = :fieldName and p.doctypeId.doctypeId =:doctypeId and p.groupId.groupId = :groupId"),
    @NamedQuery(name = "Permissions.findByFieldNameDocTypeUserID", query = "SELECT p FROM Permissions p WHERE p.fieldName = :fieldName and p.doctypeId.doctypeId =:doctypeId and p.userId.userId = :userId"),
    
    @NamedQuery(name = "Permissions.findByDocTypeUserID", query = "SELECT p FROM Permissions p WHERE p.doctypeId.doctypeId =:doctypeId and p.userId.userId = :userId"),
    @NamedQuery(name = "Permissions.findByDocTypeGroupID", query = "SELECT p FROM Permissions p WHERE p.doctypeId.doctypeId =:doctypeId and p.groupId.groupId = :groupId"),
    
    @NamedQuery(name = "Permissions.findByUpdating", query = "SELECT p FROM Permissions p WHERE p.updating = :updating"),
    @NamedQuery(name = "Permissions.findByDeleting", query = "SELECT p FROM Permissions p WHERE p.deleting = :deleting")})
public class Permissions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Perm_ID")
    private Integer permID;
    @Column(name = "FIELD_NAME")
    private String fieldName;
    @Basic(optional = false)
    @Column(name = "CREATING")
    private String creating;
    @Basic(optional = false)
    @Column(name = "READING")
    private String reading;
    @Basic(optional = false)
    @Column(name = "UPDATING")
    private String updating;
    @Basic(optional = false)
    @Column(name = "DELETING")
    private String deleting;
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")
    @ManyToOne
    private Groups groupId;
    @JoinColumn(name = "DOCTYPE_ID", referencedColumnName = "DOCTYPE_ID")
    @ManyToOne(optional = false)
    private DocTypes doctypeId;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private Users userId;

    public Permissions() {
    }

    public Permissions(Integer permID) {
        this.permID = permID;
    }

    public Permissions(Integer permID, String creating, String reading, String updating, String deleting) {
        this.permID = permID;
        this.creating = creating;
        this.reading = reading;
        this.updating = updating;
        this.deleting = deleting;
    }

    public Integer getPermID() {
        return permID;
    }

    public void setPermID(Integer permID) {
        this.permID = permID;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCreating() {
        return creating;
    }

    public void setCreating(String creating) {
        this.creating = creating;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getUpdating() {
        return updating;
    }

    public void setUpdating(String updating) {
        this.updating = updating;
    }

    public String getDeleting() {
        return deleting;
    }

    public void setDeleting(String deleting) {
        this.deleting = deleting;
    }

    public Groups getGroupId() {
        return groupId;
    }

    public void setGroupId(Groups groupId) {
        this.groupId = groupId;
    }

    public DocTypes getDoctypeId() {
        return doctypeId;
    }

    public void setDoctypeId(DocTypes doctypeId) {
        this.doctypeId = doctypeId;
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
        hash += (permID != null ? permID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permissions)) {
            return false;
        }
        Permissions other = (Permissions) object;
        if ((this.permID == null && other.permID != null) || (this.permID != null && !this.permID.equals(other.permID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Permissions[ permID=" + permID + " ]";
    }
    
}
