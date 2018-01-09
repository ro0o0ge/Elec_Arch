/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.DocHomes;
import JPA.DocHomesJpaController;
import JPA.exceptions.IllegalOrphanException;
import JPA.exceptions.NonexistentEntityException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author norsin
 */
@ManagedBean
@ViewScoped
public class DocHome_Backing implements Serializable {

    private static final long serialVersionUID = 2L;

    private int homeID = 0;
    private String homeDirectory;
    private boolean currentDirectory;

    DocHomes docHomesEntity = new DocHomes();
    DocHomes docHomeSelectedRow = new DocHomes();
    DocHomesJpaController JPA = new DocHomesJpaController();

    List<DocHomes> docHomeList;

    FacesContext context;
    private boolean disableAdd = false, disableEdit = true;

    public DocHome_Backing() {
        docHomeList = JPA.findDocHomesEntities();
    }

    public void setHomeID(int homeID) {
        this.homeID = homeID;
    }

    public int getHomeID() {
        return homeID;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setCurrentDirectory(boolean currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public boolean getCurrentDirectory() {
        return currentDirectory;
    }

    public void onDocHomeRowSelect(SelectEvent event) throws Exception {
//        homeID = docHomeSelectedRow.getHomeId();
        homeDirectory = docHomeSelectedRow.getHomePath();
        currentDirectory = docHomeSelectedRow.getCurrentPath();
        disableAdd = true;
        disableEdit = false;

    }

    public void New(ActionEvent actionEvent) throws IOException, Exception {
        context = FacesContext.getCurrentInstance();

        if (homeDirectory != null && !homeDirectory.equals("")) {

            Pattern pattern = Pattern.compile("[a-zA-Z]:/(([a-zA-Z0-9 _ أ ب ت ث ج ح خ د ذ ر ز س ش ص ض ط ظ ع غ ف ق ك ل م ن ه و ى ة ء ا ي ئ].+/?)?)*");
            Matcher matcher = pattern.matcher(homeDirectory);
            if (matcher.matches()) {

                if (!JPA.homePathExist(homeDirectory)) {
                    docHomesEntity.setHomePath(homeDirectory);
                    if (currentDirectory) {
                        for (DocHomes dh : docHomeList) {
                            if (dh.getCurrentPath()) {
                                dh.setCurrentPath(false);
                                JPA.editDocHome(dh);
                            }
                        }
                        docHomesEntity.setCurrentPath(true);
                    } else {
                        docHomesEntity.setCurrentPath(false);
                    }
                    JPA.create(docHomesEntity);
                    docHomeList = JPA.findDocHomesEntities();
                    context.addMessage(null, new FacesMessage("تــم", "إضـافـة الـمـسار"));
                } else {
                    context.addMessage(null, new FacesMessage("تـحـذيـر", "هـذا الـمـسـار مـوجـود بـالـفـعـل"));

                }
            } else {
                context.addMessage(null, new FacesMessage("تـحـذيـر", "المسار يجب ان تكون على الشكل الاتي يجب ان يكون المسار على الشكل الاتي \n  C:/New Folder/"));

            }
        } else {
            context.addMessage(null, new FacesMessage("تـحـذيـر", "أدخـل أسـم الـمـسـار"));

        }
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public void reseting() {
        homeID = 0;
        homeDirectory = "";
        currentDirectory = false;
        disableAdd = false;
        disableEdit = true;
        docHomeList = JPA.findDocHomesEntities();
        docHomeSelectedRow = null;

    }

    public void Delete(ActionEvent actionEvent) throws IOException, NonexistentEntityException, IllegalOrphanException {
        context = FacesContext.getCurrentInstance();

        List<String> except = JPA.destroy(docHomeSelectedRow.getHomeId());
        if (except == null) {
            docHomeList = JPA.findDocHomesEntities();
            context.addMessage(null, new FacesMessage("تــم", "إلـغـاء الـمـسـار"));
            reseting();
        } else {
//            for (String ex : except) {

            context.addMessage(null, new FacesMessage("تـحـذيـر", "لا يمكن مسح هذا المسار"));

//            }
        }

    }

    public void Edit(ActionEvent actionEvent) throws Exception, NonexistentEntityException {
        context = FacesContext.getCurrentInstance();

//        Pattern pattern = Pattern.compile("[a-zA-Z]:/(([a-zA-Z].+/?)?)*"); 
        Pattern pattern = Pattern.compile("[a-zA-Z]:/(([a-zA-Z0-9 _ أ ب ت ث ج ح خ د ذ ر ز س ش ص ض ط ظ ع غ ف ق ك ل م ن ه و ى ة ء ا ي ئ].+/?)?)*");
        Matcher matcher = pattern.matcher(homeDirectory);

        if (matcher.matches()) {
//            docHomeSelectedRow.setHomeId(homeID);
            docHomeSelectedRow.setHomePath(homeDirectory);
            if (currentDirectory) {
                for (DocHomes dh : docHomeList) {
                    if (!dh.getHomeId().equals(docHomeSelectedRow.getHomeId())) {
                        if (dh.getCurrentPath()) {
                            dh.setCurrentPath(false);
                            JPA.editDocHome(dh);
                        }
                    }
                }
                docHomeSelectedRow.setCurrentPath(true);
            } else {
                docHomeSelectedRow.setCurrentPath(false);
            }

            JPA.edit(docHomeSelectedRow);
//            List<DocHomes> d = JPA.getDocHomes_findCurrentHome();
//            if (d.isEmpty()) {
//                context.addMessage(null, new FacesMessage("تـحـذيـر", "يجب اختيار مكان حال لتخزين الملف"));
//                return;
//            }
            context.addMessage(null, new FacesMessage("تــم", "تـعـديـل بـيـانـات الـمـلـف"));
        } else {
            context.addMessage(null, new FacesMessage("تـحـذيـر", "يجب ان يكون المسار على الشكل الاتي \n  C:/اسم المجلد/"));

        }

    }

    public boolean isDisableAdd() {
        return disableAdd;
    }

    public void setDisableAdd(boolean disableAdd) {
        this.disableAdd = disableAdd;
    }

    public void setDocHomeSelectedRow(DocHomes docHomeSelectedRow) {
        this.docHomeSelectedRow = docHomeSelectedRow;
    }

    public DocHomes getDocHomeSelectedRow() {
        return docHomeSelectedRow;
    }

    public void setDocHomesEntity(DocHomes docHomesEntity) {
        this.docHomesEntity = docHomesEntity;
    }

    public DocHomes getDocHomesEntity() {
        return docHomesEntity;
    }

    public void setDocHomeList(List<DocHomes> docHomeList) {
        this.docHomeList = docHomeList;
    }

    public List<DocHomes> getDocHomeList() {
        //  TempdocHomeList=docHomeList;
        return docHomeList;
    }

    public boolean isDisableEdit() {
        return disableEdit;
    }

    public void setDisableEdit(boolean disableEdit) {
        this.disableEdit = disableEdit;
    }

}
