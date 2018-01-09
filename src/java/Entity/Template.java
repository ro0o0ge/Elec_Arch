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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author GIGABYTE
 */
@Entity
@Table(name = "template")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Template.findAll", query = "SELECT t FROM Template t"),
    @NamedQuery(name = "Template.findByTId", query = "SELECT t FROM Template t WHERE t.tId = :tId"),
    @NamedQuery(name = "Template.findByTBody", query = "SELECT t FROM Template t WHERE t.tBody = :tBody"),
    @NamedQuery(name = "Template.findByTTitle", query = "SELECT t FROM Template t WHERE t.tTitle = :tTitle")})
public class Template implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "T_ID")
    private Integer tId;
    @Basic(optional = false)
    @Column(name = "T_Body")
    private String tBody;
    @Basic(optional = false)
    @Column(name = "T_Title")
    private String tTitle;

    public Template() {
    }

    public Template(Integer tId) {
        this.tId = tId;
    }

    public Template(Integer tId, String tBody, String tTitle) {
        this.tId = tId;
        this.tBody = tBody;
        this.tTitle = tTitle;
    }

    public Integer getTId() {
        return tId;
    }

    public void setTId(Integer tId) {
        this.tId = tId;
    }

    public String getTBody() {
        return tBody;
    }

    public void setTBody(String tBody) {
        this.tBody = tBody;
    }

    public String getTTitle() {
        return tTitle;
    }

    public void setTTitle(String tTitle) {
        this.tTitle = tTitle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tId != null ? tId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Template)) {
            return false;
        }
        Template other = (Template) object;
        if ((this.tId == null && other.tId != null) || (this.tId != null && !this.tId.equals(other.tId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Template[ tId=" + tId + " ]";
    }
    
}
