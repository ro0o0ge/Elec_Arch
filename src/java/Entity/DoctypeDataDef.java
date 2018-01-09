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
@Table(name = "doctype_data_def")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DoctypeDataDef.findAll", query = "SELECT d FROM DoctypeDataDef d"),
    @NamedQuery(name = "DoctypeDataDef.findByFieldName", query = "SELECT d FROM DoctypeDataDef d WHERE d.fieldName = :fieldName"),
    @NamedQuery(name = "DoctypeDataDef.findByFieldType", query = "SELECT d FROM DoctypeDataDef d WHERE d.fieldType = :fieldType"),
    @NamedQuery(name = "DoctypeDataDef.findByFieldLength", query = "SELECT d FROM DoctypeDataDef d WHERE d.fieldLength = :fieldLength"),
    @NamedQuery(name = "DoctypeDataDef.findByRequired", query = "SELECT d FROM DoctypeDataDef d WHERE d.required = :required"),
    @NamedQuery(name = "DoctypeDataDef.findByDataDefID", query = "SELECT d FROM DoctypeDataDef d WHERE d.dataDefID = :dataDefID"),
    @NamedQuery(name = "DoctypeDataDef.findByDocTypeIDFieldName", query = "SELECT d FROM DoctypeDataDef d WHERE d.doctypeId = :doctypeID and d.fieldName = :fieldName"),
   
    @NamedQuery(name = "DoctypeDataDef.findByDocTypeID", query = "SELECT d FROM DoctypeDataDef d WHERE d.doctypeId.doctypeId = :DocTypeID")})
public class DoctypeDataDef implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "FIELD_NAME")
    private String fieldName;
    @Basic(optional = false)
    @Column(name = "FIELD_TYPE")
    private String fieldType;
    @Basic(optional = false)
    @Column(name = "FIELD_LENGTH")
    private String fieldLength;
    @Basic(optional = false)
    @Column(name = "REQUIRED")
    private String required;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DataDef_ID")
    private Integer dataDefID;
    @JoinColumn(name = "DOCTYPE_ID", referencedColumnName = "DOCTYPE_ID")
    @ManyToOne(optional = false)
    private DocTypes doctypeId;

    public DoctypeDataDef() {
    }

    public DoctypeDataDef(Integer dataDefID) {
        this.dataDefID = dataDefID;
    }

    public DoctypeDataDef(Integer dataDefID, String fieldName, String fieldType, String fieldLength, String required) {
        this.dataDefID = dataDefID;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldLength = fieldLength;
        this.required = required;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(String fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public Integer getDataDefID() {
        return dataDefID;
    }

    public void setDataDefID(Integer dataDefID) {
        this.dataDefID = dataDefID;
    }

    public DocTypes getDoctypeId() {
        return doctypeId;
    }

    public void setDoctypeId(DocTypes doctypeId) {
        this.doctypeId = doctypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dataDefID != null ? dataDefID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DoctypeDataDef)) {
            return false;
        }
        DoctypeDataDef other = (DoctypeDataDef) object;
        if ((this.dataDefID == null && other.dataDefID != null) || (this.dataDefID != null && !this.dataDefID.equals(other.dataDefID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DoctypeDataDef[ dataDefID=" + dataDefID + " ]";
    }

}
