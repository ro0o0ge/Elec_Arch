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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "doc_types")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocTypes.findAll", query = "SELECT d FROM DocTypes d"),
    @NamedQuery(name = "DocTypes.findByDoctypeId", query = "SELECT d FROM DocTypes d WHERE d.doctypeId = :doctypeId"),
    @NamedQuery(name = "DocTypes.findByDocNameAppID", query = "SELECT d FROM DocTypes d WHERE d.doctypeName = :docName and d.approverID.userId = :userID"),
    
    @NamedQuery(name = "DocTypes.findByAppID", query = "SELECT d FROM DocTypes d WHERE d.approverID.userId = :userID"),
    
    @NamedQuery(name = "DocTypes.findByDoctypeName", query = "SELECT d FROM DocTypes d WHERE d.doctypeName = :doctypeName")})
public class DocTypes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DOCTYPE_ID")
    private Integer doctypeId;
    @Basic(optional = false)
    @Column(name = "DOCTYPE_NAME")
    private String doctypeName;
    @Lob
    @Column(name = "NOTES")
    private String notes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctypeId")
    private List<Documents> documentsList;
    @JoinColumn(name = "Approver_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private Users approverID;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctypeId")
    private List<Permissions> permissionsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctypeId")
    private List<DoctypeDataDef> doctypeDataDefList;

    public DocTypes() {
    }

    public DocTypes(Integer doctypeId) {
        this.doctypeId = doctypeId;
    }

    public DocTypes(Integer doctypeId, String doctypeName) {
        this.doctypeId = doctypeId;
        this.doctypeName = doctypeName;
    }

    public Integer getDoctypeId() {
        return doctypeId;
    }

    public void setDoctypeId(Integer doctypeId) {
        this.doctypeId = doctypeId;
    }

    public String getDoctypeName() {
        return doctypeName;
    }

    public void setDoctypeName(String doctypeName) {
        this.doctypeName = doctypeName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @XmlTransient
    public List<Documents> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(List<Documents> documentsList) {
        this.documentsList = documentsList;
    }

    public Users getApproverID() {
        return approverID;
    }

    public void setApproverID(Users approverID) {
        this.approverID = approverID;
    }

    @XmlTransient
    public List<Permissions> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(List<Permissions> permissionsList) {
        this.permissionsList = permissionsList;
    }

    @XmlTransient
    public List<DoctypeDataDef> getDoctypeDataDefList() {
        return doctypeDataDefList;
    }

    public void setDoctypeDataDefList(List<DoctypeDataDef> doctypeDataDefList) {
        this.doctypeDataDefList = doctypeDataDefList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (doctypeId != null ? doctypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocTypes)) {
            return false;
        }
        DocTypes other = (DocTypes) object;
        if ((this.doctypeId == null && other.doctypeId != null) || (this.doctypeId != null && !this.doctypeId.equals(other.doctypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DocTypes[ doctypeId=" + doctypeId + " ]";
    }
    
}
