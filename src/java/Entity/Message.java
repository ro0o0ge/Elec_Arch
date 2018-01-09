/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author GIGABYTE
 */
@Entity
@Table(name = "message")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m"),
    @NamedQuery(name = "Message.findByMessageID", query = "SELECT m FROM Message m WHERE m.messageID = :messageID"),
    @NamedQuery(name = "Message.findBySubject", query = "SELECT m FROM Message m WHERE m.subject = :subject"),
    @NamedQuery(name = "Message.findByBody", query = "SELECT m FROM Message m WHERE m.body = :body"),
    @NamedQuery(name = "Message.findByDateSent", query = "SELECT m FROM Message m WHERE m.dateSent = :dateSent")})
public class Message implements Serializable {
    @OneToMany(mappedBy = "messageID")
    private List<Recievers> recieversList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Message_ID")
    private Integer messageID;
    @Basic(optional = false)
    @Column(name = "Subject")
    private String subject;
    @Basic(optional = false)
    @Column(name = "body")
    private String body;
    @Basic(optional = false)
    @Column(name = "Date_Sent")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSent;
    @JoinColumn(name = "Sender_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false)
    private Users senderID;

    public Message() {
    }

    public Message(Integer messageID) {
        this.messageID = messageID;
    }

    public Message(Integer messageID, String subject, String body, Date dateSent) {
        this.messageID = messageID;
        this.subject = subject;
        this.body = body;
        this.dateSent = dateSent;
    }

    public Integer getMessageID() {
        return messageID;
    }

    public void setMessageID(Integer messageID) {
        this.messageID = messageID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public Users getSenderID() {
        return senderID;
    }

    public void setSenderID(Users senderID) {
        this.senderID = senderID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (messageID != null ? messageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.messageID == null && other.messageID != null) || (this.messageID != null && !this.messageID.equals(other.messageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Message[ messageID=" + messageID + " ]";
    }

    @XmlTransient
    public List<Recievers> getRecieversList() {
        return recieversList;
    }

    public void setRecieversList(List<Recievers> recieversList) {
        this.recieversList = recieversList;
    }
    
}
