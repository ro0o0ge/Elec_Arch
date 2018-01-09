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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author GIGABYTE
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recievers.findAll", query = "SELECT r FROM Recievers r"),
    @NamedQuery(name = "Recievers.findByUserID", query = "SELECT r FROM Recievers r WHERE r.userID.userId = :userId order by r.messageID.dateSent desc"),
    @NamedQuery(name = "Recievers.findByGroupID", query = "SELECT r FROM Recievers r WHERE r.groupID.groupId = :groupId order by r.messageID.dateSent desc"),
    @NamedQuery(name = "Recievers.findByMsgID", query = "SELECT r FROM Recievers r WHERE r.messageID.messageID = :msgId and r.userID.userId = :userId"),

    @NamedQuery(name = "Recievers.findByRId", query = "SELECT r FROM Recievers r WHERE r.rId = :rId")})
public class Recievers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "R_ID")
    private Integer rId;
    @JoinColumn(name = "Message_ID", referencedColumnName = "Message_ID")
    @ManyToOne
    private Message messageID;
    @JoinColumn(name = "Doc_SER", referencedColumnName = "DOC_SER")
    @ManyToOne
    private Documents docSER;
    @JoinColumn(name = "Group_ID", referencedColumnName = "GROUP_ID")
    @ManyToOne
    private Groups groupID;
    @JoinColumn(name = "User_ID", referencedColumnName = "USER_ID")
    @ManyToOne
    private Users userID;

    public Recievers() {
    }

    public Recievers(Integer rId) {
        this.rId = rId;
    }

    public Integer getRId() {
        return rId;
    }

    public void setRId(Integer rId) {
        this.rId = rId;
    }

    public Message getMessageID() {
        return messageID;
    }

    public void setMessageID(Message messageID) {
        this.messageID = messageID;
    }

    public Documents getDocSER() {
        return docSER;
    }

    public void setDocSER(Documents docSER) {
        this.docSER = docSER;
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
        hash += (rId != null ? rId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recievers)) {
            return false;
        }
        Recievers other = (Recievers) object;
        if ((this.rId == null && other.rId != null) || (this.rId != null && !this.rId.equals(other.rId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Recievers[ rId=" + rId + " ]";
    }

}
