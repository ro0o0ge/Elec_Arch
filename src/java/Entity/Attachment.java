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
 * @author GIGABYTE
 */
@Entity
@Table(name = "attachment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Attachment.findAll", query = "SELECT a FROM Attachment a"),
    @NamedQuery(name = "Attachment.findByAId", query = "SELECT a FROM Attachment a WHERE a.aId = :aId"),
    @NamedQuery(name = "Attachment.findByDocSer", query = "SELECT a FROM Attachment a WHERE a.docSer.docSer = :docSer"),
    @NamedQuery(name = "Attachment.findByName", query = "SELECT a FROM Attachment a WHERE a.name = :name")})
public class Attachment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AId")
    private Integer aId;
    @Basic(optional = false)
    @Column(name = "Name")
    private String name;
    @JoinColumn(name = "DocSer", referencedColumnName = "DOC_SER")
    @ManyToOne(optional = false)
    private Documents docSer;
    @JoinColumn(name = "HomeId", referencedColumnName = "HOME_ID")
    @ManyToOne(optional = false)
    private DocHomes homeId;

    public Attachment() {
    }

    public Attachment(Integer aId) {
        this.aId = aId;
    }

    public Attachment(Integer aId, String name) {
        this.aId = aId;
        this.name = name;
    }

    public Integer getAId() {
        return aId;
    }

    public void setAId(Integer aId) {
        this.aId = aId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Documents getDocSer() {
        return docSer;
    }

    public void setDocSer(Documents docSer) {
        this.docSer = docSer;
    }

    public DocHomes getHomeId() {
        return homeId;
    }

    public void setHomeId(DocHomes homeId) {
        this.homeId = homeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aId != null ? aId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Attachment)) {
            return false;
        }
        Attachment other = (Attachment) object;
        if ((this.aId == null && other.aId != null) || (this.aId != null && !this.aId.equals(other.aId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Attachment[ aId=" + aId + " ]";
    }
    
}
