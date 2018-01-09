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
@Table(name = "doc_homes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocHomes.findAll", query = "SELECT d FROM DocHomes d"),
    @NamedQuery(name = "DocHomes.findByHomeId", query = "SELECT d FROM DocHomes d WHERE d.homeId = :homeId"),
    @NamedQuery(name = "DocHomes.findByHomePath", query = "SELECT d FROM DocHomes d WHERE d.homePath = :homePath"),
    @NamedQuery(name = "DocHomes.findByCurrentPath", query = "SELECT d FROM DocHomes d WHERE d.currentPath = :currentPath")})
public class DocHomes implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "homeId")
    private List<Attachment> attachmentList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "HOME_ID")
    private Integer homeId;
    @Basic(optional = false)
    @Column(name = "HOME_PATH")
    private String homePath;
    @Column(name = "CURRENT_PATH")
    private String currentPath;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "homeId")
    private List<Documents> documentsList;

    public DocHomes() {
    }

    public DocHomes(Integer homeId) {
        this.homeId = homeId;
    }

    public DocHomes(Integer homeId, String homePath) {
        this.homeId = homeId;
        this.homePath = homePath;
    }

    public Integer getHomeId() {
        return homeId;
    }

    public void setHomeId(Integer homeId) {
        this.homeId = homeId;
    }

    public String getHomePath() {
        return homePath;
    }

    public void setHomePath(String homePath) {
        this.homePath = homePath;
    }

    public boolean getCurrentPath() {
        return currentPath.equals("true");
    }

    public void setCurrentPath(boolean currentPath) {
        if (currentPath) {
            this.currentPath = "true";
        }else{
            this.currentPath = "false";
        }
    }

    @XmlTransient
    public List<Documents> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(List<Documents> documentsList) {
        this.documentsList = documentsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (homeId != null ? homeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocHomes)) {
            return false;
        }
        DocHomes other = (DocHomes) object;
        if ((this.homeId == null && other.homeId != null) || (this.homeId != null && !this.homeId.equals(other.homeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DocHomes[ homeId=" + homeId + " ]";
    }

    @XmlTransient
    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

}
