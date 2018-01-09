/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Bean;

import Entity.TransactionLog;
import JPA.TransactionLogJpaController;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author GIGABYTE
 */
@ManagedBean
@RequestScoped
public class Transactionlog_Backing {

    /**
     * Creates a new instance of Transactionlog_Backing
     */
    TransactionLogJpaController JPA=new TransactionLogJpaController();
    List<TransactionLog> transactionList;
    List<TransactionLog> filteredTransactionList;
    
    public Transactionlog_Backing() {
        transactionList=JPA.findAll();
    }

    public List<TransactionLog> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<TransactionLog> transactionList) {
        this.transactionList = transactionList;
    }

    public List<TransactionLog> getFilteredTransactionList() {
        return filteredTransactionList;
    }

    public void setFilteredTransactionList(List<TransactionLog> filteredTransactionList) {
        this.filteredTransactionList = filteredTransactionList;
    }
    
    
    
    
}
