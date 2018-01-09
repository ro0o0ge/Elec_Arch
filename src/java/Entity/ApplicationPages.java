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
@Table(name = "application_pages")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ApplicationPages.findAll", query = "SELECT a FROM ApplicationPages a"),
    @NamedQuery(name = "ApplicationPages.findByPageID", query = "SELECT a FROM ApplicationPages a WHERE a.pageID = :pageID"),
    @NamedQuery(name = "ApplicationPages.findByAppPageName", query = "SELECT a FROM ApplicationPages a WHERE a.appPageName = :appPageName"),
    @NamedQuery(name = "ApplicationPages.findByPageDescription", query = "SELECT a FROM ApplicationPages a WHERE a.pageDescription = :pageDescription")})
public class ApplicationPages implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Page_ID")
    private Integer pageID;
    @Basic(optional = false)
    @Column(name = "App_Page_Name")
    private String appPageName;
    @Column(name = "Page_Description")
    private String pageDescription;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pageName")
    private List<PagePermission> pagePermissionList;

    public ApplicationPages() {
    }

    public ApplicationPages(Integer pageID) {
        this.pageID = pageID;
    }

    public ApplicationPages(Integer pageID, String appPageName) {
        this.pageID = pageID;
        this.appPageName = appPageName;
    }

    public Integer getPageID() {
        return pageID;
    }

    public void setPageID(Integer pageID) {
        this.pageID = pageID;
    }

    public String getAppPageName() {
        return appPageName;
    }

    public void setAppPageName(String appPageName) {
        this.appPageName = appPageName;
    }

    public String getPageDescription() {
        return pageDescription;
    }

    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }

    @XmlTransient
    public List<PagePermission> getPagePermissionList() {
        return pagePermissionList;
    }

    public void setPagePermissionList(List<PagePermission> pagePermissionList) {
        this.pagePermissionList = pagePermissionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pageID != null ? pageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApplicationPages)) {
            return false;
        }
        ApplicationPages other = (ApplicationPages) object;
        if ((this.pageID == null && other.pageID != null) || (this.pageID != null && !this.pageID.equals(other.pageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ApplicationPages[ pageID=" + pageID + " ]";
    }

}
